package ism.dakar.edumanage.api.controllers.impls;

import ism.dakar.edumanage.api.controllers.interfaces.IUserRestController;
import ism.dakar.edumanage.api.mappers.UserMapper;
import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.api.models.LoginDto;
import ism.dakar.edumanage.security.api.models.Response;
import ism.dakar.edumanage.security.api.models.TokenReponseDto;
import ism.dakar.edumanage.security.configs.UserAuthenticationProvider;
import ism.dakar.edumanage.security.exceptions.AccountLockedException;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
@CrossOrigin("*")
@Primary
public class UserRestControllerImpls implements IUserRestController {

    private final UserService service;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Override
    @Transactional
    public Response<Object> login(LoginDto loginUserDto) {
        try{
            UserDto dto = service.login(loginUserDto);

            if (!dto.isActif()) return Response.accountLocked().setMessage("Vous n'avez plus access au systeme");

            var tokenDto = TokenReponseDto.builder()
                    .accessToken(userAuthenticationProvider.createToken(dto))
                    .user(dto)
                    .expiresIn(null)
                    .build();

            return Response.ok().setPayload(tokenDto).setMessage("Utilisateur authentifie");
        } catch (NotFoundException ex) {
            return Response.invalidCredentials().setMessage(ex.getMessage());
        }catch (AccountLockedException ex) {
            return Response.accountLocked().setMessage(ex.getMessage());
        }catch (Exception ex){
            return Response.exception().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> getMe(@AuthenticationPrincipal AppUserDto dto) {
        try {
            var me = service.getByLogin(dto.getEmail());
            if (me!=null)
                return Response.ok().setPayload(me).setMessage("Informations chargé");
            else
                return Response.notFound().setMessage("Utilisateur non trouvé");
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.exception().setMessage(ex);
        }
    }

    @Override
    public Response<Object> create(UserDto dtoRequest) {
        try {
            var dto_ = service.create(dtoRequest);
            return Response.ok().setPayload(dto_).setMessage("Utilisateur ajouté");
        }catch (NotFoundException ex){
            return Response.notFound().setMessage(ex.getMessage());
        }catch (Exception ex){
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> update(Long id, UserDto dto) {
        dto.setId(id);
        try {
            var dto_ = service.update(dto);
            return Response.ok().setPayload(dto_).setMessage("Utilisateur mis a jour");
        }catch (NotFoundException ex){
            return Response.notFound().setMessage(ex.getMessage());
        }catch (Exception ex){
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Override
    public Response<Object> get(Long id) {
        return null;
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
    public Response<Object> delete(Long id) {
        return null;
    }
}
