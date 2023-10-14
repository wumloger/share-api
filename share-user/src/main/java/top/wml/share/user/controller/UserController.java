package top.wml.share.user.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.user.domain.dto.LoginDTO;
import top.wml.share.user.domain.dto.UserAddBonusMsgDTO;
import top.wml.share.user.domain.entity.BonusEventLog;
import top.wml.share.user.domain.entity.User;
import top.wml.share.user.resp.UserLoginResp;
import top.wml.share.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/count")
    public CommonResp<Long> count(){
        Long count = userService.count();
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(count);
        return commonResp;
    }

    @PostMapping ("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody LoginDTO loginDTO){

        UserLoginResp userLoginResp = userService.login(loginDTO);
        CommonResp<UserLoginResp> commonResp = new CommonResp<>();
        commonResp.setData(userLoginResp);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody LoginDTO loginDTO){
        Long id = userService.register(loginDTO);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(id);
        return commonResp;
    }

    @GetMapping("/{id}")
    public CommonResp<User> getUserById(@PathVariable Long id){
        User user = userService.findById(id);
        CommonResp<User> commonResp = new CommonResp<>();
        commonResp.setData(user);
        return commonResp;
    }

    @PutMapping("/update-bonus")
    public CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO){
        Long userId = userAddBonusMsgDTO.getUserId();
        userService.updateBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(userAddBonusMsgDTO.getBonus())
                        .description("兑换分享")
                        .event("BUY")
                        .build()
        );
        CommonResp<User> commonResp = new CommonResp<>();
        commonResp.setData(userService.findById(userId));
        return commonResp;
    }

    //分页查询日志
    @GetMapping("/logs/{id}/{pageNo}/{pageSize}")
    public CommonResp<List<BonusEventLog>> getLogs(@PathVariable Long id,@PathVariable int pageNo,@PathVariable int pageSize){
        System.out.println(id + "" + pageNo + "" + pageSize);
        List<BonusEventLog> bonusEventLogs = userService.bonusLogs(id, pageNo, pageSize);
        CommonResp<List<BonusEventLog>> commonResp = new CommonResp<>();
        commonResp.setData(bonusEventLogs);
        return commonResp;
    }
}
