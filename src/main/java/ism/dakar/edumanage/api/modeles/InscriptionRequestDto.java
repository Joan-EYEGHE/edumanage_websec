package ism.dakar.edumanage.api.modeles;


import ism.dakar.edumanage.security.api.models.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionRequestDto extends AbstractDto {
    private Long apprenantId;
    private Long formationId;
    private double montant;
    private String modePaiement;
}
