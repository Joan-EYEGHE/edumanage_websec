package ism.dakar.edumanage.api.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ism.dakar.edumanage.security.api.models.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InscriptionDto extends AbstractDto {

    private Long apprenantId;
    private String apprenantNom;
    private Long formationId;
    private String formationTitre;
    private LocalDate dateInscription;
    private double montant;
    private String modePaiement;
}
