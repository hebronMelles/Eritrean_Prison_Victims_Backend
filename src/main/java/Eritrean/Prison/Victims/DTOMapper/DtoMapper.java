package Eritrean.Prison.Victims.DTOMapper;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Entity.UserDto;
import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Entity.UserFormDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoMapper {
    public UserDto userToConceleadDto(User user) {
      UserDto userDto = new UserDto();
      userDto.setFirstName(user.getFirstName());
      userDto.setLastName(user.getLastName());
      userDto.setEmail(user.getEmail());
      userDto.setUserForms(userFormListToDtoList(user.getUserForms()));
      userDto.setPhotoUrl(user.getPhotoUrl());
      userDto.setPhone(user.getPhone());
      return userDto;
    }
    public UserFormDto userFormToDto(UserForm userForm) {
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setDescription(userForm.getDescription());
        userFormDto.setLocation(userForm.getLocation());
        userFormDto.setEndDate(userForm.getEndDate());
        userFormDto.setStartDate(userForm.getStartDate());
        return userFormDto;
    }
    public List<UserFormDto> userFormListToDtoList(List<UserForm> userForms) {
        List<UserFormDto> userFormDtosList = new ArrayList<>();
        for (UserForm userForm : userForms) {
            userFormDtosList.add(userFormToDto(userForm));
        }
        return userFormDtosList;
    }
}
