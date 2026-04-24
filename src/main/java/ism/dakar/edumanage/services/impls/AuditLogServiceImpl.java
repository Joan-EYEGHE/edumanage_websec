package ism.dakar.edumanage.services.impls;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import ism.dakar.edumanage.security.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import ism.dakar.edumanage.api.mappers.AuditLogMapper;
import ism.dakar.edumanage.api.modeles.AuditLogResponseDto;
import ism.dakar.edumanage.datas.entities.AuditLogEntity;
import ism.dakar.edumanage.datas.entities.QAuditLogEntity;
import ism.dakar.edumanage.datas.repositories.AuditLogRepository;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.services.interfaces.AuditLogService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public AuditLogResponseDto create(AuditLogResponseDto dto) {
        throw new UnsupportedOperationException("Audit logs are created internally");
    }

    @Override
    public AuditLogResponseDto update(AuditLogResponseDto dto) {
        throw new UnsupportedOperationException("Audit logs cannot be updated");
    }

    @Override
    public void delete(Long id) {
        try {
            var optional = auditLogRepository.findById(id);
            if (optional.isEmpty())
                throw new NotFoundException("Log introuvable");

            auditLogRepository.deleteById(id);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenue");
        }
    }

    @Override
    public AuditLogResponseDto get(Long id) {
        return auditLogMapper.asDto(
                auditLogRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Log introuvable")));
    }

    @Override
    public Page<AuditLogResponseDto> getAll(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return auditLogRepository.findAll(booleanBuilder, pageable).map(auditLogMapper::asDto);
    }

    @Override
    public List<AuditLogResponseDto> getAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return auditLogMapper.parse((List<AuditLogEntity>) auditLogRepository.findAll(booleanBuilder));
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return auditLogRepository.count(booleanBuilder);
    }


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
//        QAuditLogEntity qAuditLog = QAuditLogEntity.auditLogEntity;
//
//        if (searchParams.containsKey("userEmail")) {
//            builder.and(qAuditLog.userEmail.containsIgnoreCase(searchParams.get("userEmail")));
//        }
//        if (searchParams.containsKey("action")) {
//            builder.and(qAuditLog.action.containsIgnoreCase(searchParams.get("action")));
//        }
        }
    }

}
