package com.eczane.service;

import com.eczane.dto.AuthRequest;
import com.eczane.dto.AuthResponse;
import com.eczane.dto.RegisterRequest;
import com.eczane.entity.Kullanici;
import com.eczane.enums.Role;
import com.eczane.repository.KullaniciRepository;
import com.eczane.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Yeni kullanıcı kaydı: {}", request.getEmail());

        if (kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }

        Kullanici kullanici = Kullanici.builder()
                .ad(request.getAd())
                .soyad(request.getSoyad())
                .email(request.getEmail())
                .sifre(passwordEncoder.encode(request.getSifre()))
                .telefon(request.getTelefon())
                .rol(request.getRol() != null ? request.getRol() : Role.USER)
                .build();

        kullanici = kullaniciRepository.save(kullanici);

        UserDetails userDetails = userDetailsService.loadUserByUsername(kullanici.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .kullaniciId(kullanici.getId())
                .email(kullanici.getEmail())
                .ad(kullanici.getAd())
                .soyad(kullanici.getSoyad())
                .rol(kullanici.getRol().name())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Kullanıcı girişi: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSifre()));

        Kullanici kullanici = kullaniciRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(kullanici.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .kullaniciId(kullanici.getId())
                .email(kullanici.getEmail())
                .ad(kullanici.getAd())
                .soyad(kullanici.getSoyad())
                .rol(kullanici.getRol().name())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String userEmail = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Geçersiz refresh token");
        }

        Kullanici kullanici = kullaniciRepository.findByEmailAndIsActiveTrue(userEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .kullaniciId(kullanici.getId())
                .email(kullanici.getEmail())
                .ad(kullanici.getAd())
                .soyad(kullanici.getSoyad())
                .rol(kullanici.getRol().name())
                .build();
    }
}
