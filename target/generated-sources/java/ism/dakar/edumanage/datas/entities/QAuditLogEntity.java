package ism.dakar.edumanage.datas.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditLogEntity is a Querydsl query type for AuditLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuditLogEntity extends EntityPathBase<AuditLogEntity> {

    private static final long serialVersionUID = -1153270585L;

    public static final QAuditLogEntity auditLogEntity = new QAuditLogEntity("auditLogEntity");

    public final ism.dakar.edumanage.security.datas.entity.QAbstractEntity _super = new ism.dakar.edumanage.security.datas.entity.QAbstractEntity(this);

    //inherited
    public final BooleanPath actif = _super.actif;

    public final StringPath action = createString("action");

    public final StringPath adresseIp = createString("adresseIp");

    public final StringPath cible = createString("cible");

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    public final StringPath details = createString("details");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> loggedAt = createDateTime("loggedAt", java.time.LocalDateTime.class);

    //inherited
    public final EnumPath<ism.dakar.edumanage.security.datas.enums.StatutEnum> statut = _super.statut;

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userName = createString("userName");

    public QAuditLogEntity(String variable) {
        super(AuditLogEntity.class, forVariable(variable));
    }

    public QAuditLogEntity(Path<? extends AuditLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditLogEntity(PathMetadata metadata) {
        super(AuditLogEntity.class, metadata);
    }

}

