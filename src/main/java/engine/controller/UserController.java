package engine.controller;

import engine.dto.UserDto;
import engine.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public void registerUser(@Valid @RequestBody UserDto userDto) {
        userService.registerUser(userDto.getEmail(), userDto.getPassword());
    }
}
