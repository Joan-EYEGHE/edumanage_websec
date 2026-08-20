package ism.dakar.edumanage.datas.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 559390505L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final ism.dakar.edumanage.security.datas.entity.QAppUser _super = new ism.dakar.edumanage.security.datas.entity.QAppUser(this);

    //inherited
    public final ListPath<ism.dakar.edumanage.security.datas.entity.AccesEntity, ism.dakar.edumanage.security.datas.entity.QAccesEntity> access = _super.access;

    //inherited
    public final BooleanPath actif = _super.actif;

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    //inherited
    public final StringPath email = _super.email;

    public final ListPath<FormationEntity, QFormationEntity> formationsAssignees = this.<FormationEntity, QFormationEntity>createList("formationsAssignees", FormationEntity.class, QFormationEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<InscriptionEntity, QInscriptionEntity> inscriptions = this.<InscriptionEntity, QInscriptionEntity>createList("inscriptions", InscriptionEntity.class, QInscriptionEntity.class, PathInits.DIRECT2);

    public final StringPath nom = createString("nom");

    //inherited
    public final StringPath password = _super.password;

    public final StringPath prenom = createString("prenom");

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public final StringPath telephone = createString("telephone");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

