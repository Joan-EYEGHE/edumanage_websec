package ism.dakar.edumanage.security.api.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ism.dakar.edumanage.security.api.models.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface IRestControllerFull<D> {
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    Response<Object> create(@RequestBody D dtoRequest);

    @Operation(summary = "Modifier un element")
    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    Response<Object> update(@Parameter(name = "id") @PathVariable("id") Long id, @RequestBody D dto);

    @Operation(summary = "Recuperer un element")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400"), @ApiResponse(responseCode = "404"), @ApiResponse(responseCode = "500")})
    @GetMapping("/{id}/get")
    @ResponseStatus(HttpStatus.OK)
    Response<Object> get(@Parameter @PathVariable Long id);

    @Operation(summary = "Lister tout les elements")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "500")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Response<Object> getAll(@RequestParam Map<String, String> searchParams, Pageable pageable);


    @Operation(summary = "Lister tout les elements sans pagination")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "500")})
    @GetMapping("/all-list")
    @ResponseStatus(HttpStatus.OK)
    Response<Object> getAllList(@RequestParam Map<String, String> searchParams);

    @Operation(summary = "Counter tous les elements")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "500")})
    @GetMapping("/count-all")
    @ResponseStatus(HttpStatus.OK)
    Response<Object> countAll(@RequestParam Map<String, String> searchParams);

    @Operation(summary = "Supprimer un element")
    @ApiResponses(value = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "400"), @ApiResponse(responseCode = "404"), @ApiResponse(responseCode = "500")})
    @DeleteMapping("/{id}/delete")
    Response<Object> delete(@PathVariable("id") Long id) ;

}
