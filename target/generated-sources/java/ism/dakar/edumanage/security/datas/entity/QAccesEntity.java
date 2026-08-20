package ism.dakar.edumanage.security.datas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccesEntity is a Querydsl query type for AccesEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccesEntity extends EntityPathBase<AccesEntity> {

    private static final long serialVersionUID = 1883185805L;

    public static final QAccesEntity accesEntity = new QAccesEntity("accesEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final BooleanPath actif = _super.actif;

    public final StringPath code = createString("code");

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    public final StringPath description = createString("description");

    public final ListPath<AbstractAcessEntity, QAbstractAcessEntity> entities = this.<AbstractAcessEntity, QAbstractAcessEntity>createList("entities", AbstractAcessEntity.class, QAbstractAcessEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public final BooleanPath visible = createBoolean("visible");

    public QAccesEntity(String variable) {
        super(AccesEntity.class, forVariable(variable));
    }

    public QAccesEntity(Path<? extends AccesEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccesEntity(PathMetadata metadata) {
        super(AccesEntity.class, metadata);
    }

}

