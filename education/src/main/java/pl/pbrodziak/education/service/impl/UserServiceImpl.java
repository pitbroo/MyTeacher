package pl.pbrodziak.education.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pbrodziak.education.entity.User;
import pl.pbrodziak.education.repository.UserRepository;
import pl.pbrodziak.education.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl( UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

}
