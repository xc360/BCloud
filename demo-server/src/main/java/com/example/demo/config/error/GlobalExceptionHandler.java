package com.example.demo.config.error;

import cn.hutool.json.JSONUtil;
import com.xc.core.enums.CoreFailCode;
import com.xc.core.exception.OperateException;
import com.xc.core.exception.ResultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * <p>全局异常处理</p>
 *
 * @author xc
 * @version v1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * <p>服务器Exception异常处理 </p>
     *
     * @return 错误信息
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exception(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        if (e.getMessage() != null && e.getMessage().contains("Incorrect string value")) {
            OperateException operate = CoreFailCode.PARAM_ERROR.getOperateException();
            return new ResultException(operate.getCode(), operate.getMessage());
        }
        OperateException operate = CoreFailCode.SERVER_EXCEPTION.getOperateException();
        ResultException resultException = new ResultException(operate.getCode(), e.getMessage());
        if (request.getHeader("Accept") != null && request.getHeader("Accept").contains(MediaType.TEXT_HTML_VALUE)) {
            return JSONUtil.toJsonStr(resultException);
        }
        return resultException;
    }

    /**
     * <p>用户操作异常处理</p>
     *
     * @return 错误信息
     */
    @ExceptionHandler(value = OperateException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exception(OperateException e, HttpServletRequest request) {
        ResultException resultException = new ResultException(e.getCode(), e.getMessage());
        if (request.getHeader("Accept") != null && request.getHeader("Accept").contains(MediaType.TEXT_HTML_VALUE)) {
            return JSONUtil.toJsonStr(resultException);
        }
        return resultException;
    }

    /**
     * 处理参数转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exception(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        OperateException operate = CoreFailCode.PARAMETER_ERROR.getOperateException();
        ResultException resultException = new ResultException(operate.getCode(), operate.getMessage());
        if (request.getHeader("Accept") != null && request.getHeader("Accept").contains(MediaType.TEXT_HTML_VALUE)) {
            return JSONUtil.toJsonStr(resultException);
        }
        return resultException;
    }
}
