# Docker Deployment Guide

## Prerequisites
1. Docker and Docker Compose installed
2. Docker Hub account
3. GitHub repository

## Setup Instructions

### 1. Configure Environment Variables

Update the `.env` file with your Docker Hub credentials:
```env
DOCKER_HUB_USERNAME=your-dockerhub-username
DOCKER_HUB_REPO=auction-system
```

### 2. Configure GitHub Secrets

Add the following secrets to your GitHub repository (Settings > Secrets and variables > Actions):

- `DOCKER_HUB_USERNAME`: Your Docker Hub username
- `DOCKER_HUB_TOKEN`: Your Docker Hub Access Token (create one at https://hub.docker.com/settings/security)
- `DOCKER_HUB_REPO`: Repository name (e.g., `auction-system`)

### 3. CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/docker-publish.yml`) will automatically:
- Build the Docker image when you push to or create a PR against the `master` branch
- Push the image to Docker Hub with two tags:
  - `latest`: Always points to the most recent build
  - `<commit-sha>`: Specific version tied to the commit

### 4. Deploy with Docker Compose

The `compose.yaml` includes:
- **MySQL**: Database service
- **Keycloak**: Authentication service
- **Keycloak Config CLI**: Configures Keycloak automatically
- **Backend**: Your Spring Boot application (pulled from Docker Hub)

To start all services:
```bash
docker-compose up -d
```

To pull the latest backend image and restart:
```bash
docker-compose pull backend
docker-compose up -d backend
```

### 5. Access the Application

- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Keycloak Admin: http://localhost:8180
- MySQL: localhost:3307

## Building Locally

To build the Docker image locally:
```bash
docker build -t auction-system:local .
```

## Troubleshooting

Check backend logs:
```bash
docker-compose logs -f backend
```

Check all services status:
```bash
docker-compose ps
```

Restart a specific service:
```bash
docker-compose restart backend
```
