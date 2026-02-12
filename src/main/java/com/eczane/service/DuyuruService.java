package com.eczane.service;

import com.eczane.entity.Duyuru;
import com.eczane.entity.Kullanici;
import com.eczane.repository.DuyuruRepository;
import com.eczane.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuyuruService {

    private final DuyuruRepository duyuruRepository;
    private final KullaniciRepository kullaniciRepository;

    public List<Duyuru> aktifDuyurulariGetir() {
        return duyuruRepository.findAktifDuyurular(LocalDateTime.now());
    }

    public List<Duyuru> onemliDuyurulariGetir() {
        return duyuruRepository.findByOnemliTrueAndIsActiveTrueOrderByYayinlanmaTarihiDesc();
    }

    @Transactional
    public Duyuru duyuruOlustur(String email, Duyuru duyuru) {
        Kullanici kullanici = kullaniciRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        duyuru.setOlusturan(kullanici);
        if (duyuru.getYayinlanmaTarihi() == null) {
            duyuru.setYayinlanmaTarihi(LocalDateTime.now());
        }

        log.info("Duyuru oluşturuluyor: {} - Oluşturan: {}", duyuru.getBaslik(), email);
        return duyuruRepository.save(duyuru);
    }

    @Transactional
    public Duyuru duyuruGuncelle(Long duyuruId, Duyuru duyuru) {
        Duyuru mevcutDuyuru = duyuruRepository.findById(duyuruId)
                .orElseThrow(() -> new RuntimeException("Duyuru bulunamadı"));

        mevcutDuyuru.setBaslik(duyuru.getBaslik());
        mevcutDuyuru.setIcerik(duyuru.getIcerik());
        mevcutDuyuru.setTip(duyuru.getTip());
        mevcutDuyuru.setOnemli(duyuru.getOnemli());
        mevcutDuyuru.setBitisTarihi(duyuru.getBitisTarihi());

        return duyuruRepository.save(mevcutDuyuru);
    }

    @Transactional
    public void duyuruSil(Long duyuruId) {
        Duyuru duyuru = duyuruRepository.findById(duyuruId)
                .orElseThrow(() -> new RuntimeException("Duyuru bulunamadı"));

        duyuru.setIsActive(false);
        duyuruRepository.save(duyuru);
        log.info("Duyuru silindi: {}", duyuruId);
    }
}
