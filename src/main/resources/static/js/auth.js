// API Configuration
const API_BASE_URL = window.location.origin + '/api';

// Show/Hide forms
function showRegisterForm() {
    document.getElementById('loginForm').parentElement.style.display = 'none';
    document.getElementById('registerCard').style.display = 'block';
}

function showLoginForm() {
    document.getElementById('registerCard').style.display = 'none';
    document.getElementById('loginForm').parentElement.style.display = 'block';
}

// Login form handler
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                sifre: password
            })
        });

        const result = await response.json();

        if (result.success) {
            // Store tokens
            localStorage.setItem('accessToken', result.data.accessToken);
            localStorage.setItem('refreshToken', result.data.refreshToken);
            localStorage.setItem('user', JSON.stringify({
                id: result.data.kullaniciId,
                email: result.data.email,
                ad: result.data.ad,
                soyad: result.data.soyad,
                rol: result.data.rol
            }));

            // Redirect based on role
            if (result.data.rol === 'ADMIN') {
                window.location.href = 'admin.html';
            } else {
                window.location.href = 'dashboard.html';
            }
        } else {
            alert(result.message || 'Giriş başarısız');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('Bağlantı hatası. Lütfen tekrar deneyin.');
    }
});

// Register form handler
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = {
        ad: document.getElementById('regAd').value,
        soyad: document.getElementById('regSoyad').value,
        email: document.getElementById('regEmail').value,
        telefon: document.getElementById('regTelefon').value,
        sifre: document.getElementById('regPassword').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (result.success) {
            // Store tokens
            localStorage.setItem('accessToken', result.data.accessToken);
            localStorage.setItem('refreshToken', result.data.refreshToken);
            localStorage.setItem('user', JSON.stringify({
                id: result.data.kullaniciId,
                email: result.data.email,
                ad: result.data.ad,
                soyad: result.data.soyad,
                rol: result.data.rol
            }));

            // Redirect to dashboard
            window.location.href = 'dashboard.html';
        } else {
            alert(result.message || 'Kayıt başarısız');
        }
    } catch (error) {
        console.error('Register error:', error);
        alert('Bağlantı hatası. Lütfen tekrar deneyin.');
    }
});

// Check if already logged in
if (localStorage.getItem('accessToken')) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user.rol === 'ADMIN') {
        window.location.href = 'admin.html';
    } else {
        window.location.href = 'dashboard.html';
    }
}
