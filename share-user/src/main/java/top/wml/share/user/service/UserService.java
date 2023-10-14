package top.wml.share.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wml.share.common.exception.BusinessException;
import top.wml.share.common.exception.BusinessExceptionEnum;
import top.wml.share.common.util.JwtUtil;
import top.wml.share.common.util.SnowUtil;
import top.wml.share.user.domain.dto.LoginDTO;

import top.wml.share.user.domain.dto.UserAddBonusMsgDTO;
import top.wml.share.user.domain.entity.BonusEventLog;
import top.wml.share.user.domain.entity.User;
import top.wml.share.user.mapper.BonusEventLogMapper;
import top.wml.share.user.mapper.UserMapper;
import top.wml.share.user.resp.UserLoginResp;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO){
        System.out.println(userAddBonusMsgDTO);
        Long userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();
        User user = userMapper.selectById(userId);
        user.setBonus(user.getBonus() + bonus);

        userMapper.update(user,new QueryWrapper<User>().lambda().eq(User::getId,userId));

        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .description(userAddBonusMsgDTO.getDescription())
                        .event(userAddBonusMsgDTO.getEvent())
                        .createTime(new Date())
                        .build()
        );
        log.info("积分添加完毕...");
    }

    public Long count(){
        return userMapper.selectCount(null);
    }

    public UserLoginResp login(LoginDTO loginDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(User::getPhone,loginDTO.getPhone());
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB == null){
           throw new BusinessException(BusinessExceptionEnum.PHONE_NOT_EXIST);
       }
       if(!userDB.getPassword().equals(loginDTO.getPassword())){
           throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR);
       }
        UserLoginResp userLoginResp = UserLoginResp.builder()
                .user(userDB)
                .build();
//       String key = "wml";
//       Map<String ,Object> map = BeanUtil.beanToMap(userLoginResp);
//       String token = JWTUtil.createToken(map,key.getBytes());
        String token = JwtUtil.createToken(userLoginResp.getUser().getId(), userLoginResp.getUser().getPhone());
        userLoginResp.setToken(token);
       return userLoginResp;

    }

    public Long register(LoginDTO loginDTO){
        User userDB = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));
        if(userDB != null){
             throw new BusinessException(BusinessExceptionEnum.PHONE_EXIST);
        }
        User savedUser = User.builder()
                .id(SnowUtil.getSnowflakeNextId())
                .phone(loginDTO.getPhone())
                .password(loginDTO.getPassword())
                .nickname("新用户")
                .roles("user")
                .avatarUrl("https://i2.100024.xyz/2023/01/26/3exzjl.webp")
                .bonus(100)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        userMapper.insert(savedUser);
        return savedUser.getId();
    }

    public User findById(Long userId){
        return userMapper.selectById(userId);
    }

    public List<BonusEventLog> bonusLogs(Long id,int pageNo,int pageSize){
        LambdaQueryWrapper<BonusEventLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BonusEventLog::getUserId,id);
        wrapper.orderByDesc(BonusEventLog::getCreateTime);
        Page<BonusEventLog> page = Page.of(pageNo,pageSize);
        return bonusEventLogMapper.selectList(page,wrapper);
    }

}
