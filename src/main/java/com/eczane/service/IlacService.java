package com.eczane.service;

import com.eczane.entity.Ilac;
import com.eczane.entity.Kullanici;
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
public class IlacService {

    private final IlacRepository ilacRepository;
    private final KullaniciRepository kullaniciRepository;

    public List<Ilac> kullaniciIlaclariniGetir(String email) {
        return ilacRepository.findByKullaniciEmailAndIsActiveTrue(email);
    }

    @Transactional
    public Ilac ilacEkle(String email, Ilac ilac) {
        Kullanici kullanici = kullaniciRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        ilac.setKullanici(kullanici);
        log.info("İlaç ekleniyor: {} - Kullanıcı: {}", ilac.getAd(), email);
        return ilacRepository.save(ilac);
    }

    @Transactional
    public Ilac ilacGuncelle(String email, Long ilacId, Ilac ilac) {
        Ilac mevcutIlac = ilacRepository.findById(ilacId)
                .orElseThrow(() -> new RuntimeException("İlaç bulunamadı"));

        if (!mevcutIlac.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu ilaca erişim yetkiniz yok");
        }

        mevcutIlac.setAd(ilac.getAd());
        mevcutIlac.setDozaj(ilac.getDozaj());
        mevcutIlac.setKullanimTalimati(ilac.getKullanimTalimati());
        mevcutIlac.setYanEtkiler(ilac.getYanEtkiler());
        mevcutIlac.setBaslangicTarihi(ilac.getBaslangicTarihi());
        mevcutIlac.setBitisTarihi(ilac.getBitisTarihi());
        mevcutIlac.setNotlar(ilac.getNotlar());

        return ilacRepository.save(mevcutIlac);
    }

    @Transactional
    public void ilacSil(String email, Long ilacId) {
        Ilac ilac = ilacRepository.findById(ilacId)
                .orElseThrow(() -> new RuntimeException("İlaç bulunamadı"));

        if (!ilac.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu ilaca erişim yetkiniz yok");
        }

        ilac.setIsActive(false);
        ilacRepository.save(ilac);
        log.info("İlaç silindi: {} - Kullanıcı: {}", ilacId, email);
    }
}
