package com.eczane.service;

import com.eczane.entity.Kullanici;
import com.eczane.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public Kullanici kullaniciGetir(String email) {
        return kullaniciRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    @Transactional
    public Kullanici profilGuncelle(String email, Kullanici kullanici) {
        Kullanici mevcutKullanici = kullaniciGetir(email);

        mevcutKullanici.setAd(kullanici.getAd());
        mevcutKullanici.setSoyad(kullanici.getSoyad());
        mevcutKullanici.setTelefon(kullanici.getTelefon());

        log.info("Profil güncellendi: {}", email);
        return kullaniciRepository.save(mevcutKullanici);
    }

    @Transactional
    public void sifreDegistir(String email, String eskiSifre, String yeniSifre) {
        Kullanici kullanici = kullaniciGetir(email);

        if (!passwordEncoder.matches(eskiSifre, kullanici.getSifre())) {
            throw new RuntimeException("Eski şifre yanlış");
        }

        kullanici.setSifre(passwordEncoder.encode(yeniSifre));
        kullaniciRepository.save(kullanici);
        log.info("Şifre değiştirildi: {}", email);
    }
}
