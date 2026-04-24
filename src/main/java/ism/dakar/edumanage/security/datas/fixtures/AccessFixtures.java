package ism.dakar.edumanage.security.datas.fixtures;

import ism.dakar.edumanage.security.api.mappers.AccessMapper;
import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import ism.dakar.edumanage.security.datas.repository.AccessRepository;
import ism.dakar.edumanage.security.helpers.AccessHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class AccessFixtures implements CommandLineRunner {

    private final AccessHelperService serviceHelper;
    private final AccessRepository repository;
    private final AccessMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        List<AccessDto> accessDtoList = new ArrayList<>();

        for (var acc : serviceHelper.getAccess()){
            if (repository.findByCode(acc.getCode())==null){
                acc.setStatut(StatutEnum.ACTIF);
                acc.setActif(true);
                accessDtoList.add(acc);
            }
        }

        if (!accessDtoList.isEmpty())
            repository.saveAll(accessDtoList.stream().map(mapper::asEntity).toList());
    }
}
