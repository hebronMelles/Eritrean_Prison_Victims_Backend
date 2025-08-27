package Eritrean.Prison.Victims.DTOMapper;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Entity.UserDto;
import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Entity.UserFormDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component
public class DtoMapper {
    public UserDto userToConceleadDto(User user) {
        UserDto userDto = new UserDto();
        if (user.isDisplay()) {
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPhotoUrl(user.getPhotoUrl());
            userDto.setPhone(user.getPhone());
        } else {
            userDto.setFirstName("Unavailable");
            userDto.setLastName("Unavailable");
            userDto.setEmail("Unavailable");
            userDto.setPhotoUrl("Unavailable");
            userDto.setPhone("Unavailable");
        }
        userDto.setUserForms(userFormListToDtoList(user.getUserForms()));

        return userDto;
    }

    public UserFormDto userFormToDto(UserForm userForm) {
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setDescription(userForm.getDescription());
        userFormDto.setLocation(userForm.getLocation());
        userFormDto.setEndDate(userForm.getEndDate());
        userFormDto.setStartDate(userForm.getStartDate());
        userFormDto.setNumberOfYears(calculateNumberOfYears(userForm.getStartDate().atStartOfDay(), userForm.getEndDate().atStartOfDay()));
        return userFormDto;
    }

    public List<UserFormDto> userFormListToDtoList(List<UserForm> userForms) {
        List<UserFormDto> userFormDtosList = new ArrayList<>();
        for (UserForm userForm : userForms) {
            userFormDtosList.add(userFormToDto(userForm));
        }
        return userFormDtosList;
    }

    private String calculateNumberOfYears(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDate endDateLocal = endDate.toLocalDate();
        LocalDate startDateLocal = startDate.toLocalDate();
        Period period = Period.between(startDateLocal, endDateLocal);
        return String.format("%d years, %d months, %d days", period.getYears(), period.getMonths(), period.getDays());
    }
}
