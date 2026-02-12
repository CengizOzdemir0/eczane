package com.eczane.repository;

import com.eczane.entity.Duyuru;
import com.eczane.enums.DuyuruTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DuyuruRepository extends JpaRepository<Duyuru, Long> {

    @Query("SELECT d FROM Duyuru d WHERE d.isActive = true " +
            "AND (d.yayinlanmaTarihi IS NULL OR d.yayinlanmaTarihi <= :simdi) " +
            "AND (d.bitisTarihi IS NULL OR d.bitisTarihi >= :simdi) " +
            "ORDER BY d.onemli DESC, d.yayinlanmaTarihi DESC")
    List<Duyuru> findAktifDuyurular(LocalDateTime simdi);

    List<Duyuru> findByTipAndIsActiveTrueOrderByYayinlanmaTarihiDesc(DuyuruTip tip);

    List<Duyuru> findByOnemliTrueAndIsActiveTrueOrderByYayinlanmaTarihiDesc();
}
