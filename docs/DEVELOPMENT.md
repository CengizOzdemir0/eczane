# Eczane Uygulaması - Geliştirme

## Proje Yapısı

```
eczane/
├── src/
│   ├── main/
│   │   ├── java/com/eczane/
│   │   │   ├── config/          # Yapılandırma sınıfları
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── enums/           # Enum sınıfları
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── security/        # Security & JWT
│   │   │   └── service/         # Business Logic
│   │   └── resources/
│   │       ├── static/          # Frontend dosyaları
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   ├── index.html
│   │       │   └── login.html
│   │       ├── application.yml
│   │       └── application-prod.yml
│   └── test/                    # Test dosyaları
├── docker-compose.yml
├── Dockerfile
├── Jenkinsfile
├── nginx/
│   └── nginx.conf
├── grafana/
│   └── provisioning/
├── glowroot/
│   └── glowroot.properties
└── scripts/
    ├── setup-server.sh
    └── deploy.sh
```

## Veritabanı Şeması

### Kullanici
- id (PK)
- ad, soyad, email, sifre
- telefon, rol, profil_resmi
- created_at, updated_at, is_active

### Eczane
- id (PK)
- ad, adres, telefon, email
- latitude, longitude
- il, ilce
- nobetci, nobetci_baslangic, nobetci_bitis
- aciklama, calisma_saatleri
- created_at, updated_at, is_active

### Ilac
- id (PK)
- ad, dozaj, kullanim_talimati, yan_etkiler
- baslangic_tarihi, bitis_tarihi, notlar
- kullanici_id (FK)
- created_at, updated_at, is_active

### Hatirlatici
- id (PK)
- ilac_id (FK), kullanici_id (FK)
- zaman, siklik, aktif
- mesaj, son_gonderim
- created_at, updated_at, is_active

### Duyuru
- id (PK)
- baslik, icerik, tip
- yayinlanma_tarihi, bitis_tarihi
- onemli, olusturan_id (FK)
- created_at, updated_at, is_active

## Geliştirme Notları

### Backend
- Spring Boot 3.2.2 kullanılıyor
- Java 17 gerekli
- Lombok ve MapStruct annotation processing kullanılıyor
- JPA Auditing aktif (@CreatedDate, @LastModifiedDate)
- Soft delete pattern kullanılıyor (is_active)

### Security
- JWT token authentication
- Role-based access control (USER, ADMIN)
- CORS yapılandırılmış
- Password encryption (BCrypt)

### Caching
- Redis kullanılıyor
- Eczane listesi cache'leniyor
- Session yönetimi Redis'te

### API Design
- RESTful principles
- Consistent response format (ApiResponse<T>)
- Proper HTTP status codes
- Error handling

## Deployment Checklist

### Sunucu Hazırlığı
- [ ] Docker ve Docker Compose kurulu
- [ ] Jenkins kurulu ve yapılandırılmış
- [ ] Firewall kuralları ayarlanmış (80, 443, 8080)
- [ ] Domain DNS ayarları yapılmış

### Uygulama Yapılandırması
- [ ] .env dosyası oluşturulmuş
- [ ] JWT secret key ayarlanmış
- [ ] Database credentials ayarlanmış
- [ ] Redis password ayarlanmış
- [ ] SSL sertifikaları eklendi

### Jenkins Yapılandırması
- [ ] GitHub credentials eklendi
- [ ] SSH credentials eklendi
- [ ] Pipeline job oluşturuldu
- [ ] Webhook yapılandırıldı (opsiyonel)

### İlk Deploy
1. Sunucuda `/opt/eczane` dizini oluştur
2. `.env` dosyasını kopyala
3. Jenkins pipeline'ı çalıştır
4. Health check yap
5. Grafana ve Glowroot'a erişimi kontrol et

## Monitoring

### Glowroot
- Transaction profiling
- SQL query analysis
- Error tracking
- JVM metrics

### Grafana
- Application metrics
- Database metrics
- System metrics
- Custom dashboards

### Logs
- Application logs: `/var/log/eczane-app/`
- Docker logs: `docker-compose logs -f app`
- Nginx logs: Container içinde `/var/log/nginx/`

## Troubleshooting

### Uygulama başlamıyor
1. Database bağlantısını kontrol et
2. Redis bağlantısını kontrol et
3. Port çakışması var mı kontrol et
4. Logs'u incele: `docker-compose logs app`

### Database connection error
1. PostgreSQL container'ı çalışıyor mu?
2. Credentials doğru mu?
3. Network bağlantısı var mı?

### JWT errors
1. JWT secret key ayarlandı mı?
2. Token expire olmamış mı?
3. Token format doğru mu?

## Performans Optimizasyonu

### Database
- Index'ler eklendi (email, coordinates)
- Connection pooling yapılandırıldı
- Query optimization

### Caching
- Redis ile cache
- HTTP response compression
- Static asset caching

### Frontend
- Minification
- Lazy loading
- Image optimization

## Güvenlik

### Best Practices
- Environment variables kullanımı
- Password encryption
- SQL injection koruması (JPA)
- XSS koruması
- CSRF koruması
- Rate limiting (Nginx)
- Security headers

### Production Checklist
- [ ] Default passwords değiştirildi
- [ ] SSL/TLS aktif
- [ ] Firewall yapılandırıldı
- [ ] Database backups ayarlandı
- [ ] Log rotation yapılandırıldı
- [ ] Monitoring aktif
