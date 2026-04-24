package ism.dakar.edumanage.api.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ism.dakar.edumanage.security.api.models.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaiementResponseDto extends AbstractDto {

    private Long inscriptionId;
    private String apprenantNom;
    private BigDecimal montant;
    private String modePaiement;
    private String referenceTransaction;
    private LocalDate datePaiement;
}
