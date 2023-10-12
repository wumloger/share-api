package top.wml.share.content.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wml.share.content.domain.entity.Share;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareResp {
    private Share share;

    private String nickname;

    private String avatarUrl;
}
