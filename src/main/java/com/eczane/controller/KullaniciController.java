package com.eczane.controller;

import com.eczane.dto.ApiResponse;
import com.eczane.entity.Kullanici;
import com.eczane.service.KullaniciService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kullanici")
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciService kullaniciService;

    @GetMapping("/profil")
    public ResponseEntity<ApiResponse<Kullanici>> profil(Authentication authentication) {
        String email = authentication.getName();
        Kullanici kullanici = kullaniciService.kullaniciGetir(email);
        return ResponseEntity.ok(ApiResponse.success(kullanici));
    }

    @PutMapping("/profil")
    public ResponseEntity<ApiResponse<Kullanici>> profilGuncelle(
            @RequestBody Kullanici kullanici,
            Authentication authentication) {
        String email = authentication.getName();
        Kullanici guncelKullanici = kullaniciService.profilGuncelle(email, kullanici);
        return ResponseEntity.ok(ApiResponse.success("Profil güncellendi", guncelKullanici));
    }

    @PostMapping("/sifre-degistir")
    public ResponseEntity<ApiResponse<Void>> sifreDegistir(
            @RequestBody SifreDegistirRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        kullaniciService.sifreDegistir(email, request.getEskiSifre(), request.getYeniSifre());
        return ResponseEntity.ok(ApiResponse.success("Şifre değiştirildi", null));
    }

    @Data
    public static class SifreDegistirRequest {
        private String eskiSifre;
        private String yeniSifre;
    }
}
