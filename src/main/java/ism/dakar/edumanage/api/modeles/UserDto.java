package ism.dakar.edumanage.api.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ism.dakar.edumanage.security.api.models.AppUserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends AppUserDto implements Serializable {

    private String nom;

    private String prenom;

    private String email;

    private String telephone;
}
