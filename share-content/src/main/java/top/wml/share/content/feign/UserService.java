package top.wml.share.content.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.content.domain.entity.User;

@FeignClient(value = "user-service",path = "/user")
public interface UserService {
    @GetMapping("/{id}")
    CommonResp<User> getUser(@PathVariable Long id);
}

