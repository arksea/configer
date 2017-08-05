package net.arksea.config.server.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * 处理Restful异常.
 */
@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);
    /**
     * 处理RestException.
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {RestException.class})
    public final ResponseEntity<?> handleRestException(final RestException ex, final WebRequest request) {
        return handle(ex, ex.getStatus(), request, ex.getLabel(), ex.getDetail());
    }

    @ExceptionHandler(value = {BindException.class})
    public final ResponseEntity<?> handleException(final BindException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<?> handleException(final Exception ex, final WebRequest request) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ConversionNotSupportedException.class})
    public final ResponseEntity<?> handleException(final ConversionNotSupportedException ex, final WebRequest request) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {HttpMediaTypeNotAcceptableException.class})
    public final ResponseEntity<?> handleException(final HttpMediaTypeNotAcceptableException ex, final WebRequest request) {
        return handle(ex, HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public final ResponseEntity<?> handleException(final HttpMediaTypeNotSupportedException ex, final WebRequest request) {
        return handle(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public final ResponseEntity<?> handleException(final HttpMessageNotReadableException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {HttpMessageNotWritableException.class})
    public final ResponseEntity<?> handleException(final HttpMessageNotWritableException ex, final WebRequest request) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public final ResponseEntity<?> handleException(final HttpRequestMethodNotSupportedException ex, final WebRequest request) {
        return handle(ex, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public final ResponseEntity<?> handleException(final MethodArgumentNotValidException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {MissingPathVariableException.class})
    public final ResponseEntity<?> handleException(final MissingPathVariableException ex, final WebRequest request) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public final ResponseEntity<?> handleException(final MissingServletRequestParameterException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    public final ResponseEntity<?> handleException(final MissingServletRequestPartException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public final ResponseEntity<?> handleException(final NoHandlerFoundException ex, final WebRequest request) {
        return handle(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {NoSuchRequestHandlingMethodException.class})
    public final ResponseEntity<?> handleException(final NoSuchRequestHandlingMethodException ex, final WebRequest request) {
        return handle(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {TypeMismatchException.class})
    public final ResponseEntity<?> handleException(final TypeMismatchException ex, final WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    public final ResponseEntity<?> handle(final Exception ex, final HttpStatus status, final WebRequest request) {
        return handle(ex,status,request,"","");
    }
    public final ResponseEntity<?> handle(final Exception ex, final HttpStatus status, final WebRequest request,
                                          final String label, final String extDetail) {
        final StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(label)) {
            sb.append("[+::"+request.getDescription(false)+"]{-+5}");
        } else {
            sb.append(label);
        }
        if (StringUtils.isEmpty(ex.getMessage())) {
            sb.append(ex.getClass().getSimpleName());
        } else {
            sb.append(ex.getMessage());
        }
        sb.append("\nstatus: ").append(status.toString());
        if (!StringUtils.isEmpty(extDetail)) {
            sb.append("\n").append(extDetail);
        }
        sb.append("\n");
        RestUtils.fillRequestLogInfo(sb, request);
        String alarmMsg = sb.toString();
        if (status.equals(HttpStatus.BAD_REQUEST) || "[=::]".equals(label)) {
            LOGGER.debug(alarmMsg, ex);
        } else {
            LOGGER.warn(alarmMsg, ex);
        }
        final String reqid = (String) request.getAttribute("x-requestid", WebRequest.SCOPE_REQUEST);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        String body = makeErrorResult(status.value(), ex, reqid);
        return new ResponseEntity<Object>(body, headers, status);
    }

    public static String makeErrorResult(int errCode, Throwable ex, String reqid) {
        String msg = ex.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            msg = msg.replace("\"", "'").replace("\n", "\\n");
        }
        final StringBuilder body = new StringBuilder();
        body.append("{\"code\":").append(errCode)
                .append(", \"msg\":\"").append(msg)
                .append("\",\"reqid\":\"").append(reqid).append("\"}");
        return body.toString();
    }
}
