package com.eczane.service;

import com.eczane.entity.Hatirlatici;
import com.eczane.entity.Ilac;
import com.eczane.entity.Kullanici;
import com.eczane.repository.HatirlaticiRepository;
import com.eczane.repository.IlacRepository;
import com.eczane.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HatirlaticiService {

    private final HatirlaticiRepository hatirlaticiRepository;
    private final IlacRepository ilacRepository;
    private final KullaniciRepository kullaniciRepository;

    public List<Hatirlatici> kullaniciHatirlaticilariGetir(String email) {
        return hatirlaticiRepository.findByKullaniciEmailAndIsActiveTrue(email);
    }

    @Transactional
    public Hatirlatici hatirlaticiEkle(String email, Hatirlatici hatirlatici, Long ilacId) {
        Kullanici kullanici = kullaniciRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Ilac ilac = ilacRepository.findById(ilacId)
                .orElseThrow(() -> new RuntimeException("İlaç bulunamadı"));

        if (!ilac.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu ilaca erişim yetkiniz yok");
        }

        hatirlatici.setKullanici(kullanici);
        hatirlatici.setIlac(ilac);

        log.info("Hatırlatıcı ekleniyor: İlaç={}, Kullanıcı={}", ilac.getAd(), email);
        return hatirlaticiRepository.save(hatirlatici);
    }

    @Transactional
    public Hatirlatici hatirlaticiGuncelle(String email, Long hatirlaticiId, Hatirlatici hatirlatici) {
        Hatirlatici mevcutHatirlatici = hatirlaticiRepository.findById(hatirlaticiId)
                .orElseThrow(() -> new RuntimeException("Hatırlatıcı bulunamadı"));

        if (!mevcutHatirlatici.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu hatırlatıcıya erişim yetkiniz yok");
        }

        mevcutHatirlatici.setZaman(hatirlatici.getZaman());
        mevcutHatirlatici.setSiklik(hatirlatici.getSiklik());
        mevcutHatirlatici.setAktif(hatirlatici.getAktif());
        mevcutHatirlatici.setMesaj(hatirlatici.getMesaj());

        return hatirlaticiRepository.save(mevcutHatirlatici);
    }

    @Transactional
    public void hatirlaticiSil(String email, Long hatirlaticiId) {
        Hatirlatici hatirlatici = hatirlaticiRepository.findById(hatirlaticiId)
                .orElseThrow(() -> new RuntimeException("Hatırlatıcı bulunamadı"));

        if (!hatirlatici.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu hatırlatıcıya erişim yetkiniz yok");
        }

        hatirlatici.setIsActive(false);
        hatirlaticiRepository.save(hatirlatici);
        log.info("Hatırlatıcı silindi: {} - Kullanıcı: {}", hatirlaticiId, email);
    }
}
