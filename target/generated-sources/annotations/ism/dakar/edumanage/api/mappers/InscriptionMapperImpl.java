package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.datas.entities.FormationEntity;
import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import ism.dakar.edumanage.datas.entities.UserEntity;
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
public class InscriptionMapperImpl implements InscriptionMapper {

    @Override
    public List<InscriptionDto> parse(List<InscriptionEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<InscriptionDto> list = new ArrayList<InscriptionDto>( entities.size() );
        for ( InscriptionEntity inscriptionEntity : entities ) {
            list.add( asDto( inscriptionEntity ) );
        }

        return list;
    }

    @Override
    public InscriptionDto asDto(InscriptionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InscriptionDto inscriptionDto = new InscriptionDto();

        inscriptionDto.setApprenantId( entityApprenantId( entity ) );
        inscriptionDto.setApprenantNom( entityApprenantNom( entity ) );
        inscriptionDto.setFormationId( entityFormationId( entity ) );
        inscriptionDto.setFormationTitre( entityFormationTitre( entity ) );
        inscriptionDto.setId( entity.getId() );
        inscriptionDto.setCreateAt( entity.getCreateAt() );
        inscriptionDto.setActif( entity.isActif() );
        inscriptionDto.setStatut( entity.getStatut() );
        inscriptionDto.setDateInscription( entity.getDateInscription() );
        inscriptionDto.setModePaiement( entity.getModePaiement() );

        return inscriptionDto;
    }

    @Override
    public InscriptionEntity asEntity(InscriptionDto dto) {
        if ( dto == null ) {
            return null;
        }

        InscriptionEntity inscriptionEntity = new InscriptionEntity();

        inscriptionEntity.setApprenant( inscriptionDtoToUserEntity( dto ) );
        inscriptionEntity.setFormation( inscriptionDtoToFormationEntity( dto ) );
        inscriptionEntity.setId( dto.getId() );
        inscriptionEntity.setActif( dto.isActif() );
        inscriptionEntity.setCreateAt( dto.getCreateAt() );
        inscriptionEntity.setStatut( dto.getStatut() );
        inscriptionEntity.setModePaiement( dto.getModePaiement() );
        inscriptionEntity.setDateInscription( dto.getDateInscription() );

        return inscriptionEntity;
    }

    @Override
    public void updateEntityFromDto(InscriptionDto dto, InscriptionEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActif( dto.isActif() );
        entity.setCreateAt( dto.getCreateAt() );
        entity.setStatut( dto.getStatut() );
        entity.setModePaiement( dto.getModePaiement() );
        entity.setDateInscription( dto.getDateInscription() );
    }

    private Long entityApprenantId(InscriptionEntity inscriptionEntity) {
        if ( inscriptionEntity == null ) {
            return null;
        }
        UserEntity apprenant = inscriptionEntity.getApprenant();
        if ( apprenant == null ) {
            return null;
        }
        Long id = apprenant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityApprenantNom(InscriptionEntity inscriptionEntity) {
        if ( inscriptionEntity == null ) {
            return null;
        }
        UserEntity apprenant = inscriptionEntity.getApprenant();
        if ( apprenant == null ) {
            return null;
        }
        String nom = apprenant.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    private Long entityFormationId(InscriptionEntity inscriptionEntity) {
        if ( inscriptionEntity == null ) {
            return null;
        }
        FormationEntity formation = inscriptionEntity.getFormation();
        if ( formation == null ) {
            return null;
        }
        Long id = formation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFormationTitre(InscriptionEntity inscriptionEntity) {
        if ( inscriptionEntity == null ) {
            return null;
        }
        FormationEntity formation = inscriptionEntity.getFormation();
        if ( formation == null ) {
            return null;
        }
        String titre = formation.getTitre();
        if ( titre == null ) {
            return null;
        }
        return titre;
    }

    protected UserEntity inscriptionDtoToUserEntity(InscriptionDto inscriptionDto) {
        if ( inscriptionDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( inscriptionDto.getApprenantId() );

        return userEntity;
    }

    protected FormationEntity inscriptionDtoToFormationEntity(InscriptionDto inscriptionDto) {
        if ( inscriptionDto == null ) {
            return null;
        }

        FormationEntity formationEntity = new FormationEntity();

        formationEntity.setId( inscriptionDto.getFormationId() );

        return formationEntity;
    }
}
