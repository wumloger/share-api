package top.wml.share.common.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.wml.share.common.exception.BusinessException;
import top.wml.share.common.resp.CommonResp;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(Exception e) throws Exception{
        CommonResp<?> resp = new CommonResp<>();
        log.error("系统异常",e);
        resp.setSuccess(false);
        resp.setMessage(e.getMessage());
        return resp;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BusinessException e) throws Exception{
        CommonResp<?> resp = new CommonResp<>();
        log.error("业务异常",e);
        resp.setSuccess(false);
        resp.setMessage(e.getE().getDesc());
        return resp;
    }
}
