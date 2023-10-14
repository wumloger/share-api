package top.wml.share.content.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.wml.share.content.domain.entity.Share;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShareResp2 {
    private Share share;

    private String id;
}
