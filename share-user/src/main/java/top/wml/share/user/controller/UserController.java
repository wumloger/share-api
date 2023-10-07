package top.wml.share.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.wml.share.user.domain.dto.LoginDTO;
import top.wml.share.user.domain.entity.User;
import top.wml.share.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/count")
    public Long count(){
        return userService.count();
    }

    @PostMapping ("/login")
    public User login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
}
