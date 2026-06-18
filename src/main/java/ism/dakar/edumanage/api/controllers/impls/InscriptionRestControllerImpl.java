package ism.dakar.edumanage.api.controllers.impls;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ism.dakar.edumanage.api.controllers.interfaces.IInscriptionRestController;
import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.security.datas.enums.Role;
import ism.dakar.edumanage.services.interfaces.InscriptionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inscriptions")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InscriptionRestControllerImpl implements IInscriptionRestController {

    private final InscriptionService inscriptionService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "')")
    public Response<Object> create(InscriptionDto dto) {
        try {
            return Response.ok().setPayload(inscriptionService.create(dto))
                    .setMessage("Inscription réussie");
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "')")
    public Response<Object> update(Long id, InscriptionDto dto) {
        try {
            dto.setId(id);
            return Response.ok().setPayload(inscriptionService.update(dto))
                    .setMessage("Inscription réussie");
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> get(Long id) {
        try {
            return Response.ok().setPayload(inscriptionService.get(id));
        } catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> getAll(Map<String, String> searchParams, Pageable pageable) {
        Page<InscriptionDto> page = inscriptionService.getAll(searchParams, pageable);
        return Response.ok().setPayload(page.getContent()).setMetadata(
                Map.of("totalElements", page.getTotalElements(),
                       "totalPages", page.getTotalPages(),
                       "currentPage", page.getNumber()));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> getAllList(Map<String, String> searchParams) {
        List<InscriptionDto> list = inscriptionService.getAll(searchParams);
        return Response.ok().setPayload(list);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "','" + Role.Constants.FORMATEUR + "')")
    public Response<Object> countAll(Map<String, String> searchParams) {
        return Response.ok().setPayload(inscriptionService.countAll(searchParams));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + Role.Constants.ADMINISTRATEUR + "','" + Role.Constants.GESTIONNAIRE + "')")
    public Response<Object> delete(Long id) {
        inscriptionService.delete(id);
        return Response.ok().setMessage("Inscription supprimé");
    }
}
