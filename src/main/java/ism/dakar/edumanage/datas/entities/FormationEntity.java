package ism.dakar.edumanage.datas.entities;

import ism.dakar.edumanage.security.datas.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"formateur", "inscriptions"})
public class FormationEntity extends AbstractEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String afficheUrl;

    private double duree;

    @Column(nullable = false)
    private double prix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formateur_id")
    private UserEntity formateur;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscriptionEntity> inscriptions = new ArrayList<>();
}
