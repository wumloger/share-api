package top.wml.share.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String phone;
    private String password;
    private String nickname;
    private String rules;
    private String avatarUrl;
    private Integer bonus;
    private Date createTime;
    private Date updateTime;
}
