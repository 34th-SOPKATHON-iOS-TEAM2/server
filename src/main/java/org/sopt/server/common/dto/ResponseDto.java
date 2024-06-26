package org.sopt.server.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;
import org.sopt.server.exception.CommonException;
import org.sopt.server.exception.NotFoundException;
import org.sopt.server.exception.dto.ErrorCode;
import org.sopt.server.exception.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public record ResponseDto<T>(
        @JsonIgnore HttpStatus httpStatus,
        @NonNull Integer status,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Nullable T data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Nullable ExceptionDto exception
) {

    public static <T> ResponseDto<T> success(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK, HttpStatus.OK.value(), data, null);
    }

    public static ResponseDto<?> fail(@NonNull final HttpStatus httpStatus, @NonNull final CommonException e) {
        return new ResponseDto<>(httpStatus, httpStatus.value(), null, ExceptionDto.from(e.getErrorCode()));
    }

    public static ResponseDto<?> fail(@NonNull final HttpStatus httpStatus, @NonNull final NotFoundException e) {
        return new ResponseDto<>(httpStatus, httpStatus.value(), null, ExceptionDto.from(e.getErrorCode()));
    }

    public static ResponseDto<?> fail(@NonNull final MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, ExceptionDto.from(
                ErrorCode.BAD_REQUEST));
    }

    public static ResponseDto<?> fail(@NonNull final MethodArgumentTypeMismatchException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, ExceptionDto.from(
                ErrorCode.INTERNAL_SERVER_ERROR));
    }

    public static ResponseDto<?> fail(@NonNull final MissingServletRequestParameterException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, ExceptionDto.from(
                ErrorCode.BAD_REQUEST));
    }

    public static ResponseDto<?> fail(@NonNull final Exception e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), null, ExceptionDto.from(
                ErrorCode.BAD_REQUEST));
    }
}
