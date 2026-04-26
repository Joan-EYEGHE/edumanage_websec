package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.FormationDto;
import ism.dakar.edumanage.datas.entities.FormationEntity;
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
public class FormationMapperImpl implements FormationMapper {

    @Override
    public List<FormationDto> parse(List<FormationEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<FormationDto> list = new ArrayList<FormationDto>( entities.size() );
        for ( FormationEntity formationEntity : entities ) {
            list.add( asDto( formationEntity ) );
        }

        return list;
    }

    @Override
    public FormationDto asDto(FormationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        FormationDto formationDto = new FormationDto();

        formationDto.setFormateurId( entityFormateurId( entity ) );
        formationDto.setFormateurNom( entityFormateurNom( entity ) );
        formationDto.setId( entity.getId() );
        formationDto.setCreateAt( entity.getCreateAt() );
        formationDto.setActif( entity.isActif() );
        formationDto.setStatut( entity.getStatut() );
        formationDto.setAfficheUrl( entity.getAfficheUrl() );
        formationDto.setTitre( entity.getTitre() );
        formationDto.setDescription( entity.getDescription() );
        formationDto.setDuree( (int) entity.getDuree() );
        formationDto.setPrix( entity.getPrix() );

        return formationDto;
    }

    @Override
    public FormationEntity asEntity(FormationDto dto) {
        if ( dto == null ) {
            return null;
        }

        FormationEntity formationEntity = new FormationEntity();

        formationEntity.setFormateur( formationDtoToUserEntity( dto ) );
        formationEntity.setId( dto.getId() );
        formationEntity.setActif( dto.isActif() );
        formationEntity.setCreateAt( dto.getCreateAt() );
        formationEntity.setStatut( dto.getStatut() );
        formationEntity.setTitre( dto.getTitre() );
        formationEntity.setDescription( dto.getDescription() );
        formationEntity.setAfficheUrl( dto.getAfficheUrl() );
        if ( dto.getDuree() != null ) {
            formationEntity.setDuree( dto.getDuree() );
        }
        formationEntity.setPrix( dto.getPrix() );

        return formationEntity;
    }

    @Override
    public void updateEntityFromDto(FormationDto dto, FormationEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActif( dto.isActif() );
        entity.setCreateAt( dto.getCreateAt() );
        entity.setStatut( dto.getStatut() );
        entity.setTitre( dto.getTitre() );
        entity.setDescription( dto.getDescription() );
        entity.setAfficheUrl( dto.getAfficheUrl() );
        if ( dto.getDuree() != null ) {
            entity.setDuree( dto.getDuree() );
        }
        entity.setPrix( dto.getPrix() );
    }

    private Long entityFormateurId(FormationEntity formationEntity) {
        if ( formationEntity == null ) {
            return null;
        }
        UserEntity formateur = formationEntity.getFormateur();
        if ( formateur == null ) {
            return null;
        }
        Long id = formateur.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFormateurNom(FormationEntity formationEntity) {
        if ( formationEntity == null ) {
            return null;
        }
        UserEntity formateur = formationEntity.getFormateur();
        if ( formateur == null ) {
            return null;
        }
        String nom = formateur.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected UserEntity formationDtoToUserEntity(FormationDto formationDto) {
        if ( formationDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( formationDto.getFormateurId() );

        return userEntity;
    }
}
