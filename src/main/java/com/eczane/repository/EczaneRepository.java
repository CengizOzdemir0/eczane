package com.eczane.repository;

import com.eczane.entity.Eczane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EczaneRepository extends JpaRepository<Eczane, Long> {

    List<Eczane> findByIsActiveTrue();

    @Query("SELECT e FROM Eczane e WHERE e.nobetci = true AND e.isActive = true " +
            "AND (e.nobetciBaslangic IS NULL OR e.nobetciBaslangic <= :tarih) " +
            "AND (e.nobetciBitis IS NULL OR e.nobetciBitis >= :tarih)")
    List<Eczane> findNobetciEczaneler(LocalDate tarih);

    List<Eczane> findByIlAndIsActiveTrue(String il);

    List<Eczane> findByIlAndIlceAndIsActiveTrue(String il, String ilce);

    @Query("SELECT e FROM Eczane e WHERE e.isActive = true " +
            "AND (6371 * acos(cos(radians(:lat)) * cos(radians(e.latitude)) * " +
            "cos(radians(e.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(e.latitude)))) <= :radiusKm " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(e.latitude)) * " +
            "cos(radians(e.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(e.latitude))))")
    List<Eczane> findNearbyEczaneler(Double lat, Double lon, Double radiusKm);
}
