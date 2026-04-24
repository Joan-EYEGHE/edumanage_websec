package ism.dakar.edumanage.api.controllers.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.api.modeles.InscriptionRequestDto;
import ism.dakar.edumanage.security.api.controllers.interfaces.IRestController;
import ism.dakar.edumanage.security.api.controllers.interfaces.IRestControllerFull;
import ism.dakar.edumanage.security.api.models.Response;
import org.springframework.http.HttpStatus;

public interface IInscriptionRestController extends IRestControllerFull<InscriptionDto> {

}
