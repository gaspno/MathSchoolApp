package com.example.mathschool.minigameviews.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ResolutionValuesData {
    private String[] column1;
    private String[] column2;
    private String[] column3;
    private String[] equations;
    private String[] solutions;
    private String correct;
    private int position;
}
