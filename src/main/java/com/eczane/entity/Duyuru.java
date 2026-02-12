package com.eczane.entity;

import com.eczane.enums.DuyuruTip;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "duyuru")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Duyuru extends BaseEntity {

    @NotBlank(message = "Başlık boş olamaz")
    @Column(name = "baslik", nullable = false)
    private String baslik;

    @NotBlank(message = "İçerik boş olamaz")
    @Column(name = "icerik", nullable = false, length = 2000)
    private String icerik;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip", nullable = false)
    private DuyuruTip tip = DuyuruTip.GENEL;

    @Column(name = "yayinlanma_tarihi")
    private java.time.LocalDateTime yayinlanmaTarihi;

    @Column(name = "bitis_tarihi")
    private java.time.LocalDateTime bitisTarihi;

    @Column(name = "onemli")
    private Boolean onemli = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "olusturan_id")
    private Kullanici olusturan;
}
