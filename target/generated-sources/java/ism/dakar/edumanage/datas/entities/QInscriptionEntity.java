package ism.dakar.edumanage.datas.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInscriptionEntity is a Querydsl query type for InscriptionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInscriptionEntity extends EntityPathBase<InscriptionEntity> {

    private static final long serialVersionUID = -1237209632L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInscriptionEntity inscriptionEntity = new QInscriptionEntity("inscriptionEntity");

    public final ism.dakar.edumanage.security.datas.entity.QAbstractEntity _super = new ism.dakar.edumanage.security.datas.entity.QAbstractEntity(this);

    //inherited
    public final BooleanPath actif = _super.actif;

    public final QUserEntity apprenant;

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    public final DatePath<java.time.LocalDate> dateInscription = createDate("dateInscription", java.time.LocalDate.class);

    public final QFormationEntity formation;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath modePaiement = createString("modePaiement");

    public final ListPath<PaiementEntity, QPaiementEntity> paiements = this.<PaiementEntity, QPaiementEntity>createList("paiements", PaiementEntity.class, QPaiementEntity.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public QInscriptionEntity(String variable) {
        this(InscriptionEntity.class, forVariable(variable), INITS);
    }

    public QInscriptionEntity(Path<? extends InscriptionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInscriptionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInscriptionEntity(PathMetadata metadata, PathInits inits) {
        this(InscriptionEntity.class, metadata, inits);
    }

    public QInscriptionEntity(Class<? extends InscriptionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.apprenant = inits.isInitialized("apprenant") ? new QUserEntity(forProperty("apprenant")) : null;
        this.formation = inits.isInitialized("formation") ? new QFormationEntity(forProperty("formation"), inits.get("formation")) : null;
    }

}

