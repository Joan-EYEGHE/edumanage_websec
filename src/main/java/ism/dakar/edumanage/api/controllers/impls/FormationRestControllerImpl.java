package ism.dakar.edumanage.api.controllers.impls;

import java.util.List;
import java.util.Map;

import ism.dakar.edumanage.api.modeles.FormationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ism.dakar.edumanage.api.controllers.interfaces.IFormationRestController;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.security.datas.enums.Role;
import ism.dakar.edumanage.services.interfaces.FormationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/formations")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FormationRestControllerImpl implements IFormationRestController {

    private final FormationService formationService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> create(FormationDto dtoRequest) {
        try {
            var dto = formationService.create(dtoRequest);
            return Response.ok().setPayload(dto).setMessage("Formation cree");
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> update(Long id, FormationDto dto) {
        try {
            dto.setId(id);
            return Response.ok().setPayload(formationService.update(dto))
                    .setMessage("Formation mis a jour");
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "','" + Role.Constants.APPRENANT + "')")
    public Response<Object> get(Long id) {
        try {
            return Response.ok().setPayload(formationService.get(id));
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "','" + Role.Constants.APPRENANT + "')")
    public Response<Object> getAll(Map<String, String> searchParams, Pageable pageable) {
        Page<FormationDto> page = formationService.getAll(searchParams, pageable);
        return Response.ok().setPayload(page.getContent()).setMetadata(
                Map.of("totalElements", page.getTotalElements(),
                        "totalPages", page.getTotalPages(),
                        "currentPage", page.getNumber()));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "','" + Role.Constants.APPRENANT + "')")
    public Response<Object> getAllList(Map<String, String> searchParams) {
        List<FormationDto> list = formationService.getAll(searchParams);
        return Response.ok().setPayload(list);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "','" + Role.Constants.APPRENANT + "')")
    public Response<Object> countAll(Map<String, String> searchParams) {
        return Response.ok().setPayload(formationService.countAll(searchParams));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> delete(Long id) {
        try {
            formationService.delete(id);
            return Response.ok().setMessage("Formation supprimée");
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
    }
}
