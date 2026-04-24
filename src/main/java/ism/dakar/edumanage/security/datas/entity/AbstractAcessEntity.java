package ism.dakar.edumanage.security.datas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "base_acces_entity")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class AbstractAcessEntity implements Serializable {
    @Id
    @SequenceGenerator(
            name = "global_entity_seq_gen",
            sequenceName = "global_entity_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_entity_seq_gen")
    @Column(nullable = false)
    private Long id;

//    @Column(name = "active")
    private boolean actif;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createAt;

    @Enumerated(EnumType.STRING)
    private StatutEnum statut;

//    relations
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "abstract_access",
            joinColumns = @JoinColumn(name = "abstract_id"),
            inverseJoinColumns =@JoinColumn(name = "acces_id")
    )
    List<AccesEntity> access= new ArrayList<>();

}
