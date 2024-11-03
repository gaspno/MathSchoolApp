package com.example.mathschool.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Question implements Serializable {
    private String enunciado;
    private String alternativa1;
    private String alternativa2;
    private String alternativa3;
    private String alternativa4;
    private int indexRespostaCorreta;
}
