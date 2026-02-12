#!/bin/bash

# Eczane Application Server Setup Script
# This script sets up the server environment for the Eczane application

set -e

echo "=== Eczane Application Server Setup ==="

# Update system
echo "Updating system packages..."
apt-get update
apt-get upgrade -y

# Install Docker
echo "Installing Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    rm get-docker.sh
    systemctl enable docker
    systemctl start docker
else
    echo "Docker already installed"
fi

# Install Docker Compose
echo "Installing Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
else
    echo "Docker Compose already installed"
fi

# Install Java (for Jenkins agent if needed)
echo "Installing Java..."
apt-get install -y openjdk-17-jdk

# Install Git
echo "Installing Git..."
apt-get install -y git

# Create application directory
echo "Creating application directory..."
mkdir -p /opt/eczane
cd /opt/eczane

# Create log directory
mkdir -p /var/log/eczane-app

# Install Jenkins
echo "Installing Jenkins..."
if ! command -v jenkins &> /dev/null; then
    wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | apt-key add -
    sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    apt-get update
    apt-get install -y jenkins
    systemctl enable jenkins
    systemctl start jenkins
    
    echo "Jenkins installed. Initial admin password:"
    cat /var/lib/jenkins/secrets/initialAdminPassword
else
    echo "Jenkins already installed"
fi

# Configure firewall
echo "Configuring firewall..."
apt-get install -y ufw
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw allow 8080/tcp  # Jenkins
ufw --force enable

# Setup SSL with Let's Encrypt (commented out - requires domain verification)
# echo "Installing Certbot for SSL..."
# apt-get install -y certbot
# certbot certonly --standalone -d cengizozdemir.duckdns.org --non-interactive --agree-tos -m your-email@example.com

echo ""
echo "=== Setup Complete ==="
echo "Next steps:"
echo "1. Configure .env file in /opt/eczane/"
echo "2. Setup Jenkins credentials for GitHub and SSH"
echo "3. Create Jenkins pipeline job pointing to your repository"
echo "4. Configure SSL certificates in nginx/ssl/"
echo "5. Run: cd /opt/eczane && docker-compose up -d"
echo ""
echo "Access points:"
echo "- Application: http://cengizozdemir.duckdns.org"
echo "- Jenkins: http://185.136.206.32:8080"
echo "- Grafana: http://cengizozdemir.duckdns.org/grafana"
echo "- Glowroot: http://cengizozdemir.duckdns.org/glowroot"
