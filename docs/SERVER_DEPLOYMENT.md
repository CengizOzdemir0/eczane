# Sunucu Deployment Komutları

## 1. Sunucuya Bağlanma
MobaXterm ile zaten bağlandınız: root@185.136.206.32

## 2. Sistem Güncellemesi ve Gerekli Paketler
```bash
# Sistem güncellemesi
apt-get update && apt-get upgrade -y

# Gerekli paketleri kur
apt-get install -y git curl wget unzip
```

## 3. Docker Kurulumu
```bash
# Docker kurulumu
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
rm get-docker.sh

# Docker'ı başlat
systemctl enable docker
systemctl start docker

# Docker durumunu kontrol et
docker --version
```

## 4. Docker Compose Kurulumu
```bash
# Docker Compose kurulumu
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Çalıştırma izni ver
chmod +x /usr/local/bin/docker-compose

# Versiyonu kontrol et
docker-compose --version
```

## 5. Uygulama Dizinini Oluştur
```bash
# Uygulama dizini
mkdir -p /opt/eczane
cd /opt/eczane

# Log dizini
mkdir -p /var/log/eczane-app
```

## 6. Projeyi GitHub'dan Çek
```bash
cd /opt/eczane
git clone https://github.com/CengizOzdemir0/eczane.git .
```

## 7. Environment Dosyasını Oluştur
```bash
# .env dosyasını oluştur
cat > /opt/eczane/.env << 'EOF'
# Database
DB_NAME=eczane_db
DB_USERNAME=eczane_user
DB_PASSWORD=EczaneSecure2024!

# Redis
REDIS_PASSWORD=RedisSecure2024!

# JWT - Bu secret'ı değiştirin!
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Grafana
GRAFANA_USER=admin
GRAFANA_PASSWORD=GrafanaSecure2024!

# Server
SERVER_PORT=8080
EOF

# Dosya izinlerini ayarla
chmod 600 /opt/eczane/.env
```

## 8. Docker Compose ile Başlat
```bash
cd /opt/eczane

# Container'ları başlat
docker-compose up -d

# Logları izle
docker-compose logs -f
```

## 9. Container Durumunu Kontrol Et
```bash
# Tüm container'ları listele
docker-compose ps

# Uygulama loglarını kontrol et
docker-compose logs app

# PostgreSQL loglarını kontrol et
docker-compose logs postgres

# Redis loglarını kontrol et
docker-compose logs redis
```

## 10. Health Check
```bash
# Uygulama health check
curl http://localhost:8080/api/actuator/health

# Beklenen çıktı: {"status":"UP"}
```

## 11. Firewall Ayarları
```bash
# UFW kurulumu
apt-get install -y ufw

# Port'ları aç
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw allow 8080/tcp  # Application (geçici - production'da kapatılacak)

# Firewall'ı aktif et
ufw --force enable

# Durumu kontrol et
ufw status
```

## 12. Nginx Yapılandırması (Opsiyonel - SSL için)
```bash
# Nginx container'ı zaten var, ancak SSL için:

# Let's Encrypt kurulumu
apt-get install -y certbot

# SSL sertifikası al (domain doğrulaması gerekli)
certbot certonly --standalone -d cengizozdemir.duckdns.org --non-interactive --agree-tos -m your-email@example.com

# Sertifikaları nginx dizinine kopyala
mkdir -p /opt/eczane/nginx/ssl
cp /etc/letsencrypt/live/cengizozdemir.duckdns.org/fullchain.pem /opt/eczane/nginx/ssl/cert.pem
cp /etc/letsencrypt/live/cengizozdemir.duckdns.org/privkey.pem /opt/eczane/nginx/ssl/key.pem

# Nginx'i yeniden başlat
docker-compose restart nginx
```

## 13. Erişim Kontrolleri

### Uygulama
```bash
curl http://185.136.206.32:8080/api/eczane/list
```

### Grafana
- URL: http://185.136.206.32:3000
- Kullanıcı: admin
- Şifre: GrafanaSecure2024!

### Glowroot
- URL: http://185.136.206.32:4000

## 14. Faydalı Komutlar

### Container Yönetimi
```bash
# Tüm container'ları durdur
docker-compose down

# Yeniden başlat
docker-compose restart

# Sadece app container'ını yeniden başlat
docker-compose restart app

# Logları temizle
docker system prune -a
```

### Database Yönetimi
```bash
# PostgreSQL'e bağlan
docker-compose exec postgres psql -U eczane_user -d eczane_db

# Tabloları listele
\dt

# Çıkış
\q
```

### Redis Yönetimi
```bash
# Redis CLI
docker-compose exec redis redis-cli

# Auth
AUTH RedisSecure2024!

# Keys listele
KEYS *

# Çıkış
exit
```

## 15. Troubleshooting

### Uygulama başlamıyor
```bash
# Logları kontrol et
docker-compose logs app

# Database bağlantısını kontrol et
docker-compose exec app ping postgres

# Port çakışması var mı?
netstat -tulpn | grep 8080
```

### Database connection error
```bash
# PostgreSQL çalışıyor mu?
docker-compose ps postgres

# PostgreSQL logları
docker-compose logs postgres

# Database'e manuel bağlan
docker-compose exec postgres psql -U eczane_user -d eczane_db
```

### Memory issues
```bash
# Memory kullanımı
free -h

# Container resource kullanımı
docker stats
```

## 16. Monitoring

### Sistem Monitoring
```bash
# CPU ve Memory
htop

# Disk kullanımı
df -h

# Docker disk kullanımı
docker system df
```

### Application Monitoring
- Glowroot: http://185.136.206.32:4000
- Grafana: http://185.136.206.32:3000
- Actuator: http://185.136.206.32:8080/api/actuator

## 17. Backup

### Database Backup
```bash
# Backup oluştur
docker-compose exec postgres pg_dump -U eczane_user eczane_db > /opt/backups/eczane_$(date +%Y%m%d).sql

# Restore
docker-compose exec -T postgres psql -U eczane_user eczane_db < /opt/backups/eczane_20240212.sql
```

### Otomatik Backup (Cron)
```bash
# Crontab düzenle
crontab -e

# Her gün saat 02:00'de backup al
0 2 * * * docker-compose -f /opt/eczane/docker-compose.yml exec -T postgres pg_dump -U eczane_user eczane_db > /opt/backups/eczane_$(date +\%Y\%m\%d).sql
```

## 18. Güncelleme

### Kod Güncellemesi
```bash
cd /opt/eczane

# Son değişiklikleri çek
git pull origin main

# Container'ları yeniden build et
docker-compose build app

# Yeniden başlat
docker-compose up -d
```

## Notlar

1. **Güvenlik:** Production'da mutlaka güçlü şifreler kullanın
2. **SSL:** Let's Encrypt ile ücretsiz SSL sertifikası alın
3. **Monitoring:** Grafana ve Glowroot'u düzenli kontrol edin
4. **Backup:** Düzenli database backup'ları alın
5. **Logs:** Log rotation yapılandırın
6. **Updates:** Düzenli güvenlik güncellemeleri yapın

## Hızlı Başlangıç (Tek Komut)

Tüm adımları tek seferde yapmak için:

```bash
# Bu komutu kopyalayıp MobaXterm'e yapıştırın
curl -fsSL https://get.docker.com | sh && \
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
chmod +x /usr/local/bin/docker-compose && \
mkdir -p /opt/eczane && \
cd /opt/eczane && \
git clone https://github.com/CengizOzdemir0/eczane.git . && \
cat > .env << 'EOF'
DB_NAME=eczane_db
DB_USERNAME=eczane_user
DB_PASSWORD=EczaneSecure2024!
REDIS_PASSWORD=RedisSecure2024!
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
GRAFANA_USER=admin
GRAFANA_PASSWORD=GrafanaSecure2024!
SERVER_PORT=8080
EOF
chmod 600 .env && \
docker-compose up -d && \
echo "Deployment tamamlandı! Health check yapılıyor..." && \
sleep 30 && \
curl http://localhost:8080/api/actuator/health
```
