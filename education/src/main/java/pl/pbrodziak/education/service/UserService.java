package pl.pbrodziak.education.service;


import pl.pbrodziak.education.entity.User;
import pl.pbrodziak.education.entity.dto.UserDto;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User addUser(UserDto userDto);
}
