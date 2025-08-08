package com.ftr.iam.controller;

import com.ftr.iam.controller.response.ErrorResponse;
import com.ftr.iam.controller.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class BaseController {

    private static final Logger LOGGER = LogManager.getLogger(BaseController.class);

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected Response handleException(Exception e) {
        Response response = new Response();
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Uncaught general exception");
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.addError(errorResponse);

        return response;
    }

    // TODO: Add more exceptions as needed
}
