package ism.dakar.edumanage.datas.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaiementEntity is a Querydsl query type for PaiementEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaiementEntity extends EntityPathBase<PaiementEntity> {

    private static final long serialVersionUID = 1013370761L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaiementEntity paiementEntity = new QPaiementEntity("paiementEntity");

    public final ism.dakar.edumanage.security.datas.entity.QAbstractEntity _super = new ism.dakar.edumanage.security.datas.entity.QAbstractEntity(this);

    //inherited
    public final BooleanPath actif = _super.actif;

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    public final DatePath<java.time.LocalDate> datePaiement = createDate("datePaiement", java.time.LocalDate.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QInscriptionEntity inscription;

    public final StringPath modePaiement = createString("modePaiement");

    public final NumberPath<Double> montant = createNumber("montant", Double.class);

    public final StringPath referenceTransaction = createString("referenceTransaction");

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public QPaiementEntity(String variable) {
        this(PaiementEntity.class, forVariable(variable), INITS);
    }

    public QPaiementEntity(Path<? extends PaiementEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaiementEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaiementEntity(PathMetadata metadata, PathInits inits) {
        this(PaiementEntity.class, metadata, inits);
    }

    public QPaiementEntity(Class<? extends PaiementEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inscription = inits.isInitialized("inscription") ? new QInscriptionEntity(forProperty("inscription"), inits.get("inscription")) : null;
    }

}

