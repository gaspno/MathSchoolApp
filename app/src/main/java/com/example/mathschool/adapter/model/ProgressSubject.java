package com.example.mathschool.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProgressSubject {

    private int lessonsConclude;
    private int totalLessons;
    private int testConclude;
    private int totalTest;
    private String urlImage;
    private String subjectName;

}
