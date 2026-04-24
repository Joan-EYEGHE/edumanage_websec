package ism.dakar.edumanage.api.controllers.impls;

import java.util.List;
import java.util.Map;

import ism.dakar.edumanage.api.modeles.AuditLogResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ism.dakar.edumanage.api.controllers.interfaces.IAuditLogRestController;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.services.interfaces.AuditLogService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/audit-logs")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuditLogRestControllerImpl implements IAuditLogRestController {

    private final AuditLogService logService;

    @Override
    public Response<Object> get(Long id) {
        try {
            return Response.ok().setPayload(logService.get(id));
        }catch (Exception ex){
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> getAll(Map<String, String> searchParams, Pageable pageable) {
        Page<AuditLogResponseDto> page = logService.getAll(searchParams, pageable);
        return Response.ok().setPayload(page.getContent()).setMetadata(
                Map.of("totalElements", page.getTotalElements(),
                        "totalPages", page.getTotalPages(),
                        "currentPage", page.getNumber()));
    }

    @Override
    public Response<Object> getAllList(Map<String, String> searchParams) {
        List<AuditLogResponseDto> list = logService.getAll(searchParams);
        return Response.ok().setPayload(list);
    }

    @Override
    public Response<Object> countAll(Map<String, String> searchParams) {
        return Response.ok().setPayload(logService.countAll(searchParams));
    }

}
