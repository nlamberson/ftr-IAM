package com.ftr.iam.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Integer status = HttpStatus.OK.value();
    private String message = "Successful response";
    private String responseCode = HttpStatus.OK.name();
    private List<ErrorResponse> errors;

    public void addError(ErrorResponse errorResponse) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }

        this.errors.add(errorResponse);
    }
}
