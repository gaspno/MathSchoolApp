package com.example.mathschool.services;

import com.example.mathschool.model.Student;
import com.example.mathschool.model.StudentRegister;
import com.example.mathschool.provider.ContentProvider;

import java.io.IOException;

public class ProgressService {

    public static StudentRegister getStudentRegister(Integer Id, String firebaseToken) throws IOException {
        return ContentProvider.getRegister(Id,firebaseToken);
    }
    public static Student saveStudent(Student student,String tokenFirebase) throws IOException {
        return ContentProvider.saveStudent(student,tokenFirebase);
    }
    public static StudentRegister registerWatchedClass(Integer studentID,Integer classID,String token) throws IOException {
        return ContentProvider.registerWatchedClass(studentID,classID,token);
    }
    public static StudentRegister registerCompletedTest(Integer studentID,Integer testID,String token) throws IOException {
        return ContentProvider.registerCompletedClass(studentID,testID,token);
    }
}
