package com.eczane.controller;

import com.eczane.dto.ApiResponse;
import com.eczane.entity.Hatirlatici;
import com.eczane.service.HatirlaticiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hatirlatici")
@RequiredArgsConstructor
public class HatirlaticiController {

    private final HatirlaticiService hatirlaticiService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Hatirlatici>>> hatirlaticilarim(Authentication authentication) {
        String email = authentication.getName();
        List<Hatirlatici> hatirlaticilar = hatirlaticiService.kullaniciHatirlaticilariGetir(email);
        return ResponseEntity.ok(ApiResponse.success(hatirlaticilar));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Hatirlatici>> hatirlaticiEkle(
            @RequestBody Hatirlatici hatirlatici,
            @RequestParam Long ilacId,
            Authentication authentication) {
        String email = authentication.getName();
        Hatirlatici yeniHatirlatici = hatirlaticiService.hatirlaticiEkle(email, hatirlatici, ilacId);
        return ResponseEntity.ok(ApiResponse.success("Hatırlatıcı eklendi", yeniHatirlatici));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Hatirlatici>> hatirlaticiGuncelle(
            @PathVariable Long id,
            @RequestBody Hatirlatici hatirlatici,
            Authentication authentication) {
        String email = authentication.getName();
        Hatirlatici guncelHatirlatici = hatirlaticiService.hatirlaticiGuncelle(email, id, hatirlatici);
        return ResponseEntity.ok(ApiResponse.success("Hatırlatıcı güncellendi", guncelHatirlatici));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hatirlaticiSil(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        hatirlaticiService.hatirlaticiSil(email, id);
        return ResponseEntity.ok(ApiResponse.success("Hatırlatıcı silindi", null));
    }
}
