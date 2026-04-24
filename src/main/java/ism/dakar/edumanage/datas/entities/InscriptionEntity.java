package ism.dakar.edumanage.datas.entities;

import ism.dakar.edumanage.security.datas.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"apprenant", "formation", "paiements"})
public class InscriptionEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apprenant_id", nullable = false)
    private UserEntity apprenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id", nullable = false)
    private FormationEntity formation;

    private String modePaiement;
    private LocalDate dateInscription;

    @OneToMany(mappedBy = "inscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaiementEntity> paiements = new ArrayList<>();
}

