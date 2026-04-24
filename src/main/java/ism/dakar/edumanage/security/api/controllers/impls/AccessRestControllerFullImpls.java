package ism.dakar.edumanage.security.api.controllers.impls;

import ism.dakar.edumanage.security.api.controllers.interfaces.IAccessRestControllerFull;
import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.security.services.interfaces.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("access")
@CrossOrigin("*")
public class AccessRestControllerFullImpls implements IAccessRestControllerFull {
    private final AccessService service;

    @Override
    public Response<Object> create(AccessDto dtoRequest) {
        try {
            var dto = service.create(dtoRequest);
            return Response.ok().setPayload(dto).setMessage("Acces créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> update(Long id, AccessDto dtoRequest) {
        dtoRequest.setId(id);
        try {
            var dto = service.update(dtoRequest);
            return Response.ok().setPayload(dto).setMessage("Acces modifié");
        } catch (NotFoundException ex) {
            return Response.notFound().setMessage(ex.getMessage());
        }  catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> get(Long id) {
        try {
            var dto = service.get(id);
            return Response.ok().setPayload(dto).setMessage("Acces trouvé");
        } catch (NotFoundException ex) {
            return Response.notFound().setMessage(ex.getMessage());
        }  catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> getAll(Map<String, String> searchParams, Pageable pageable) {
        var page = service.getAll(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder()
                                            .number(page.getNumber())
                                            .totalElements(page.getTotalElements())
                                            .size(page.getSize())
                                            .totalPages(page.getTotalPages())
                                        .build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @Override
    public Response<Object> getAllList(Map<String, String> searchParams) {
        var datas = service.getAll(searchParams);
        return Response.ok().setPayload(datas);
    }

    @Override
    public Response<Object> countAll(Map<String, String> searchParams) {
        var nbr = service.countAll(searchParams);
        return Response.ok().setPayload(nbr);
    }

    @Override
    public Response<Object>  delete(Long id) {
        try {
            service.delete(id);
            return Response.ok().setMessage("Acces supprimé");
        } catch (NotFoundException ex) {
            return Response.notFound().setMessage(ex.getMessage());
        } catch (Exception e) {
            return Response.exception().setMessage(e.getMessage());
        }
    }
}
