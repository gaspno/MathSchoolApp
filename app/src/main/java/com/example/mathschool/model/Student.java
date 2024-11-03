package com.example.mathschool.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Student {
    private Integer id;
    private String name;
    private String firebase_id;
}
