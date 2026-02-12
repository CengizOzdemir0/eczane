package com.eczane.entity;

import com.eczane.enums.HatirlaticiSiklik;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "hatirlatici")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Hatirlatici extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ilac_id", nullable = false)
    private Ilac ilac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @NotNull(message = "Hatırlatıcı zamanı boş olamaz")
    @Column(name = "zaman", nullable = false)
    private LocalTime zaman;

    @Enumerated(EnumType.STRING)
    @Column(name = "siklik", nullable = false)
    private HatirlaticiSiklik siklik = HatirlaticiSiklik.GUNLUK;

    @Column(name = "aktif")
    private Boolean aktif = true;

    @Column(name = "mesaj", length = 500)
    private String mesaj;

    @Column(name = "son_gonderim")
    private java.time.LocalDateTime sonGonderim;
}
