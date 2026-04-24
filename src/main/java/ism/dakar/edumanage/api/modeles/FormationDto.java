package ism.dakar.edumanage.api.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ism.dakar.edumanage.security.api.models.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormationDto extends AbstractDto {

    private Long formateurId;
    private String formateurNom;
    private String afficheUrl;
    private String titre;
    private String description;
    private Integer duree;
    private double prix;
}
