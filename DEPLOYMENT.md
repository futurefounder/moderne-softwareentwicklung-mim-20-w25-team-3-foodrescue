# Deployment Setup

This document explains how the automatic deployment to GitHub Pages is configured.

## Overview

The project uses GitHub Actions to automatically deploy both the frontend application and Maven-generated documentation to GitHub Pages whenever changes are pushed to the main branch.

## What gets deployed

1. **Frontend Application** (`/frontend/`) - The main FoodRescue web application
2. **Maven Site Documentation** (`/backend/target/site/`) - Auto-generated project documentation including:
   - Project information and reports
   - Dependencies analysis
   - Test coverage reports
   - Plugin information

## Deployment Structure

```
GitHub Pages Site Root
├── index.html              # Frontend application
├── script.js               # Frontend JavaScript
├── package.json            # Frontend package info
└── docs/                   # Maven-generated documentation
    ├── index.html          # Project documentation home
    ├── dependencies.html   # Dependencies report
    ├── jacoco/            # Test coverage reports
    └── ...                # Other Maven reports
```

## GitHub Actions Workflow

The deployment is handled by `.github/workflows/deploy.yml` which:

1. **Builds the backend** with Maven
2. **Lints the frontend** with ESLint
3. **Generates Maven site documentation**
4. **Combines everything** into a single deployment package
5. **Deploys to GitHub Pages** automatically

## Setup Requirements

### 1. Enable GitHub Pages

1. Go to your repository settings
2. Navigate to "Pages" section
3. Under "Source", select "GitHub Actions"

### 2. Repository Permissions

The workflow requires these permissions (already configured):

- `contents: read` - To checkout the code
- `pages: write` - To deploy to GitHub Pages
- `id-token: write` - For secure deployment

## Manual Deployment

You can also generate the documentation locally:

```bash
# Generate Maven site
cd backend
mvn clean site

# The documentation will be in backend/target/site/
```

## Accessing the Deployed Site

Once deployed, your site will be available at:

- **Main Application**: `https://[username].github.io/[repository-name]/`
- **Documentation**: `https://[username].github.io/[repository-name]/docs/`

## Troubleshooting

### Build Failures

Check the GitHub Actions logs in the "Actions" tab of your repository.

Common issues:

- **Maven build fails**: Check Java version compatibility
- **Node.js issues**: Verify package.json and dependencies
- **Deployment fails**: Check GitHub Pages settings and permissions

### Documentation Not Updating

The Maven site generation requires:

- Valid `pom.xml` configuration
- Proper site descriptor in `src/site/site.xml`
- All dependencies available

## Customization

### Adding More Reports

Edit `backend/pom.xml` in the `<reporting>` section to add more Maven plugins and reports.

### Changing Site Appearance

Modify `backend/src/site/site.xml` to customize:

- Site navigation
- Skin/theme
- Project information
- External links

### Frontend Build Process

The current setup simply copies frontend files. For more complex builds:

1. Add build scripts to `frontend/package.json`
2. Update the GitHub Actions workflow to run the build
3. Copy the built files instead of source files
