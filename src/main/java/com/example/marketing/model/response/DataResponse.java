package com.example.marketing.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse<T, D> {
    private int status;
    private String message;
    private T value;
    private int totalPage;
    private D value2;

    public DataResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DataResponse(int status, String message, T value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    public DataResponse(T value, int totalPage) {
        this.value = value;
        this.totalPage = totalPage;
    }

    public DataResponse(T value) {
        this.value = value;
    }

    public DataResponse(T value, int totalPage, D value2) {
        this.value = value;
        this.totalPage = totalPage;
        this.value2 = value2;
    }
}
