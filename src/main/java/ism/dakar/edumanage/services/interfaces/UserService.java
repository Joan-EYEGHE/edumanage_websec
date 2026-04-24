package ism.dakar.edumanage.services.interfaces;


import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.security.api.models.LoginDto;
import ism.dakar.edumanage.security.services.interfaces.IService;

public interface UserService extends IService<UserDto> {
    UserDto getByLogin(String username);
    UserDto login(LoginDto dto);
}
