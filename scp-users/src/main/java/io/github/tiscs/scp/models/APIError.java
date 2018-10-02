package io.github.tiscs.scp.models;

public class APIError {
    public APIError(String error) {
        this.error = error;
    }

    public APIError(String error, Object errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    private String error;

    private Object errorDescription;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(Object errorDescription) {
        this.errorDescription = errorDescription;
    }
}
