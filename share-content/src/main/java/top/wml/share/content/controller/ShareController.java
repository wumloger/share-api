package top.wml.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.content.domain.entity.Notice;
import top.wml.share.content.service.NoticeService;

@RestController
@RequestMapping("/share")
public class ShareController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        CommonResp<Notice> commonResp = new CommonResp<>();
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }

}
