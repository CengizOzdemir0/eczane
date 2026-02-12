# Eczane Uygulaması

Modern ve kapsamlı bir eczane yönetim sistemi. Hem açık erişim hem de kullanıcı girişli modda çalışır.

## 🚀 Özellikler

### Açık Erişim
- 🗺️ Harita üzerinde eczane görüntüleme (Leaflet.js)
- 🔴 Nöbetçi eczaneleri görüntüleme
- 📍 Konuma göre yakındaki eczaneleri bulma
- 🔍 Eczane arama ve filtreleme

### Kullanıcı Paneli
- 💊 İlaç listesi yönetimi
- ⏰ İlaç hatırlatıcıları
- 📢 Duyurular
- 👤 Profil yönetimi
- 📧 İletişim bilgilerini güncelleme

### Admin Paneli
- 🏥 Eczane CRUD işlemleri
- 📝 Duyuru yönetimi
- 👥 Kullanıcı yönetimi

## 🛠️ Teknolojiler

### Backend
- Java 17
- Spring Boot 3.2.2
- Maven
- PostgreSQL (Ana veri)
- Redis (Session/Cache)
- JWT Authentication
- Spring Security

### Frontend
- HTML5, CSS3, JavaScript
- Leaflet.js (Harita)
- Modern responsive tasarım
- Glassmorphism efektleri

### DevOps & Monitoring
- Docker & Docker Compose
- Glowroot (APM)
- Grafana (Monitoring)
- Jenkins (CI/CD)
- Nginx (Reverse Proxy)

## 📦 Kurulum

### Gereksinimler
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

### Yerel Geliştirme

1. **Projeyi klonlayın:**
```bash
git clone https://github.com/CengizOzdemir0/eczane.git
cd eczane
```

2. **Environment dosyasını oluşturun:**
```bash
cp .env.example .env
# .env dosyasını düzenleyin ve gerekli değerleri girin
```

3. **Docker Compose ile başlatın:**
```bash
docker-compose up -d
```

4. **Uygulamaya erişin:**
- Ana uygulama: http://localhost:8080
- Grafana: http://localhost:3000
- Glowroot: http://localhost:4000

### Production Deployment

1. **Sunucuyu hazırlayın:**
```bash
chmod +x scripts/setup-server.sh
sudo ./scripts/setup-server.sh
```

2. **Jenkins'i yapılandırın:**
- Jenkins'e erişin: http://185.136.206.32:8080
- GitHub credentials ekleyin
- SSH credentials ekleyin (server-ssh-credentials)
- Pipeline job oluşturun

3. **Deploy edin:**
```bash
# Jenkins pipeline otomatik deploy yapar
# veya manuel:
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

## 🔧 Yapılandırma

### Database
PostgreSQL veritabanı otomatik olarak oluşturulur. Tablo yapıları JPA tarafından yönetilir.

### Redis
Session ve cache yönetimi için kullanılır.

### JWT
`.env` dosyasında JWT secret key'i ayarlayın:
```
JWT_SECRET=your_base64_encoded_secret_here
```

### SSL/TLS
Nginx için SSL sertifikalarını `nginx/ssl/` dizinine ekleyin.

## 📚 API Dokümantasyonu

### Authentication
- `POST /api/auth/register` - Yeni kullanıcı kaydı
- `POST /api/auth/login` - Kullanıcı girişi
- `POST /api/auth/refresh` - Token yenileme

### Eczane
- `GET /api/eczane/list` - Tüm eczaneler
- `GET /api/eczane/nobetci` - Nöbetçi eczaneler
- `GET /api/eczane/nearby?lat={lat}&lon={lon}&radius={km}` - Yakındaki eczaneler
- `GET /api/eczane/{id}` - Eczane detayı
- `POST /api/eczane` - Eczane ekle (Admin)
- `PUT /api/eczane/{id}` - Eczane güncelle (Admin)
- `DELETE /api/eczane/{id}` - Eczane sil (Admin)

### İlaç (Authentication Required)
- `GET /api/ilac` - Kullanıcının ilaçları
- `POST /api/ilac` - İlaç ekle
- `PUT /api/ilac/{id}` - İlaç güncelle
- `DELETE /api/ilac/{id}` - İlaç sil

### Hatırlatıcı (Authentication Required)
- `GET /api/hatirlatici` - Kullanıcının hatırlatıcıları
- `POST /api/hatirlatici?ilacId={id}` - Hatırlatıcı ekle
- `PUT /api/hatirlatici/{id}` - Hatırlatıcı güncelle
- `DELETE /api/hatirlatici/{id}` - Hatırlatıcı sil

### Duyuru
- `GET /api/duyuru` - Aktif duyurular
- `GET /api/duyuru/onemli` - Önemli duyurular
- `POST /api/duyuru` - Duyuru oluştur (Admin)
- `PUT /api/duyuru/{id}` - Duyuru güncelle (Admin)
- `DELETE /api/duyuru/{id}` - Duyuru sil (Admin)

## 🔍 Monitoring

### Glowroot
APM ve performance monitoring için:
- URL: http://cengizozdemir.duckdns.org/glowroot
- Request tracking
- Transaction profiling
- Error tracking

### Grafana
Metrics ve dashboards için:
- URL: http://cengizozdemir.duckdns.org/grafana
- Default credentials: admin/admin (değiştirin!)

## 🧪 Test

```bash
# Unit testleri çalıştır
mvn test

# Integration testleri çalıştır
mvn verify
```

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 👥 Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit edin (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

## 📧 İletişim

Cengiz Özdemir - [@CengizOzdemir0](https://github.com/CengizOzdemir0)

Proje Linki: [https://github.com/CengizOzdemir0/eczane](https://github.com/CengizOzdemir0/eczane)
