package pl.pbrodziak.education.controller;

import org.springframework.web.bind.annotation.*;
import pl.pbrodziak.education.entity.user.User;
import pl.pbrodziak.education.entity.dto.UserDto;
import pl.pbrodziak.education.service.UserService;

import java.util.List;


@RestController
@RequestMapping("user")
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("add")
    public User addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

}
