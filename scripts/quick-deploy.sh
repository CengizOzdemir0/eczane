#!/bin/bash
# Eczane Uygulaması - Hızlı Deployment Script
# Bu script'i sunucuda çalıştırın: bash quick-deploy.sh

set -e

echo "=========================================="
echo "Eczane Uygulaması - Deployment Başlıyor"
echo "=========================================="

# Renk kodları
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Docker kurulumu
echo -e "${YELLOW}[1/8] Docker kurulumu...${NC}"
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com | sh
    systemctl enable docker
    systemctl start docker
    echo -e "${GREEN}✓ Docker kuruldu${NC}"
else
    echo -e "${GREEN}✓ Docker zaten kurulu${NC}"
fi

# 2. Docker Compose kurulumu
echo -e "${YELLOW}[2/8] Docker Compose kurulumu...${NC}"
if ! command -v docker-compose &> /dev/null; then
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    echo -e "${GREEN}✓ Docker Compose kuruldu${NC}"
else
    echo -e "${GREEN}✓ Docker Compose zaten kurulu${NC}"
fi

# 3. Dizin oluşturma
echo -e "${YELLOW}[3/8] Dizinler oluşturuluyor...${NC}"
mkdir -p /opt/eczane
mkdir -p /opt/backups
mkdir -p /var/log/eczane-app
echo -e "${GREEN}✓ Dizinler oluşturuldu${NC}"

# 4. Projeyi çekme
echo -e "${YELLOW}[4/8] GitHub'dan proje çekiliyor...${NC}"
cd /opt/eczane
if [ -d ".git" ]; then
    git pull origin main
    echo -e "${GREEN}✓ Proje güncellendi${NC}"
else
    git clone https://github.com/CengizOzdemir0/eczane.git .
    echo -e "${GREEN}✓ Proje çekildi${NC}"
fi

# 5. Environment dosyası
echo -e "${YELLOW}[5/8] Environment dosyası oluşturuluyor...${NC}"
if [ ! -f ".env" ]; then
    cat > .env << 'EOF'
DB_NAME=eczane_db
DB_USERNAME=eczane_user
DB_PASSWORD=EczaneSecure2024!
REDIS_PASSWORD=RedisSecure2024!
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
GRAFANA_USER=admin
GRAFANA_PASSWORD=GrafanaSecure2024!
SERVER_PORT=8080
EOF
    chmod 600 .env
    echo -e "${GREEN}✓ .env dosyası oluşturuldu${NC}"
else
    echo -e "${GREEN}✓ .env dosyası zaten mevcut${NC}"
fi

# 6. Firewall ayarları
echo -e "${YELLOW}[6/8] Firewall yapılandırılıyor...${NC}"
if command -v ufw &> /dev/null; then
    ufw allow 22/tcp
    ufw allow 80/tcp
    ufw allow 443/tcp
    ufw allow 8080/tcp
    ufw --force enable
    echo -e "${GREEN}✓ Firewall yapılandırıldı${NC}"
else
    apt-get install -y ufw
    ufw allow 22/tcp
    ufw allow 80/tcp
    ufw allow 443/tcp
    ufw allow 8080/tcp
    ufw --force enable
    echo -e "${GREEN}✓ Firewall kuruldu ve yapılandırıldı${NC}"
fi

# 7. Docker Compose başlatma
echo -e "${YELLOW}[7/8] Docker container'ları başlatılıyor...${NC}"
cd /opt/eczane
docker-compose down 2>/dev/null || true
docker-compose up -d
echo -e "${GREEN}✓ Container'lar başlatıldı${NC}"

# 8. Health check
echo -e "${YELLOW}[8/8] Health check yapılıyor...${NC}"
echo "30 saniye bekleniyor..."
sleep 30

if curl -f http://localhost:8080/api/actuator/health &> /dev/null; then
    echo -e "${GREEN}✓ Uygulama başarıyla çalışıyor!${NC}"
else
    echo -e "${YELLOW}⚠ Uygulama henüz hazır değil. Logları kontrol edin: docker-compose logs app${NC}"
fi

echo ""
echo "=========================================="
echo -e "${GREEN}Deployment Tamamlandı!${NC}"
echo "=========================================="
echo ""
echo "Erişim Bilgileri:"
echo "- Uygulama: http://185.136.206.32:8080"
echo "- Grafana: http://185.136.206.32:3000 (admin/GrafanaSecure2024!)"
echo "- Glowroot: http://185.136.206.32:4000"
echo ""
echo "Faydalı Komutlar:"
echo "- Logları izle: docker-compose logs -f"
echo "- Container durumu: docker-compose ps"
echo "- Yeniden başlat: docker-compose restart"
echo ""
