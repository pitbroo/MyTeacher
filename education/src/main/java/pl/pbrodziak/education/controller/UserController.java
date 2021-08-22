package pl.pbrodziak.education.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pbrodziak.education.entity.User;
import pl.pbrodziak.education.service.UserService;

import java.util.List;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
