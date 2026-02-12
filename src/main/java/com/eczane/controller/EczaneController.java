package com.eczane.controller;

import com.eczane.dto.ApiResponse;
import com.eczane.entity.Eczane;
import com.eczane.service.EczaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eczane")
@RequiredArgsConstructor
public class EczaneController {

    private final EczaneService eczaneService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Eczane>>> tumEczaneler() {
        List<Eczane> eczaneler = eczaneService.tumEczaneleriGetir();
        return ResponseEntity.ok(ApiResponse.success(eczaneler));
    }

    @GetMapping("/nobetci")
    public ResponseEntity<ApiResponse<List<Eczane>>> nobetciEczaneler(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tarih) {
        LocalDate searchDate = tarih != null ? tarih : LocalDate.now();
        List<Eczane> eczaneler = eczaneService.nobetciEczaneleriGetir(searchDate);
        return ResponseEntity.ok(ApiResponse.success(eczaneler));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<Eczane>>> yakinEczaneler(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "5.0") Double radius) {
        List<Eczane> eczaneler = eczaneService.yakinEczaneleriGetir(lat, lon, radius);
        return ResponseEntity.ok(ApiResponse.success(eczaneler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Eczane>> eczaneDetay(@PathVariable Long id) {
        Eczane eczane = eczaneService.eczaneGetir(id);
        return ResponseEntity.ok(ApiResponse.success(eczane));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Eczane>> eczaneEkle(@RequestBody Eczane eczane) {
        Eczane yeniEczane = eczaneService.eczaneKaydet(eczane);
        return ResponseEntity.ok(ApiResponse.success("Eczane eklendi", yeniEczane));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Eczane>> eczaneGuncelle(
            @PathVariable Long id,
            @RequestBody Eczane eczane) {
        Eczane guncelEczane = eczaneService.eczaneGuncelle(id, eczane);
        return ResponseEntity.ok(ApiResponse.success("Eczane güncellendi", guncelEczane));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eczaneSil(@PathVariable Long id) {
        eczaneService.eczaneSil(id);
        return ResponseEntity.ok(ApiResponse.success("Eczane silindi", null));
    }
}
