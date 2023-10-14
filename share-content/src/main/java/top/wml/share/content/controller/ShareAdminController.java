package top.wml.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.content.domain.dto.ShareAuditDTO;
import top.wml.share.content.domain.entity.Share;
import top.wml.share.content.domain.resp.ShareResp2;
import top.wml.share.content.service.ShareService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/share/admin")
public class ShareAdminController {
    @Resource
    private ShareService shareService;

    @GetMapping(value = "/list")
    public CommonResp<List<ShareResp2>> getShareNotYet(){
        CommonResp<List<ShareResp2>> commonResp = new CommonResp<>();
        List<Share> shares = shareService.queryShareNotYet();
        List<ShareResp2> list = new ArrayList<>();
        System.out.println(shares);
        shares.forEach((item)->{
            ShareResp2 shareResp2 = ShareResp2.builder()
                    .share(item)
                    .id(item.getId().toString())
                    .build();
            list.add(shareResp2);
        });
        System.out.println(list);
        commonResp.setData(list);
        return commonResp;
    }

    @PostMapping("/audit/{id}")
    public CommonResp<Share> auditById(@PathVariable Long id, @RequestBody ShareAuditDTO auditDTO){
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.auditById(id, auditDTO));
        return commonResp;
    }
}
