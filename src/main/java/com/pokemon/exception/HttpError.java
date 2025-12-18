package com.pokemon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpError {

    private int status;

    private String error;

    private String message;
}
