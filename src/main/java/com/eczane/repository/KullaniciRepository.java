package com.eczane.repository;

import com.eczane.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {

    Optional<Kullanici> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<Kullanici> findByEmailAndIsActiveTrue(String email);
}
