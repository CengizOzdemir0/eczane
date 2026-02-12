package com.eczane.controller;

import com.eczane.dto.ApiResponse;
import com.eczane.entity.Ilac;
import com.eczane.service.IlacService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ilac")
@RequiredArgsConstructor
public class IlacController {

    private final IlacService ilacService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ilac>>> ilaclarim(Authentication authentication) {
        String email = authentication.getName();
        List<Ilac> ilaclar = ilacService.kullaniciIlaclariniGetir(email);
        return ResponseEntity.ok(ApiResponse.success(ilaclar));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Ilac>> ilacEkle(
            @RequestBody Ilac ilac,
            Authentication authentication) {
        String email = authentication.getName();
        Ilac yeniIlac = ilacService.ilacEkle(email, ilac);
        return ResponseEntity.ok(ApiResponse.success("İlaç eklendi", yeniIlac));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ilac>> ilacGuncelle(
            @PathVariable Long id,
            @RequestBody Ilac ilac,
            Authentication authentication) {
        String email = authentication.getName();
        Ilac guncelIlac = ilacService.ilacGuncelle(email, id, ilac);
        return ResponseEntity.ok(ApiResponse.success("İlaç güncellendi", guncelIlac));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> ilacSil(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        ilacService.ilacSil(email, id);
        return ResponseEntity.ok(ApiResponse.success("İlaç silindi", null));
    }
}
