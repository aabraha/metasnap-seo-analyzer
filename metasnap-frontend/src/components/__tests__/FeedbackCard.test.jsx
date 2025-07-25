import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import FeedbackCard from '../FeedbackCard'

describe('FeedbackCard', () => {
  it('renders the feedback card with title', () => {
    render(<FeedbackCard feedback={[]} />)
    
    expect(screen.getByText('Suggestions')).toBeInTheDocument()
  })

  it('renders feedback items when provided', () => {
    const feedback = [
      { type: 'good', message: 'Title tag is present' },
      { type: 'warning', message: 'Description meta tag is missing' },
      { type: 'missing', message: 'Open Graph tags are missing' }
    ]
    
    render(<FeedbackCard feedback={feedback} />)
    
    expect(screen.getByText('Title tag is present')).toBeInTheDocument()
    expect(screen.getByText('Description meta tag is missing')).toBeInTheDocument()
    expect(screen.getByText('Open Graph tags are missing')).toBeInTheDocument()
  })

  it('renders "No suggestions" when feedback is empty', () => {
    render(<FeedbackCard feedback={[]} />)
    
    expect(screen.getByText('No suggestions')).toBeInTheDocument()
  })

  it('renders "No suggestions" when feedback is null', () => {
    render(<FeedbackCard feedback={null} />)
    
    expect(screen.getByText('No suggestions')).toBeInTheDocument()
  })

  it('renders "No suggestions" when feedback is undefined', () => {
    render(<FeedbackCard feedback={undefined} />)
    
    expect(screen.getByText('No suggestions')).toBeInTheDocument()
  })

  it('applies correct text color for good feedback', () => {
    const feedback = [{ type: 'good', message: 'Title tag is present' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const messageElement = screen.getByText('Title tag is present')
    expect(messageElement).toHaveClass('text-green-700')
  })

  it('applies correct text color for warning feedback', () => {
    const feedback = [{ type: 'warning', message: 'Description meta tag is missing' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const messageElement = screen.getByText('Description meta tag is missing')
    expect(messageElement).toHaveClass('text-yellow-700')
  })

  it('applies correct text color for missing feedback', () => {
    const feedback = [{ type: 'missing', message: 'Open Graph tags are missing' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const messageElement = screen.getByText('Open Graph tags are missing')
    expect(messageElement).toHaveClass('text-red-700')
  })

  it('renders correct icon for good feedback', () => {
    const feedback = [{ type: 'good', message: 'Title tag is present' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const icon = screen.getByText('Title tag is present').previousElementSibling
    expect(icon).toHaveClass('w-5', 'h-5', 'text-green-500', 'mr-2')
  })

  it('renders correct icon for warning feedback', () => {
    const feedback = [{ type: 'warning', message: 'Description meta tag is missing' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const icon = screen.getByText('Description meta tag is missing').previousElementSibling
    expect(icon).toHaveClass('w-5', 'h-5', 'text-yellow-500', 'mr-2')
  })

  it('renders correct icon for missing feedback', () => {
    const feedback = [{ type: 'missing', message: 'Open Graph tags are missing' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const icon = screen.getByText('Open Graph tags are missing').previousElementSibling
    expect(icon).toHaveClass('w-5', 'h-5', 'text-red-500', 'mr-2')
  })

  it('handles unknown feedback type gracefully', () => {
    const feedback = [{ type: 'unknown', message: 'Unknown feedback type' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    expect(screen.getByText('Unknown feedback type')).toBeInTheDocument()
    // Should not have any specific color class since type is unknown
    const messageElement = screen.getByText('Unknown feedback type')
    expect(messageElement).not.toHaveClass('text-green-700', 'text-yellow-700', 'text-red-700')
  })

  it('renders multiple feedback items in correct order', () => {
    const feedback = [
      { type: 'good', message: 'First feedback' },
      { type: 'warning', message: 'Second feedback' },
      { type: 'missing', message: 'Third feedback' }
    ]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const items = screen.getAllByRole('listitem')
    expect(items).toHaveLength(3)
    expect(items[0]).toHaveTextContent('First feedback')
    expect(items[1]).toHaveTextContent('Second feedback')
    expect(items[2]).toHaveTextContent('Third feedback')
  })

  it('renders container with correct classes', () => {
    render(<FeedbackCard feedback={[]} />)
    
    const container = screen.getByText('Suggestions').parentElement
    expect(container).toHaveClass('bg-white', 'rounded-xl', 'shadow', 'p-6', 'min-h-[180px]', 'flex', 'flex-col')
  })

  it('renders title with correct classes', () => {
    render(<FeedbackCard feedback={[]} />)
    
    const title = screen.getByText('Suggestions')
    expect(title).toHaveClass('font-bold', 'text-lg', 'mb-3')
  })

  it('renders list with correct classes', () => {
    render(<FeedbackCard feedback={[]} />)
    
    const list = screen.getByRole('list')
    expect(list).toHaveClass('space-y-2', 'flex-1')
  })

  it('renders list items with correct classes', () => {
    const feedback = [{ type: 'good', message: 'Test feedback' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const listItem = screen.getByRole('listitem')
    expect(listItem).toHaveClass('flex', 'items-center', 'text-sm')
  })

  it('handles empty message gracefully', () => {
    const feedback = [{ type: 'good', message: '' }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const listItem = screen.getByRole('listitem')
    expect(listItem).toBeInTheDocument()
  })

  it('handles null message gracefully', () => {
    const feedback = [{ type: 'good', message: null }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const listItem = screen.getByRole('listitem')
    expect(listItem).toBeInTheDocument()
  })

  it('handles undefined message gracefully', () => {
    const feedback = [{ type: 'good', message: undefined }]
    
    render(<FeedbackCard feedback={feedback} />)
    
    const listItem = screen.getByRole('listitem')
    expect(listItem).toBeInTheDocument()
  })

  it('renders "No suggestions" with correct styling', () => {
    render(<FeedbackCard feedback={[]} />)
    
    const noSuggestions = screen.getByText('No suggestions')
    expect(noSuggestions).toHaveClass('text-gray-400')
  })
}) 