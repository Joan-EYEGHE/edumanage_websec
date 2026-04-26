package ism.dakar.edumanage.datas.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFormationEntity is a Querydsl query type for FormationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFormationEntity extends EntityPathBase<FormationEntity> {

    private static final long serialVersionUID = 1671213369L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFormationEntity formationEntity = new QFormationEntity("formationEntity");

    public final ism.dakar.edumanage.security.datas.entity.QAbstractEntity _super = new ism.dakar.edumanage.security.datas.entity.QAbstractEntity(this);

    //inherited
    public final BooleanPath actif = _super.actif;

    public final StringPath afficheUrl = createString("afficheUrl");

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    public final StringPath description = createString("description");

    public final NumberPath<Double> duree = createNumber("duree", Double.class);

    public final QUserEntity formateur;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<InscriptionEntity, QInscriptionEntity> inscriptions = this.<InscriptionEntity, QInscriptionEntity>createList("inscriptions", InscriptionEntity.class, QInscriptionEntity.class, PathInits.DIRECT2);

    public final NumberPath<Double> prix = createNumber("prix", Double.class);

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public final StringPath titre = createString("titre");

    public QFormationEntity(String variable) {
        this(FormationEntity.class, forVariable(variable), INITS);
    }

    public QFormationEntity(Path<? extends FormationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFormationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFormationEntity(PathMetadata metadata, PathInits inits) {
        this(FormationEntity.class, metadata, inits);
    }

    public QFormationEntity(Class<? extends FormationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.formateur = inits.isInitialized("formateur") ? new QUserEntity(forProperty("formateur")) : null;
    }

}

