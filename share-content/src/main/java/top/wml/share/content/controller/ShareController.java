package top.wml.share.content.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.wml.share.common.resp.CommonResp;
import top.wml.share.common.util.JwtUtil;
import top.wml.share.content.domain.dto.ExchangeDTO;
import top.wml.share.content.domain.dto.ShareRequestDTO;
import top.wml.share.content.domain.entity.Notice;
import top.wml.share.content.domain.entity.Share;
import top.wml.share.content.domain.resp.ShareResp;
import top.wml.share.content.service.NoticeService;
import top.wml.share.content.service.ShareService;

import java.util.List;

@RestController
@RequestMapping("/share")
@Slf4j
public class ShareController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private ShareService shareService;

    private final int MAX = 100;

    @GetMapping("/exchanges/{id}")
    public CommonResp<List<Share>> getMyExchanges(@PathVariable Long id){
        List<Share> myExchange = shareService.getMyExchange(id);
        CommonResp<List<Share>> resp = new CommonResp<>();
        resp.setData(myExchange);
        return resp;
    }

    @PostMapping("/contribute")
    public CommonResp<Integer> contributeShare(@RequestBody ShareRequestDTO shareRequestDTO, @RequestHeader(value = "token",required = false) String token){
        long userId = getUserIdFromToken(token);
        shareRequestDTO.setUserId(userId);
        System.out.println(shareRequestDTO);
        CommonResp<Integer> resp = new CommonResp<>();
        resp.setData(shareService.contribute(shareRequestDTO));
        return resp;
    }

    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        CommonResp<Notice> commonResp = new CommonResp<>();
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }

    @GetMapping("/list")
    public CommonResp<List<Share>> getShareList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                @RequestParam(required = false,defaultValue = "3") Integer pageSize,
                                                @RequestHeader(required = false,value = "token") String token){
        if (pageSize > MAX){
            pageSize = MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.getList(title,pageNo,pageSize,userId));
        return commonResp;
    }

    private long getUserIdFromToken(String token) {
        log.info(">>>>>>>>>>> token" + token);
        long userId = 0;
        String noToken = "no-token";
        if(!noToken.equals(token)){
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            log.info("解析到 token 的json 数据为:{}",jsonObject);
            userId = Long.parseLong(jsonObject.get("id").toString());
        }else{
            log.info("没有 token");
        }
        return userId;
    }

    @GetMapping("/{id}")
    public CommonResp<ShareResp> getShareById(@PathVariable Long id){
        ShareResp shareResp = shareService.findById(id);
        CommonResp<ShareResp> commonResp = new CommonResp<>();
        commonResp.setData(shareResp);
        return commonResp;
    }

    @PostMapping("/exchange")
    public CommonResp<Share> exchage(@RequestBody ExchangeDTO exchangeDTO){
        System.out.println(exchangeDTO);
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.exchange(exchangeDTO));
        return commonResp;
    }

    @GetMapping("/my-contribute")
    public CommonResp<List<Share>> myContribute(
           @RequestParam(required = false,defaultValue = "1") Integer pageNo,
            @RequestParam(required = false,defaultValue = "8") Integer pageSize,
            @RequestHeader(value = "token",required = false) String token
    ){
        if(pageSize > MAX){
            pageSize = MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.myContribute(pageNo,pageSize,userId));
        return commonResp;
    }

}
