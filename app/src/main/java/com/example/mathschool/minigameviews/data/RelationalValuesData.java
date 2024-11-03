package com.example.mathschool.minigameviews.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RelationalValuesData {

    private String[] column1;
    private String[] column2;
    private int[] secondColumnRelation;



}
