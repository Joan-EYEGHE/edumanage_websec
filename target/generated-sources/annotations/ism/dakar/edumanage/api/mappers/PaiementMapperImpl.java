package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.PaiementResponseDto;
import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import ism.dakar.edumanage.datas.entities.PaiementEntity;
import ism.dakar.edumanage.datas.entities.UserEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T22:42:55+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class PaiementMapperImpl implements PaiementMapper {

    @Override
    public List<PaiementResponseDto> parse(List<PaiementEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaiementResponseDto> list = new ArrayList<PaiementResponseDto>( entities.size() );
        for ( PaiementEntity paiementEntity : entities ) {
            list.add( asDto( paiementEntity ) );
        }

        return list;
    }

    @Override
    public PaiementResponseDto asDto(PaiementEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PaiementResponseDto paiementResponseDto = new PaiementResponseDto();

        paiementResponseDto.setInscriptionId( entityInscriptionId( entity ) );
        paiementResponseDto.setApprenantNom( entityInscriptionApprenantNom( entity ) );
        paiementResponseDto.setId( entity.getId() );
        paiementResponseDto.setCreateAt( entity.getCreateAt() );
        paiementResponseDto.setActif( entity.isActif() );
        paiementResponseDto.setStatut( entity.getStatut() );
        paiementResponseDto.setMontant( BigDecimal.valueOf( entity.getMontant() ) );
        paiementResponseDto.setModePaiement( entity.getModePaiement() );
        paiementResponseDto.setReferenceTransaction( entity.getReferenceTransaction() );
        paiementResponseDto.setDatePaiement( entity.getDatePaiement() );

        return paiementResponseDto;
    }

    @Override
    public PaiementEntity asEntity(PaiementResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        PaiementEntity paiementEntity = new PaiementEntity();

        paiementEntity.setInscription( paiementResponseDtoToInscriptionEntity( dto ) );
        paiementEntity.setId( dto.getId() );
        paiementEntity.setActif( dto.isActif() );
        paiementEntity.setCreateAt( dto.getCreateAt() );
        paiementEntity.setStatut( dto.getStatut() );
        if ( dto.getMontant() != null ) {
            paiementEntity.setMontant( dto.getMontant().doubleValue() );
        }
        paiementEntity.setModePaiement( dto.getModePaiement() );
        paiementEntity.setReferenceTransaction( dto.getReferenceTransaction() );
        paiementEntity.setDatePaiement( dto.getDatePaiement() );

        return paiementEntity;
    }

    @Override
    public void updateEntityFromDto(PaiementResponseDto dto, PaiementEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActif( dto.isActif() );
        entity.setCreateAt( dto.getCreateAt() );
        entity.setStatut( dto.getStatut() );
        if ( dto.getMontant() != null ) {
            entity.setMontant( dto.getMontant().doubleValue() );
        }
        entity.setModePaiement( dto.getModePaiement() );
        entity.setReferenceTransaction( dto.getReferenceTransaction() );
        entity.setDatePaiement( dto.getDatePaiement() );
    }

    private Long entityInscriptionId(PaiementEntity paiementEntity) {
        if ( paiementEntity == null ) {
            return null;
        }
        InscriptionEntity inscription = paiementEntity.getInscription();
        if ( inscription == null ) {
            return null;
        }
        Long id = inscription.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityInscriptionApprenantNom(PaiementEntity paiementEntity) {
        if ( paiementEntity == null ) {
            return null;
        }
        InscriptionEntity inscription = paiementEntity.getInscription();
        if ( inscription == null ) {
            return null;
        }
        UserEntity apprenant = inscription.getApprenant();
        if ( apprenant == null ) {
            return null;
        }
        String nom = apprenant.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected InscriptionEntity paiementResponseDtoToInscriptionEntity(PaiementResponseDto paiementResponseDto) {
        if ( paiementResponseDto == null ) {
            return null;
        }

        InscriptionEntity inscriptionEntity = new InscriptionEntity();

        inscriptionEntity.setId( paiementResponseDto.getInscriptionId() );

        return inscriptionEntity;
    }
}
