package top.wml.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import top.wml.share.content.domain.entity.Notice;
import top.wml.share.content.mapper.NoticeMapper;

import java.util.List;

@Service
@RefreshScope
public class NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    @Value("${enableNotice}")
    private boolean enableNotice;
    public Notice getLatest(){
        if(enableNotice){
            LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Notice::getShowFlag,1);
            wrapper.orderByDesc(Notice::getId);
            List<Notice> noticeList = noticeMapper.selectList(wrapper);
            return noticeList.get(0);
        }else{
            return Notice.builder()
                    .content("系统正在维护中...")
                    .build();
        }
    }
}
