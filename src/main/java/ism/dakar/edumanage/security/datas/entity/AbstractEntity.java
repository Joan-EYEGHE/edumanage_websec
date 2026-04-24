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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "abstract_entity")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
