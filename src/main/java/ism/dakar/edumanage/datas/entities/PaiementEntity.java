package ism.dakar.edumanage.datas.entities;

import ism.dakar.edumanage.security.datas.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paiements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "inscription")
public class PaiementEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscription_id", nullable = false)
    private InscriptionEntity inscription;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private String modePaiement;

    @Column(unique = true)
    private String referenceTransaction;

    private LocalDate datePaiement;
}

