package ism.dakar.edumanage.security.api.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ism.dakar.edumanage.security.api.models.LoginDto;
import ism.dakar.edumanage.security.api.models.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SecurityController {

    @Operation(summary = "Authentification")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping("/login")
    Response<Object> login(@RequestBody LoginDto loginUserDto);
}
