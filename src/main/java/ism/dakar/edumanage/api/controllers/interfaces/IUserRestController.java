package ism.dakar.edumanage.api.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.security.api.controllers.interfaces.IRestControllerFull;
import ism.dakar.edumanage.security.api.controllers.interfaces.SecurityController;
import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.api.models.Response;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;


public interface IUserRestController extends IRestControllerFull<UserDto>, SecurityController {

    @Operation(summary = "Recupere l'utilisateur connecte")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400"), @ApiResponse(responseCode = "404"), @ApiResponse(responseCode = "500")})
    @GetMapping("/me")
    public Response<Object> getMe(@AuthenticationPrincipal AppUserDto dto);

}
