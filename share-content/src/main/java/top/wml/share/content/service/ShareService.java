package top.wml.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import top.wml.share.common.resp.CommonResp;
import top.wml.share.content.domain.dto.ExchangeDTO;
import top.wml.share.content.domain.dto.ShareRequestDTO;
import top.wml.share.content.domain.dto.UserAddBonusMsgDTO;
import top.wml.share.content.domain.entity.MidUserShare;
import top.wml.share.content.domain.entity.Share;
import top.wml.share.content.domain.entity.User;
import top.wml.share.content.domain.resp.ShareResp;
import top.wml.share.content.feign.UserService;
import top.wml.share.content.mapper.MidUserShareMapper;
import top.wml.share.content.mapper.ShareMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareService {
    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserService userService;

    @Resource
    private MidUserShareMapper midUserShareMapper;

    public List<Share> getList(String title,Integer pageNo,Integer pageSize,Long userId){
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Share::getId);
        if(title != null){
            wrapper.like(Share::getTitle,title);
        }
        wrapper.eq(Share::getAuditStatus,"PASS").eq(Share::getShowFlag,true);
        Page<Share> page = Page.of(pageNo,pageSize);
//        shareMapper.selectList(wrapper);

        List<Share> shares = shareMapper.selectList(page, wrapper);
//        List<Share> shares = shareMapper.selectList(wrapper);

        List<Share> sharesDeal;
        if(userId == null){
            sharesDeal = shares.stream().peek(share -> share.setDownloadUrl(null)).collect(Collectors.toList());
        }else{
            sharesDeal = shares.stream().peek(share -> {
                MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>().lambda()
                        .eq(MidUserShare::getUserId, userId)
                        .eq(MidUserShare::getShareId, share.getId()));
                if(midUserShare == null){
                    share.setDownloadUrl(null);
                }
            }).collect(Collectors.toList());
        }
        return sharesDeal;

    }
    public ShareResp findById(Long shareId){
        Share share = shareMapper.selectById(shareId);
        CommonResp<User> commonResp = userService.getUser(share.getUserId());
        return ShareResp.builder()
                .share(share)
                .nickname(commonResp.getData().getNickname())
                .avatarUrl(commonResp.getData().getAvatarUrl())
                .build();

    }

    public Share exchange(ExchangeDTO exchangeDTO){
        Long userId = exchangeDTO.getUserId();
        Long shareId = exchangeDTO.getShareId();
        Share share = shareMapper.selectById(shareId);
        if(share == null){
            throw new IllegalArgumentException("该分享不存在！");
        }

        MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>().lambda()
                .eq(MidUserShare::getUserId, userId)
                .eq(MidUserShare::getShareId, shareId));
        if(midUserShare != null){
            return share;
        }

        CommonResp<User> commonResp = userService.getUser(userId);
        User user = commonResp.getData();

        Integer price = share.getPrice();

        if(price > user.getBonus()){
            throw new IllegalArgumentException("用户积分不够");
        }
        userService.updateBonus(UserAddBonusMsgDTO.builder().userId(userId).bonus(price * -1).build());

        midUserShareMapper.insert(MidUserShare.builder().userId(userId).shareId(shareId).build());
        return share;

    }
    public int contribute(ShareRequestDTO shareRequestDTO){
        Share share = Share.builder()
                .isOriginal(shareRequestDTO.getIsOriginal())
                .author(shareRequestDTO.getAuthor())
                .price(shareRequestDTO.getPrice())
                .downloadUrl(shareRequestDTO.getDownloadUrl())
                .summary(shareRequestDTO.getSummary())
                .buyCount(0)
                .title(shareRequestDTO.getTitle())
                .userId(shareRequestDTO.getUserId())
                .cover(shareRequestDTO.getCover())
                .createTime(new Date())
                .updateTime(new Date())
                .showFlag(false)
                .auditStatus("NOT_YET")
                .reason("未审核")
                .build();
        return shareMapper.insert(share);
    }

    public List<Share> myContribute(Integer pageNo,Integer pageSize, Long userId){
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Share::getId);
        wrapper.eq(Share::getUserId,userId);
        Page<Share> page = Page.of(pageNo,pageSize);
        return shareMapper.selectList(page,wrapper);

    }

}
