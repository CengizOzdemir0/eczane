package com.eczane.service;

import com.eczane.entity.Eczane;
import com.eczane.repository.EczaneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EczaneService {

    private final EczaneRepository eczaneRepository;

    @Cacheable(value = "eczaneler", key = "'all'")
    public List<Eczane> tumEczaneleriGetir() {
        log.info("Tüm eczaneler getiriliyor");
        return eczaneRepository.findByIsActiveTrue();
    }

    @Cacheable(value = "nobetciEczaneler", key = "#tarih")
    public List<Eczane> nobetciEczaneleriGetir(LocalDate tarih) {
        log.info("Nöbetçi eczaneler getiriliyor: {}", tarih);
        return eczaneRepository.findNobetciEczaneler(tarih);
    }

    public List<Eczane> yakinEczaneleriGetir(Double lat, Double lon, Double radiusKm) {
        log.info("Yakın eczaneler getiriliyor: lat={}, lon={}, radius={}km", lat, lon, radiusKm);
        return eczaneRepository.findNearbyEczaneler(lat, lon, radiusKm);
    }

    public List<Eczane> ilEczaneleriGetir(String il) {
        return eczaneRepository.findByIlAndIsActiveTrue(il);
    }

    public List<Eczane> ilceEczaneleriGetir(String il, String ilce) {
        return eczaneRepository.findByIlAndIlceAndIsActiveTrue(il, ilce);
    }

    public Eczane eczaneGetir(Long id) {
        return eczaneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Eczane bulunamadı: " + id));
    }

    @Transactional
    public Eczane eczaneKaydet(Eczane eczane) {
        log.info("Eczane kaydediliyor: {}", eczane.getAd());
        return eczaneRepository.save(eczane);
    }

    @Transactional
    public Eczane eczaneGuncelle(Long id, Eczane eczane) {
        Eczane mevcutEczane = eczaneGetir(id);

        mevcutEczane.setAd(eczane.getAd());
        mevcutEczane.setAdres(eczane.getAdres());
        mevcutEczane.setTelefon(eczane.getTelefon());
        mevcutEczane.setEmail(eczane.getEmail());
        mevcutEczane.setLatitude(eczane.getLatitude());
        mevcutEczane.setLongitude(eczane.getLongitude());
        mevcutEczane.setIl(eczane.getIl());
        mevcutEczane.setIlce(eczane.getIlce());
        mevcutEczane.setNobetci(eczane.getNobetci());
        mevcutEczane.setNobetciBaslangic(eczane.getNobetciBaslangic());
        mevcutEczane.setNobetciBitis(eczane.getNobetciBitis());
        mevcutEczane.setAciklama(eczane.getAciklama());
        mevcutEczane.setCalismaSaatleri(eczane.getCalismaSaatleri());

        return eczaneRepository.save(mevcutEczane);
    }

    @Transactional
    public void eczaneSil(Long id) {
        Eczane eczane = eczaneGetir(id);
        eczane.setIsActive(false);
        eczaneRepository.save(eczane);
        log.info("Eczane silindi (soft delete): {}", id);
    }
}
