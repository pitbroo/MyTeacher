package pl.pbrodziak.education.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pbrodziak.education.entity.user.User;
import pl.pbrodziak.education.entity.dto.UserDto;
import pl.pbrodziak.education.entity.dto.mapper.UserMapper;
import pl.pbrodziak.education.repository.UserRepository;
import pl.pbrodziak.education.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User addUser(UserDto userDto) {
        try {

            if ((userRepository.getUserByEmail(userDto.getEmail())) != null) {
                throw new IllegalStateException("Użytkownik o mailu" + userDto.getEmail() + "' juz jest w bazie danych.");
            }
            return userRepository.save(UserMapper.INSTANCE.userDtoToUser(userDto));
        } catch (NullPointerException e) {
            throw new NullPointerException("Wystąpił Bład");
        }
    }

}
