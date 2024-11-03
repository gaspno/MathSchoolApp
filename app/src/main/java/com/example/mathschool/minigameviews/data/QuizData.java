package com.example.mathschool.minigameviews.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class QuizData {

    private String[] question;
    private int indexAnswer;
    private String statement;



}
