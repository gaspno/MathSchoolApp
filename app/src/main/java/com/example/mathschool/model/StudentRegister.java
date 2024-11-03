package com.example.mathschool.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudentRegister {
    private Student student;
    private List<Integer> lessonsId;
    private List<Integer> testsId;
}
