package com.example.mathschool.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Subject {

    private String name;
    private String imageUrl;
    private SchoolYear schoolYearEntity;
    private List<Test> testEntitySet;
    private List<Lesson> classEntitySet;

}
