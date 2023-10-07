package top.wml.share.common.exception;

import lombok.Getter;

@Getter
public class BusinessException  extends RuntimeException{
    private BusinessExceptionEnum e;

    public BusinessException(BusinessExceptionEnum e) {
        this.e = e;
    }

    public void setE(BusinessExceptionEnum e) {
        this.e = e;
    }
}
