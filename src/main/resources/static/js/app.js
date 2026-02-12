// API Configuration
const API_BASE_URL = window.location.origin + '/api';

// State
let map;
let markers = [];
let allPharmacies = [];
let currentFilter = 'all';
let userLocation = null;

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    initMap();
    loadPharmacies();
    setupEventListeners();
});

// Initialize Leaflet map
function initMap() {
    // Default center: Turkey
    map = L.map('map').setView([39.9334, 32.8597], 6);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
    }).addTo(map);

    // Try to get user location
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                userLocation = {
                    lat: position.coords.latitude,
                    lon: position.coords.longitude
                };
                map.setView([userLocation.lat, userLocation.lon], 13);
                
                // Add user location marker
                L.marker([userLocation.lat, userLocation.lon], {
                    icon: L.divIcon({
                        className: 'user-location-marker',
                        html: '<div style="background: #667eea; width: 20px; height: 20px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>',
                        iconSize: [20, 20]
                    })
                }).addTo(map).bindPopup('Konumunuz');
            },
            (error) => {
                console.error('Konum alınamadı:', error);
            }
        );
    }
}

// Load pharmacies from API
async function loadPharmacies() {
    try {
        showLoading(true);
        const response = await fetch(`${API_BASE_URL}/eczane/list`);
        const result = await response.json();
        
        if (result.success) {
            allPharmacies = result.data;
            displayPharmacies(allPharmacies);
            updateCount(allPharmacies.length);
        } else {
            showError('Eczaneler yüklenemedi');
        }
    } catch (error) {
        console.error('API Error:', error);
        showError('Bağlantı hatası');
    } finally {
        showLoading(false);
    }
}

// Load on-duty pharmacies
async function loadNobetciPharmacies() {
    try {
        showLoading(true);
        const response = await fetch(`${API_BASE_URL}/eczane/nobetci`);
        const result = await response.json();
        
        if (result.success) {
            displayPharmacies(result.data);
            updateCount(result.data.length);
        }
    } catch (error) {
        console.error('API Error:', error);
        showError('Nöbetçi eczaneler yüklenemedi');
    } finally {
        showLoading(false);
    }
}

// Load nearby pharmacies
async function loadNearbyPharmacies() {
    if (!userLocation) {
        alert('Konum bilgisi alınamadı. Lütfen konum erişimine izin verin.');
        return;
    }

    try {
        showLoading(true);
        const response = await fetch(
            `${API_BASE_URL}/eczane/nearby?lat=${userLocation.lat}&lon=${userLocation.lon}&radius=5`
        );
        const result = await response.json();
        
        if (result.success) {
            displayPharmacies(result.data);
            updateCount(result.data.length);
        }
    } catch (error) {
        console.error('API Error:', error);
        showError('Yakındaki eczaneler yüklenemedi');
    } finally {
        showLoading(false);
    }
}

// Display pharmacies on map and list
function displayPharmacies(pharmacies) {
    // Clear existing markers
    markers.forEach(marker => map.removeLayer(marker));
    markers = [];

    // Clear list
    const listContainer = document.getElementById('pharmacyList');
    listContainer.innerHTML = '';

    if (pharmacies.length === 0) {
        listContainer.innerHTML = '<div class="loading"><p>Eczane bulunamadı</p></div>';
        return;
    }

    // Add markers and list items
    pharmacies.forEach(pharmacy => {
        // Add marker
        const markerIcon = L.divIcon({
            className: 'custom-marker',
            html: `<div style="background: ${pharmacy.nobetci ? '#ef4444' : '#667eea'}; 
                   width: 32px; height: 32px; border-radius: 50%; 
                   border: 3px solid white; box-shadow: 0 2px 8px rgba(0,0,0,0.3);
                   display: flex; align-items: center; justify-content: center; color: white; font-weight: bold;">
                   ${pharmacy.nobetci ? '!' : '+'}</div>`,
            iconSize: [32, 32]
        });

        const marker = L.marker([pharmacy.latitude, pharmacy.longitude], { icon: markerIcon })
            .addTo(map)
            .bindPopup(createPopupContent(pharmacy));

        markers.push(marker);

        // Add to list
        const card = createPharmacyCard(pharmacy);
        listContainer.appendChild(card);

        // Click handler
        card.addEventListener('click', () => {
            map.setView([pharmacy.latitude, pharmacy.longitude], 15);
            marker.openPopup();
        });
    });
}

// Create pharmacy card
function createPharmacyCard(pharmacy) {
    const card = document.createElement('div');
    card.className = `pharmacy-card ${pharmacy.nobetci ? 'nobetci' : ''}`;
    
    card.innerHTML = `
        <div class="pharmacy-header">
            <div class="pharmacy-name">${pharmacy.ad}</div>
            ${pharmacy.nobetci ? '<span class="nobetci-badge">NÖBETÇİ</span>' : ''}
        </div>
        <div class="pharmacy-info">
            <div class="info-row">
                <svg width="16" height="16" fill="currentColor">
                    <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
                </svg>
                <span>${pharmacy.adres}</span>
            </div>
            <div class="info-row">
                <svg width="16" height="16" fill="currentColor">
                    <path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/>
                </svg>
                <span>${pharmacy.telefon}</span>
            </div>
            ${pharmacy.calismaSaatleri ? `
                <div class="info-row">
                    <svg width="16" height="16" fill="currentColor">
                        <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"/>
                    </svg>
                    <span>${pharmacy.calismaSaatleri}</span>
                </div>
            ` : ''}
        </div>
    `;
    
    return card;
}

// Create popup content
function createPopupContent(pharmacy) {
    return `
        <div style="min-width: 200px;">
            <h3 style="margin: 0 0 0.5rem 0; font-size: 1.1rem;">${pharmacy.ad}</h3>
            ${pharmacy.nobetci ? '<span style="background: #ef4444; color: white; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: bold;">NÖBETÇİ</span>' : ''}
            <p style="margin: 0.5rem 0; color: #6b7280; font-size: 0.9rem;">${pharmacy.adres}</p>
            <p style="margin: 0.25rem 0; color: #6b7280; font-size: 0.9rem;">📞 ${pharmacy.telefon}</p>
            ${pharmacy.calismaSaatleri ? `<p style="margin: 0.25rem 0; color: #6b7280; font-size: 0.9rem;">🕐 ${pharmacy.calismaSaatleri}</p>` : ''}
        </div>
    `;
}

// Setup event listeners
function setupEventListeners() {
    // Search
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase();
        const filtered = allPharmacies.filter(p => 
            p.ad.toLowerCase().includes(query) ||
            p.adres.toLowerCase().includes(query)
        );
        displayPharmacies(filtered);
        updateCount(filtered.length);
    });

    // Filter buttons
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            const filter = btn.dataset.filter;
            if (filter === 'all') {
                displayPharmacies(allPharmacies);
                updateCount(allPharmacies.length);
            } else if (filter === 'nobetci') {
                loadNobetciPharmacies();
            }
        });
    });

    // Nearby button
    document.getElementById('nearbyBtn').addEventListener('click', () => {
        loadNearbyPharmacies();
    });

    // Locate button
    document.getElementById('locateBtn').addEventListener('click', () => {
        if (userLocation) {
            map.setView([userLocation.lat, userLocation.lon], 13);
        } else {
            alert('Konum bilgisi alınamadı');
        }
    });
}

// Helper functions
function showLoading(show) {
    const listContainer = document.getElementById('pharmacyList');
    if (show) {
        listContainer.innerHTML = `
            <div class="loading">
                <div class="spinner"></div>
                <p>Yükleniyor...</p>
            </div>
        `;
    }
}

function showError(message) {
    const listContainer = document.getElementById('pharmacyList');
    listContainer.innerHTML = `
        <div class="loading">
            <p style="color: #ef4444;">${message}</p>
        </div>
    `;
}

function updateCount(count) {
    document.getElementById('countBadge').textContent = count;
}
