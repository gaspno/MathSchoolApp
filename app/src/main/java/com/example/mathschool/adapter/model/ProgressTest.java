package com.example.mathschool.adapter.model;

import com.example.mathschool.model.Question;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProgressTest {
    private boolean isCompleted;
    private Integer id;
    private List<Question> questionEntitySet;
    private String subject;
}
