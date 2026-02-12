package com.eczane.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "eczane")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Eczane extends BaseEntity {

    @NotBlank(message = "Eczane adı boş olamaz")
    @Column(name = "ad", nullable = false)
    private String ad;

    @NotBlank(message = "Adres boş olamaz")
    @Column(name = "adres", nullable = false, length = 500)
    private String adres;

    @NotBlank(message = "Telefon boş olamaz")
    @Column(name = "telefon", nullable = false)
    private String telefon;

    @Column(name = "email")
    private String email;

    @NotNull(message = "Enlem bilgisi boş olamaz")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull(message = "Boylam bilgisi boş olamaz")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "il")
    private String il;

    @Column(name = "ilce")
    private String ilce;

    @Column(name = "nobetci")
    private Boolean nobetci = false;

    @Column(name = "nobetci_baslangic")
    private java.time.LocalDate nobetciBaslangic;

    @Column(name = "nobetci_bitis")
    private java.time.LocalDate nobetciBitis;

    @Column(name = "aciklama", length = 1000)
    private String aciklama;

    @Column(name = "calisma_saatleri")
    private String calismaSaatleri;
}
