package com.eczane.controller;

import com.eczane.dto.ApiResponse;
import com.eczane.entity.Duyuru;
import com.eczane.service.DuyuruService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/duyuru")
@RequiredArgsConstructor
public class DuyuruController {

    private final DuyuruService duyuruService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Duyuru>>> aktifDuyurular() {
        List<Duyuru> duyurular = duyuruService.aktifDuyurulariGetir();
        return ResponseEntity.ok(ApiResponse.success(duyurular));
    }

    @GetMapping("/onemli")
    public ResponseEntity<ApiResponse<List<Duyuru>>> onemliDuyurular() {
        List<Duyuru> duyurular = duyuruService.onemliDuyurulariGetir();
        return ResponseEntity.ok(ApiResponse.success(duyurular));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Duyuru>> duyuruOlustur(
            @RequestBody Duyuru duyuru,
            Authentication authentication) {
        String email = authentication.getName();
        Duyuru yeniDuyuru = duyuruService.duyuruOlustur(email, duyuru);
        return ResponseEntity.ok(ApiResponse.success("Duyuru oluşturuldu", yeniDuyuru));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Duyuru>> duyuruGuncelle(
            @PathVariable Long id,
            @RequestBody Duyuru duyuru) {
        Duyuru guncelDuyuru = duyuruService.duyuruGuncelle(id, duyuru);
        return ResponseEntity.ok(ApiResponse.success("Duyuru güncellendi", guncelDuyuru));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> duyuruSil(@PathVariable Long id) {
        duyuruService.duyuruSil(id);
        return ResponseEntity.ok(ApiResponse.success("Duyuru silindi", null));
    }
}
