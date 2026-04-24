package ism.dakar.edumanage.api.controllers.impls;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ism.dakar.edumanage.api.controllers.interfaces.IPaiementRestController;
import ism.dakar.edumanage.api.modeles.PaiementResponseDto;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.services.interfaces.PaiementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/paiements")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaiementRestControllerImpl implements IPaiementRestController {

    private final PaiementService paiementService;

    @Override
    public Response<Object> create(PaiementResponseDto dtoRequest) {
        try {
            var dto = paiementService.create(dtoRequest);
            return Response.ok().setPayload(dto).setMessage("Paiement cree");
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
        
    }

    @Override
    public Response<Object> update(Long id, PaiementResponseDto dto) {
        try {
            dto.setId(id);
            return Response.ok().setPayload(paiementService.update(dto))
                    .setMessage("Paiement mis a jour");
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> get(Long id) {
        try {
            return Response.ok().setPayload(paiementService.get(id));
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> getAll(Map<String, String> searchParams, Pageable pageable) {
        Page<PaiementResponseDto> page = paiementService.getAll(searchParams, pageable);
        return Response.ok().setPayload(page.getContent()).setMetadata(
                Map.of("totalElements", page.getTotalElements(),
                       "totalPages", page.getTotalPages(),
                       "currentPage", page.getNumber()));
    }

    @Override
    public Response<Object> getAllList(Map<String, String> searchParams) {
        List<PaiementResponseDto> list = paiementService.getAll(searchParams);
        return Response.ok().setPayload(list);
    }

    @Override
    public Response<Object> countAll(Map<String, String> searchParams) {
        return Response.ok().setPayload(paiementService.countAll(searchParams));
    }

    @Override
    public Response<Object> delete(Long id) {
        try {
            paiementService.delete(id);
            return Response.ok().setMessage("Paiement supprimé");
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }
}
