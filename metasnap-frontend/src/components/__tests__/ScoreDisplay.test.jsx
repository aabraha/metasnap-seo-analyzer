import React from 'react'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import ScoreDisplay from '../ScoreDisplay'

describe('ScoreDisplay', () => {
  test('renders correct score and label', () => {
    render(<ScoreDisplay score={85} />)
    
    expect(screen.getByText('85')).toBeInTheDocument()
    expect(screen.getByText('SEO SCORE')).toBeInTheDocument()
  })

  test('applies green color for high scores (>= 80)', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke', '#22c55e')
  })

  test('handles score of exactly 80 (green)', () => {
    render(<ScoreDisplay score={80} />)
    
    const svg = screen.getByText('80').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke', '#22c55e')
  })

  test('handles score of exactly 60 (yellow)', () => {
    render(<ScoreDisplay score={60} />)
    
    const svg = screen.getByText('60').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke', '#facc15')
  })

  test('handles score of 59 (red)', () => {
    render(<ScoreDisplay score={59} />)
    
    const svg = screen.getByText('59').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke', '#ef4444')
  })

  test('handles zero score', () => {
    render(<ScoreDisplay score={0} />)
    
    expect(screen.getByText('0')).toBeInTheDocument()
    const svg = screen.getByText('0').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke', '#ef4444')
  })

  test('handles negative score (shows as negative)', () => {
    render(<ScoreDisplay score={-10} />)
    
    expect(screen.getByText('-10')).toBeInTheDocument()
  })

  test('handles score over 100 (shows as > 100)', () => {
    render(<ScoreDisplay score={150} />)
    
    expect(screen.getByText('150')).toBeInTheDocument()
  })

  test('renders SVG with correct dimensions', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    expect(svg).toHaveAttribute('height', '96') // radius * 2 = 48 * 2
    expect(svg).toHaveAttribute('width', '96')
  })

  test('renders background circle with gray color', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    const backgroundCircle = svg.querySelector('circle:nth-child(1)')
    expect(backgroundCircle).toHaveAttribute('stroke', '#e5e7eb')
  })

  test('renders progress circle with correct stroke properties', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveAttribute('stroke-linecap', 'round')
    expect(progressCircle).toHaveAttribute('stroke-width', '8')
  })

  test('renders text with correct styling', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    const text = svg.querySelector('text')
    expect(text).toHaveAttribute('text-anchor', 'middle')
    expect(text).toHaveAttribute('font-size', '2.2rem')
  })

  test('applies transition style to progress circle', () => {
    render(<ScoreDisplay score={85} />)
    
    const svg = screen.getByText('85').closest('svg')
    const progressCircle = svg.querySelector('circle:nth-child(2)')
    expect(progressCircle).toHaveStyle('transition: stroke-dashoffset 0.5s')
  })
}) 