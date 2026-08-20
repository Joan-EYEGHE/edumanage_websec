package ism.dakar.edumanage.security.datas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAbstractAcessEntity is a Querydsl query type for AbstractAcessEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAbstractAcessEntity extends EntityPathBase<AbstractAcessEntity> {

    private static final long serialVersionUID = -240314305L;

    public static final QAbstractAcessEntity abstractAcessEntity = new QAbstractAcessEntity("abstractAcessEntity");

    public final ListPath<AccesEntity, QAccesEntity> access = this.<AccesEntity, QAccesEntity>createList("access", AccesEntity.class, QAccesEntity.class, PathInits.DIRECT2);

    public final BooleanPath actif = createBoolean("actif");

    public final DatePath<java.time.LocalDate> createAt = createDate("createAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = createEnum("statut", ism.dakar.edumanage.security.datas.enums.StatutEnum.class);

    public QAbstractAcessEntity(String variable) {
        super(AbstractAcessEntity.class, forVariable(variable));
    }

    public QAbstractAcessEntity(Path<? extends AbstractAcessEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractAcessEntity(PathMetadata metadata) {
        super(AbstractAcessEntity.class, metadata);
    }

}

