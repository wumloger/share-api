package top.wml.share.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.wml.share.user.domain.dto.LoginDTO;

import top.wml.share.user.domain.entity.User;
import top.wml.share.user.mapper.UserMapper;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Long count(){
        return userMapper.selectCount(null);
    }

    public User login(LoginDTO loginDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(User::getPhone,loginDTO.getPhone());
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB == null){
           throw new RuntimeException("手机号不存在");
       }
       if(!userDB.getPassword().equals(loginDTO.getPassword())){
           throw new RuntimeException("密码错误");
       }
       return userDB;

    }

}