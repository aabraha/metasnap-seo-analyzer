import React from 'react'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import TagList from '../TagList'

describe('TagList', () => {
  test('renders meta tags title', () => {
    render(<TagList tags={{}} feedback={[]} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
  })

  test('renders feedback title', () => {
    render(<TagList tags={{}} feedback={[]} />)
    
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('renders meta tags with values', () => {
    const tags = {
      title: 'Test Title',
      description: 'Test Description'
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('Test Title')).toBeInTheDocument()
    expect(screen.getByText('Test Description')).toBeInTheDocument()
  })

  test('displays "Missing" for null tag values', () => {
    const tags = {
      title: 'Test Title',
      description: null
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('Test Title')).toBeInTheDocument()
    expect(screen.getByText('Missing')).toBeInTheDocument()
  })

  test('displays "Missing" for undefined tag values', () => {
    const tags = {
      title: 'Test Title',
      description: undefined
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('Test Title')).toBeInTheDocument()
    expect(screen.getByText('Missing')).toBeInTheDocument()
  })

  test('displays "Missing" for empty string tag values', () => {
    const tags = {
      title: 'Test Title',
      description: ''
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('Test Title')).toBeInTheDocument()
    expect(screen.getByText('Missing')).toBeInTheDocument()
  })

  test('applies green styling for present tags', () => {
    const tags = {
      title: 'Test Title'
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    const titleSpan = screen.getByText('Test Title')
    expect(titleSpan).toHaveClass('bg-green-100', 'text-green-700')
  })

  test('applies red styling for missing tags', () => {
    const tags = {
      title: 'Test Title',
      description: null
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    const missingSpan = screen.getByText('Missing')
    expect(missingSpan).toHaveClass('bg-red-100', 'text-red-700')
  })

  test('renders feedback items with messages', () => {
    const feedback = [
      { type: 'good', message: 'Title tag is present' },
      { type: 'warning', message: 'Description meta tag is missing' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    expect(screen.getByText('Title tag is present')).toBeInTheDocument()
    expect(screen.getByText('Description meta tag is missing')).toBeInTheDocument()
  })

  test('applies correct styling for good feedback', () => {
    const feedback = [
      { type: 'good', message: 'Title tag is present' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItem = screen.getByText('Title tag is present').closest('li')
    expect(feedbackItem).toHaveClass('bg-green-100', 'text-green-700', 'border-green-300')
  })

  test('applies correct styling for warning feedback', () => {
    const feedback = [
      { type: 'warning', message: 'Description meta tag is missing' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItem = screen.getByText('Description meta tag is missing').closest('li')
    expect(feedbackItem).toHaveClass('bg-yellow-100', 'text-yellow-700', 'border-yellow-300')
  })

  test('applies correct styling for missing feedback', () => {
    const feedback = [
      { type: 'missing', message: 'Open Graph tags are missing' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItem = screen.getByText('Open Graph tags are missing').closest('li')
    expect(feedbackItem).toHaveClass('bg-red-100', 'text-red-700', 'border-red-300')
  })

  test('handles unknown feedback type gracefully', () => {
    const feedback = [
      { type: 'unknown', message: 'Unknown feedback type' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItem = screen.getByText('Unknown feedback type').closest('li')
    expect(feedbackItem).toHaveClass('bg-gray-100', 'text-gray-700', 'border-gray-300')
  })

  test('renders multiple feedback items in order', () => {
    const feedback = [
      { type: 'good', message: 'First feedback' },
      { type: 'warning', message: 'Second feedback' },
      { type: 'missing', message: 'Third feedback' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItems = screen.getAllByRole('listitem')
    expect(feedbackItems).toHaveLength(3)
    expect(feedbackItems[0]).toHaveTextContent('First feedback')
    expect(feedbackItems[1]).toHaveTextContent('Second feedback')
    expect(feedbackItems[2]).toHaveTextContent('Third feedback')
  })

  test('converts camelCase tag names to space-separated format', () => {
    const tags = {
      metaTitle: 'Test Title',
      openGraphDescription: 'Test Description'
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('meta Title')).toBeInTheDocument()
    expect(screen.getByText('open Graph Description')).toBeInTheDocument()
  })

  test('handles empty tags object', () => {
    render(<TagList tags={{}} feedback={[]} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('handles null tags prop', () => {
    render(<TagList tags={null} feedback={[]} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('handles undefined tags prop', () => {
    render(<TagList tags={undefined} feedback={[]} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('handles empty feedback array', () => {
    render(<TagList tags={{}} feedback={[]} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('handles null feedback prop', () => {
    render(<TagList tags={{}} feedback={null} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('handles undefined feedback prop', () => {
    render(<TagList tags={{}} feedback={undefined} />)
    
    expect(screen.getByText('Meta Tags')).toBeInTheDocument()
    expect(screen.getByText('Feedback')).toBeInTheDocument()
  })

  test('renders feedback items with correct classes', () => {
    const feedback = [
      { type: 'good', message: 'Test feedback' }
    ]
    
    render(<TagList tags={{}} feedback={feedback} />)
    
    const feedbackItem = screen.getByText('Test feedback').closest('li')
    expect(feedbackItem).toHaveClass('border-l-4', 'pl-2', 'py-1', 'rounded')
  })

  test('handles boolean tag values', () => {
    const tags = {
      title: true,
      description: false
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('true')).toBeInTheDocument()
    expect(screen.getByText('false')).toBeInTheDocument()
  })

  test('handles number tag values', () => {
    const tags = {
      score: 85,
      count: 0
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('85')).toBeInTheDocument()
    expect(screen.getByText('0')).toBeInTheDocument()
  })

  test('handles complex tag values', () => {
    const tags = {
      url: 'https://example.com',
      data: { key: 'value' }
    }
    
    render(<TagList tags={tags} feedback={[]} />)
    
    expect(screen.getByText('https://example.com')).toBeInTheDocument()
    expect(screen.getByText('{"key":"value"}')).toBeInTheDocument()
  })
}) 