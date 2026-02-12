## Sunucuda Çalıştırılacak Komutlar

Dockerfile düzeltildi. Şimdi sunucuda şu komutları çalıştırın:

### 1. Projeyi Güncelle
```bash
cd /opt/eczane
git pull origin main
```

### 2. Docker Build ve Başlat
```bash
# Eski container'ları temizle
docker-compose down

# Yeniden build et ve başlat
docker-compose up -d --build

# Logları izle
docker-compose logs -f app
```

### 3. Health Check (30 saniye sonra)
```bash
# 30 saniye bekleyin, sonra:
curl http://localhost:8080/api/actuator/health
```

Beklenen çıktı: `{"status":"UP"}`

### Sorun Yaşarsanız

#### Container durumunu kontrol edin:
```bash
docker-compose ps
```

#### Logları kontrol edin:
```bash
# Tüm loglar
docker-compose logs

# Sadece app logları
docker-compose logs app

# Canlı log izleme
docker-compose logs -f app
```

#### Database bağlantısını test edin:
```bash
docker-compose exec postgres psql -U eczane_user -d eczane_db -c "SELECT version();"
```

#### Redis bağlantısını test edin:
```bash
docker-compose exec redis redis-cli -a RedisSecure2024! ping
```

### Erişim Bilgileri

Deployment başarılı olduğunda:

- **Ana Uygulama:** http://185.136.206.32:8080
- **API Health:** http://185.136.206.32:8080/api/actuator/health
- **Eczane Listesi:** http://185.136.206.32:8080/api/eczane/list
- **Grafana:** http://185.136.206.32:3000
- **Glowroot:** http://185.136.206.32:4000

### Faydalı Komutlar

```bash
# Container'ları yeniden başlat
docker-compose restart

# Sadece app'i yeniden başlat
docker-compose restart app

# Container'ları durdur
docker-compose down

# Disk alanını temizle
docker system prune -a

# Memory kullanımını kontrol et
docker stats
```
