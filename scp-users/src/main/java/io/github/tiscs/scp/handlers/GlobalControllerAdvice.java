package io.github.tiscs.scp.handlers;

import io.github.tiscs.scp.models.APIError;
import io.github.tiscs.scp.runtime.HttpServiceRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerAdvice {
    @ExceptionHandler(value = {HttpServiceRuntimeException.class})
    @ResponseBody
    public ResponseEntity<APIError> HandleHttpServiceRuntimeException(HttpServiceRuntimeException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new APIError(ex.getError(), ex.getErrorDescription()));
    }
}
