package com.example.mathschool.provider;

import com.example.mathschool.model.SchoolYear;
import com.example.mathschool.model.Student;
import com.example.mathschool.model.StudentRegister;
import com.example.mathschool.model.Subject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class  ContentProvider {

    private static final String baseUrlContent="https://ec2-3-147-26-211.us-east-2.compute.amazonaws.com:8080/summary/";

    public static List<SchoolYear> getYears() throws IOException {
        List<SchoolYear> years;
        URL url = new URL(baseUrlContent+"years");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Type yearListType = new TypeToken<List<SchoolYear>>(){}.getType();
            years=gson.fromJson(response.toString(), yearListType);
            return years;
        }
        return null;
    }


    public static List<Subject> getSubjects(String year) throws IOException {
        List<Subject> subjects;
        URL url = new URL(baseUrlContent+year+"/subject");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Type subjectListType = new TypeToken<List<Subject>>(){}.getType();
            subjects=gson.fromJson(response.toString(), subjectListType);
            return subjects;
        }
        return null;
    }
    public static Student saveStudent(Student student,String tokenFirebase) throws IOException {
        Student s;
        URL url = new URL(baseUrlContent+"student");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Authorization",tokenFirebase);

        httpURLConnection.setDoOutput(true);

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            String jsonInputString = new Gson().toJson(student);
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Gson gson = new Gson();
            Type testListType = new TypeToken<Student>(){}.getType();
            s=gson.fromJson(response.toString(), testListType);
            return s;
        }
        return null;
    }

    public static StudentRegister getRegister(Integer id, String tokenFirebase) throws IOException {
        StudentRegister register;
        URL url = new URL(baseUrlContent+"watched_list/"+id);
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Authorization",tokenFirebase);
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Type registerType = new TypeToken<StudentRegister>(){}.getType();
            register=gson.fromJson(response.toString(), registerType);
            return register;
        }
        return null;
    }

    public static StudentRegister registerWatchedClass(Integer studentID, Integer classID, String tokenFirebase) throws IOException {
        StudentRegister register;
        URL url = new URL(baseUrlContent+"register_student_class_watched");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpURLConnection.setRequestProperty("Authorization",tokenFirebase);
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.setDoOutput(true);
        String parameters = "student_id=" + studentID + "&class_id=" + classID;

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = parameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }


        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Type registerType = new TypeToken<StudentRegister>(){}.getType();
            register=gson.fromJson(response.toString(), registerType);
            return register;
        }
        return null;
    }

    public static StudentRegister registerCompletedClass(Integer studentID, Integer testID, String tokenFirebase) throws IOException {
        StudentRegister register;
        URL url = new URL(baseUrlContent+"register_student_test_completed");
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpURLConnection.setRequestProperty("Authorization",tokenFirebase);
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        String parameters = "student_id=" + studentID + "&test_id=" + testID;

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = parameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Type registerType = new TypeToken<StudentRegister>(){}.getType();
            register=gson.fromJson(response.toString(), registerType);
            return register;
        }
        return null;
    }
}
