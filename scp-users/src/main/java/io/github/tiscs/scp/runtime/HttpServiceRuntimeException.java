package io.github.tiscs.scp.runtime;

import org.springframework.http.HttpStatus;

public class HttpServiceRuntimeException extends RuntimeException {
    private final HttpStatus status;
    private final String error;
    private final Object errorDescription;

    public HttpServiceRuntimeException(HttpStatus status) {
        this(status, status.getReasonPhrase(), null, null);
    }

    public HttpServiceRuntimeException(Throwable throwable) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null, throwable);
    }

    public HttpServiceRuntimeException(String error) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, error, null, null);
    }

    public HttpServiceRuntimeException(HttpStatus status, String error) {
        this(status, error, null, null);
    }

    public HttpServiceRuntimeException(String error, String description) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, error, description, null);
    }

    public HttpServiceRuntimeException(HttpStatus status, String error, String description) {
        this(status, error, description, null);
    }

    public HttpServiceRuntimeException(HttpStatus status, String error, String description, Throwable throwable) {
        super(description, throwable);
        this.status = status;
        this.error = error;
        this.errorDescription = description;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Object getErrorDescription() {
        return errorDescription;
    }
}
