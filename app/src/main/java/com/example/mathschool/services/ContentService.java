package com.example.mathschool.services;

import com.example.mathschool.model.SchoolYear;
import com.example.mathschool.model.Subject;
import com.example.mathschool.provider.ContentProvider;

import java.io.IOException;
import java.util.List;

//provide data of years and subject and class needed to app be functionally
public class ContentService {

    public static List<Subject> getSubjects(String year) throws IOException {
        return ContentProvider.getSubjects(year);
    }
    public static List<SchoolYear> getYears() throws IOException {
        return ContentProvider.getYears();
    }

}
