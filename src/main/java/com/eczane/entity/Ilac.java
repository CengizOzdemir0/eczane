package com.eczane.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "ilac")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Ilac extends BaseEntity {

    @NotBlank(message = "İlaç adı boş olamaz")
    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "dozaj")
    private String dozaj;

    @Column(name = "kullanim_talimati", length = 1000)
    private String kullanimTalimati;

    @Column(name = "yan_etkiler", length = 1000)
    private String yanEtkiler;

    @Column(name = "baslangic_tarihi")
    private java.time.LocalDate baslangicTarihi;

    @Column(name = "bitis_tarihi")
    private java.time.LocalDate bitisTarihi;

    @Column(name = "notlar", length = 500)
    private String notlar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;
}
