package com.example.mathschool.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ClassData {

        private String name;
        private List<String> content_urls;
        private int id;


}
