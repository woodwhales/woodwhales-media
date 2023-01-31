package cn.woodwhales.media.aop;

import cn.woodwhales.common.model.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author woodwhales on 2023-01-31 14:10
 */
@Slf4j
@ControllerAdvice
public class ControllerException {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespVO<Void> exception(Exception exception) {
        log.error("{}", exception.getMessage(), exception);
        return RespVO.errorWithErrorMsg(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RespVO<Void> exception(MethodArgumentNotValidException exception) {
        log.error("{}", exception.getMessage(), exception);
        String errorMessage = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return RespVO.errorWithErrorMsg(errorMessage);
    }

}
