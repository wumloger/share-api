package top.wml.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.content.domain.entity.Share;
import top.wml.share.content.service.ShareService;

import java.util.List;

@RestController
@RequestMapping("/share/admin")
public class ShareAdminController {
    @Resource
    private ShareService shareService;

    @GetMapping(value = "/list")
    public CommonResp<List<Share>> getShareNotYet(){
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.queryShareNotYet());
        return commonResp;
    }
}
