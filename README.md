# Metasnap SEO Analyzer

A comprehensive SEO meta tag analysis tool that evaluates websites for optimal search engine optimization. This project consists of a Spring Boot backend service and a React frontend application that work together to provide detailed SEO insights.

## 🎯 Project Overview

Metasnap SEO Analyzer is designed to help developers, marketers, and SEO specialists quickly assess the quality of a website's meta tags and provide actionable feedback for improvement. The system analyzes various SEO elements including:

- **Meta Tags**: Title, description, keywords, viewport, robots
- **Open Graph Tags**: For social media sharing optimization
- **Twitter Card Tags**: For Twitter sharing optimization
- **Structured Data**: Schema.org markup
- **Technical SEO**: Canonical URLs, language tags, character encoding

## 🏗️ System Architecture

### High-Level Architecture
```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│   React Frontend │ ◄──────────────► │ Spring Boot API │
│   (Port 5173)    │                 │  (Port 8080)    │
└─────────────────┘                 └─────────────────┘
                                              │
                                              ▼
                                    ┌─────────────────┐
                                    │   Target Website │
                                    │   (External)     │
                                    └─────────────────┘
```

### Technology Stack

#### Backend (`metasnap-backend/`)
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Build Tool**: Maven
- **Key Libraries**:
  - Jsoup: HTML parsing and meta tag extraction
  - Spring Web: REST API endpoints
  - Jakarta Validation: Request validation
  - JUnit 5 + Mockito: Unit testing

#### Frontend (`metasnap-frontend/`)
- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Testing**: Vitest + React Testing Library
- **Key Features**:
  - Real-time form validation
  - Responsive design
  - Interactive score visualization
  - Detailed feedback display

## 📋 Original Development Plan

### Phase 1: Backend Development
1. **Core Service Layer**
   - Create `MetaAnalyzerService` for URL analysis
   - Implement meta tag extraction using Jsoup
   - Develop scoring algorithm for SEO assessment
   - Add comprehensive error handling

2. **API Layer**
   - Design REST API endpoints
   - Implement request/response DTOs
   - Add input validation
   - Configure CORS for frontend integration

3. **Testing Strategy**
   - Unit tests for service layer
   - Integration tests for API endpoints
   - Mock external HTTP calls
   - Test error scenarios

### Phase 2: Frontend Development
1. **Core Components**
   - `MetaAnalyzer`: Main form component
   - `ScoreDisplay`: Visual score representation
   - `FeedbackCard`: Detailed feedback display
   - `TagList`: Meta tag inspection
   - `PreviewCard`: Social media preview

2. **User Experience**
   - Real-time form validation
   - Loading states and error handling
   - Responsive design for all devices
   - Interactive score visualization

3. **Testing Strategy**
   - Component unit tests
   - User interaction testing
   - API integration testing
   - Accessibility testing

### Phase 3: Integration & Polish
1. **Cross-Origin Configuration**
   - Configure CORS for local development
   - Handle preflight requests
   - Secure API endpoints

2. **Error Handling**
   - Graceful degradation
   - User-friendly error messages
   - Network error recovery

3. **Documentation**
   - Comprehensive README files
   - API documentation
   - Setup instructions

## 🚀 Getting Started

### Prerequisites
- **Java 17+** (for backend)
- **Node.js 18+** (for frontend)
- **Maven 3.6+** (for backend builds)
- **Git** (for version control)

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/aabraha/metasnap-seo-analyzer.git
   cd metasnap-seo-analyzer
   ```

2. **Start the Backend:**
   ```bash
   cd metasnap-backend
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`

3. **Start the Frontend:**
   ```bash
   cd metasnap-frontend
   npm install
   npm run dev
   ```
   The application will be available at `http://localhost:5173`

4. **Run Tests:**
   ```bash
   # Backend tests
   cd metasnap-backend
   mvn test
   
   # Frontend tests
   cd metasnap-frontend
   npm test
   ```

## 📊 Features

### SEO Analysis Capabilities
- **Meta Tag Detection**: Identifies presence of essential meta tags
- **Social Media Optimization**: Analyzes Open Graph and Twitter Card tags
- **Technical SEO**: Checks canonical URLs, language tags, and encoding
- **Content Quality**: Evaluates title and description length and relevance
- **Mobile Optimization**: Assesses viewport and mobile-friendly tags

### Scoring System
The analyzer uses a comprehensive scoring algorithm that evaluates:
- **Basic SEO (25 points)**: Title, description, keywords
- **Social Media (20 points)**: Open Graph and Twitter Card tags
- **Technical SEO (20 points)**: Canonical, language, encoding
- **Mobile SEO (15 points)**: Viewport, mobile optimization
- **Advanced SEO (20 points)**: Structured data, schema markup

### User Interface Features
- **Real-time Analysis**: Instant feedback as you enter URLs
- **Visual Score Display**: Interactive circular progress indicator
- **Detailed Feedback**: Specific recommendations for improvement
- **Tag Inspector**: View all extracted meta tags
- **Social Preview**: See how your site appears on social media

## 🧪 Testing

### Backend Testing
- **Service Layer Tests**: Comprehensive unit tests for `MetaAnalyzerService`
- **Controller Tests**: API endpoint testing with validation
- **Mock Integration**: Jsoup static method mocking
- **Error Scenario Testing**: Network failures, invalid URLs, missing content

### Frontend Testing
- **Component Tests**: Individual component behavior testing
- **Integration Tests**: Form submission and API interaction
- **User Interaction Tests**: Click events, form validation
- **Accessibility Tests**: Screen reader compatibility

## 📁 Project Structure

```
metasnap-seo-analyzer/
├── README.md                    # This file
├── .gitignore                   # Git ignore rules
├── metasnap-backend/            # Spring Boot backend
│   ├── src/
│   │   ├── main/java/
│   │   │   └── com/metasnap/metasnap_backend/
│   │   │       ├── controller/  # REST API controllers
│   │   │       ├── service/     # Business logic
│   │   │       └── dto/         # Data transfer objects
│   │   └── test/java/           # Unit tests
│   ├── pom.xml                  # Maven dependencies
│   └── README.md               # Backend-specific docs
└── metasnap-frontend/           # React frontend
    ├── src/
    │   ├── components/          # React components
    │   ├── test/                # Test setup
    │   └── main.jsx            # App entry point
    ├── package.json             # Node.js dependencies
    ├── vite.config.js           # Vite configuration
    └── README.md               # Frontend-specific docs
```

## 🔧 Development

### Backend Development
- **API Endpoints**: `POST /api/analyze` for URL analysis
- **Validation**: Jakarta Bean Validation for request validation
- **Error Handling**: Comprehensive exception handling with meaningful messages
- **CORS**: Configured for local development with frontend

### Frontend Development
- **State Management**: React hooks for local state
- **API Integration**: Fetch API for backend communication
- **Styling**: Tailwind CSS for responsive design
- **Testing**: Vitest with React Testing Library

## 🚀 Deployment

### Backend Deployment
- **Build**: `mvn clean package`
- **Run**: `java -jar target/metasnap-backend-0.0.1-SNAPSHOT.jar`
- **Environment**: Configure `application.properties` for production

### Frontend Deployment
- **Build**: `npm run build`
- **Serve**: Deploy `dist/` folder to web server
- **Environment**: Configure API endpoint for production backend

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 🙏 Acknowledgments

- **Jsoup**: For HTML parsing and meta tag extraction
- **Spring Boot**: For robust backend development
- **React**: For modern frontend development
- **Tailwind CSS**: For beautiful, responsive design
- **Vite**: For fast development and building

---

**Metasnap SEO Analyzer** - Making SEO analysis simple and accessible for everyone. 