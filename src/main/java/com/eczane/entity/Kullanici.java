package com.eczane.entity;

import com.eczane.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "kullanici", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Kullanici extends BaseEntity {

    @NotBlank(message = "Ad boş olamaz")
    @Column(name = "ad", nullable = false)
    private String ad;

    @NotBlank(message = "Soyad boş olamaz")
    @Column(name = "soyad", nullable = false)
    private String soyad;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    @Column(name = "sifre", nullable = false)
    private String sifre;

    @Column(name = "telefon")
    private String telefon;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Role rol = Role.USER;

    @Column(name = "profil_resmi")
    private String profilResmi;

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Ilac> ilaclar = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Hatirlatici> hatirlaticilar = new HashSet<>();
}
