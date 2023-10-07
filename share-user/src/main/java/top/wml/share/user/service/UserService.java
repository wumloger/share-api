package top.wml.share.user.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.wml.share.user.mapper.UserMapper;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Long count(){
        return userMapper.selectCount(null);
    }

}
