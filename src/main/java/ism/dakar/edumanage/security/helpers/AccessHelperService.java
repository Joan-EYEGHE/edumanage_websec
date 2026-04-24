package ism.dakar.edumanage.security.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessHelperService {

    private static String PATH = "public/config/access.json";
    private static String PATH_DIRECTORY = "public/config";
    private final HelperService helperService;

    private static ObjectMapper mapper=new ObjectMapper();

    public List<AccessDto> getAccess() {
        try {
            init();
            if (Files.exists(Path.of(PATH))){
                return mapper.readValue(
                        new File(PATH),
                        new TypeReference<List<AccessDto>>(){}
                );
            }
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void init() {
        try {
            if (!Files.exists(Path.of(PATH))){
                helperService.createDirectoryIsNotExist(PATH_DIRECTORY);
                List<AccessDto> access = List.of(
                        new AccessDto(true, "Administrateur".toUpperCase(), "Acces Admin", false, StatutEnum.ACTIF),
                        new AccessDto(true, "Gestionnaire".toUpperCase(), "Acces Gestionaire", true, StatutEnum.ACTIF),
                        new AccessDto(true, "Formateur".toUpperCase(), "Acces Formateur", true, StatutEnum.ACTIF),
                        new AccessDto(true, "Apprenant".toUpperCase(), "Acces Aprennant", true, StatutEnum.ACTIF)
                );
                mapper.writeValue(new File(PATH), access);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
