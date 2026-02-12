package com.eczane.repository;

import com.eczane.entity.Hatirlatici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HatirlaticiRepository extends JpaRepository<Hatirlatici, Long> {

    List<Hatirlatici> findByKullaniciIdAndIsActiveTrue(Long kullaniciId);

    List<Hatirlatici> findByKullaniciEmailAndIsActiveTrue(String email);

    @Query("SELECT h FROM Hatirlatici h WHERE h.aktif = true AND h.isActive = true " +
            "AND h.zaman BETWEEN :baslangic AND :bitis")
    List<Hatirlatici> findAktifHatirlaticilarByZamanAraligi(LocalTime baslangic, LocalTime bitis);
}
