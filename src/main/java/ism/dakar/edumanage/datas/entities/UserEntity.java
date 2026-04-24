package ism.dakar.edumanage.datas.entities;


import ism.dakar.edumanage.security.datas.entity.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue(value = "User")
@ToString(exclude = {"formationsAssignees", "inscriptions"})
public class UserEntity extends AppUser implements Serializable {

    private String nom;

    private String prenom;

    private String telephone;

    // relations bidirectionnelles
    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormationEntity> formationsAssignees = new ArrayList<>();

    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscriptionEntity> inscriptions = new ArrayList<>();
}
