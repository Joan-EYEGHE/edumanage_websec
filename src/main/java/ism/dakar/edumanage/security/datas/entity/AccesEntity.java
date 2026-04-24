package ism.dakar.edumanage.security.datas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "access")
@Builder
public class AccesEntity extends AbstractEntity implements Serializable {

    @Column(unique = true)
    private String code;

    private String description;

    private boolean visible;

//    relations
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "access")
    List<AbstractAcessEntity> entities= new ArrayList<>();

}
