import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import MetaAnalyzer from '../MetaAnalyzer'

// Mock the child components
vi.mock('../ScoreDisplay', () => ({
  default: ({ score }) => <div data-testid="score-display">Score: {score}</div>
}))

vi.mock('../FeedbackCard', () => ({
  default: ({ feedback }) => <div data-testid="feedback-card">Feedback: {feedback?.length || 0} items</div>
}))

vi.mock('../MetaTagsCard', () => ({
  default: ({ tags }) => <div data-testid="meta-tags-card">Meta Tags: {tags?.title || 'No title'}</div>
}))

vi.mock('../PreviewCard', () => ({
  default: ({ type, tags }) => <div data-testid={`preview-card-${type.toLowerCase()}`}>{type} Preview</div>
}))

describe('MetaAnalyzer', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    global.fetch.mockClear()
  })

  it('renders the form with input and button', () => {
    render(<MetaAnalyzer />)
    
    expect(screen.getByPlaceholderText('https://example.com')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /analyze/i })).toBeInTheDocument()
  })

  it('updates URL input value when user types', async () => {
    const user = userEvent.setup()
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    await user.type(input, 'https://example.com')
    
    expect(input).toHaveValue('https://example.com')
  })

  it('shows loading state when form is submitted', async () => {
    const user = userEvent.setup()
    global.fetch.mockImplementation(() => new Promise(() => {})) // Never resolves
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    expect(button).toHaveTextContent('Analyzing...')
    expect(button).toBeDisabled()
    expect(input).toBeDisabled()
  })

  it('calls API with correct URL when form is submitted', async () => {
    const user = userEvent.setup()
    const mockResponse = { score: 85, meta: { title: 'Test' }, feedback: [] }
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    })
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/api/analyze', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ url: 'https://example.com' })
    })
  })

  it('displays results when API call is successful', async () => {
    const user = userEvent.setup()
    const mockResponse = {
      score: 85,
      meta: { title: 'Test Page', description: 'Test description' },
      feedback: [{ type: 'good', message: 'Title present' }],
      breakdown: { titleDescription: 30 }
    }
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    })
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(screen.getByTestId('score-display')).toBeInTheDocument()
      expect(screen.getByTestId('feedback-card')).toBeInTheDocument()
      expect(screen.getByTestId('meta-tags-card')).toBeInTheDocument()
      expect(screen.getByTestId('preview-card-google')).toBeInTheDocument()
      expect(screen.getByTestId('preview-card-facebook')).toBeInTheDocument()
      expect(screen.getByTestId('preview-card-twitter')).toBeInTheDocument()
    })
  })

  it('shows error alert when API call fails', async () => {
    const user = userEvent.setup()
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {})
    
    global.fetch.mockRejectedValueOnce(new Error('Network error'))
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Failed to analyze URL. Is the backend running?')
    })
    
    alertSpy.mockRestore()
  })

  it('shows error alert when API returns non-ok response', async () => {
    const user = userEvent.setup()
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {})
    
    global.fetch.mockResolvedValueOnce({
      ok: false,
      status: 500
    })
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Failed to analyze URL. Is the backend running?')
    })
    
    alertSpy.mockRestore()
  })

  it('prevents form submission when URL is empty', async () => {
    const user = userEvent.setup()
    render(<MetaAnalyzer />)
    
    const button = screen.getByRole('button', { name: /analyze/i })
    await user.click(button)
    
    expect(global.fetch).not.toHaveBeenCalled()
  })

  it('resets loading state after successful API call', async () => {
    const user = userEvent.setup()
    const mockResponse = { score: 85, meta: { title: 'Test' }, feedback: [] }
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    })
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(button).toHaveTextContent('Analyze')
      expect(button).not.toBeDisabled()
      expect(input).not.toBeDisabled()
    })
  })

  it('resets loading state after failed API call', async () => {
    const user = userEvent.setup()
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {})
    
    global.fetch.mockRejectedValueOnce(new Error('Network error'))
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(button).toHaveTextContent('Analyze')
      expect(button).not.toBeDisabled()
      expect(input).not.toBeDisabled()
    })
    
    alertSpy.mockRestore()
  })

  it('clears previous results when new analysis starts', async () => {
    const user = userEvent.setup()
    const mockResponse = { score: 85, meta: { title: 'Test' }, feedback: [] }
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    })
    
    render(<MetaAnalyzer />)
    
    const input = screen.getByPlaceholderText('https://example.com')
    const button = screen.getByRole('button', { name: /analyze/i })
    
    // First analysis
    await user.type(input, 'https://example.com')
    await user.click(button)
    
    await waitFor(() => {
      expect(screen.getByTestId('score-display')).toBeInTheDocument()
    })
    
    // Clear input and start new analysis
    await user.clear(input)
    await user.type(input, 'https://another-example.com')
    
    // Mock second API call
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ score: 90, meta: { title: 'New Test' }, feedback: [] })
    })
    
    await user.click(button)
    
    // Results should be updated with new data
    await waitFor(() => {
      expect(screen.getByTestId('score-display')).toBeInTheDocument()
    })
  })

  it('renders footer with current year', () => {
    render(<MetaAnalyzer />)
    
    const currentYear = new Date().getFullYear()
    expect(screen.getByText(new RegExp(currentYear.toString()))).toBeInTheDocument()
    expect(screen.getByText(/MetaSnap/)).toBeInTheDocument()
  })
}) 