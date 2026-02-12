#!/bin/bash

# Deployment script for Eczane application
# This script is called by Jenkins or can be run manually

set -e

echo "=== Deploying Eczane Application ==="

# Navigate to application directory
cd /opt/eczane

# Pull latest changes (if deploying from git directly)
# git pull origin main

# Stop existing containers
echo "Stopping existing containers..."
docker-compose down

# Remove old images (optional)
# docker image prune -f

# Start containers
echo "Starting containers..."
docker-compose up -d

# Wait for application to start
echo "Waiting for application to start..."
sleep 30

# Health check
echo "Performing health check..."
if curl -f http://localhost:8080/api/actuator/health; then
    echo "✓ Application is healthy"
else
    echo "✗ Application health check failed"
    docker-compose logs app
    exit 1
fi

# Show container status
echo ""
echo "Container status:"
docker-compose ps

echo ""
echo "=== Deployment Complete ==="
echo "Application logs: docker-compose logs -f app"
