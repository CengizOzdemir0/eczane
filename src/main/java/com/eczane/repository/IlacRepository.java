package com.eczane.repository;

import com.eczane.entity.Ilac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IlacRepository extends JpaRepository<Ilac, Long> {

    List<Ilac> findByKullaniciIdAndIsActiveTrue(Long kullaniciId);

    List<Ilac> findByKullaniciEmailAndIsActiveTrue(String email);
}
