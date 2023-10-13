package top.wml.share.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonusEventLog {
    private Long id;
    private Long userId;
    private Integer value;
    private String description;

    private String event;

    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private Date createTime;


}
