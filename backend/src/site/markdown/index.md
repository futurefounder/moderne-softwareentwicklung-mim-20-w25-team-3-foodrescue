# FoodRescue Project

Welcome to the FoodRescue project documentation!

## About

FoodRescue is a web application designed to help reduce food waste by connecting people who have excess food with those who need it.

## Architecture

This project consists of:

- **Backend**: Spring Boot REST API (Java 21)
- **Frontend**: Simple HTML/CSS/JavaScript application

## Features

- Health check endpoint for monitoring
- Modern responsive web interface
- Comprehensive test coverage
- Automated deployment via GitHub Actions

## Getting Started

### Prerequisites

- Java 21
- Maven 3.6+
- Node.js 20+

### Running the Application

1. Start the backend:

   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. Open the frontend:
   ```bash
   cd frontend
   # Serve the files with any HTTP server
   python -m http.server 8080
   ```

## Documentation

- [Project Information](project-info.html) - Generated project reports
- [Dependencies](dependencies.html) - Project dependencies
- [Test Coverage](jacoco/index.html) - Code coverage reports

## Links

- [Live Application](../index.html)
- [GitHub Repository](https://github.com/futurefounder/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue)
