# Metasnap Frontend

This is the frontend for the Metasnap project, built with React, Vite, and Tailwind CSS.

## Features
- Fast development with Vite
- Modern React (v19)
- Tailwind CSS for utility-first styling
- ESLint for code quality

## Getting Started

### Prerequisites
- [Node.js](https://nodejs.org/) (v18 or newer recommended)
- [npm](https://www.npmjs.com/) (comes with Node.js)

### Installation
1. Install dependencies:
   ```sh
   npm install
   ```

2. Start the development server:
   ```sh
   npm run dev
   ```
   The app will be available at [http://localhost:5173](http://localhost:5173) by default.

3. Build for production:
   ```sh
   npm run build
   ```

4. Preview the production build:
   ```sh
   npm run preview
   ```

### Linting
To check code quality with ESLint:
```sh
npm run lint
```

### Running Tests
```sh
npm test
```

### Test Coverage
The project includes comprehensive unit tests for:
- **MetaAnalyzer**: Main component tests for form submission, API calls, loading states, and error handling
- **ScoreDisplay**: Component tests for score rendering, color changes, and SVG calculations
- **FeedbackCard**: Component tests for feedback rendering, different feedback types, and empty states
- **TagList**: Component tests for tag rendering, feedback display, and styling

Run tests in watch mode:
```sh
npm run test:ui
```

Run tests once:
```sh
npm run test:run
```

## Project Structure
- `src/` - Main source code
- `src/components/` - React components
- `src/components/__tests__/` - Component unit tests
- `src/test/` - Test configuration
- `index.html` - Entry HTML file
- `tailwind.config.js` - Tailwind CSS configuration
- `postcss.config.js` - PostCSS plugins

## Customization
- Tailwind CSS is configured in `tailwind.config.js`.
- PostCSS plugins are set in `postcss.config.js`.

## License
MIT (or specify your license)
