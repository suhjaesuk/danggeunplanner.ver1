package com.finalteam4.danggeunplanner.calendar.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ColorStageResponse {
    private final List<String> colorStage1=new ArrayList<>();
    private final List<String> colorStage2=new ArrayList<>();
    private final List<String> colorStage3=new ArrayList<>();
    private final List<String> colorStage4=new ArrayList<>();

    public void updateColorStage(String date, int carrot) {
        if (carrot <= 4) {
            colorStage1.add(date);
        } else if (carrot <= 8) {
            colorStage2.add(date);
        } else if (carrot <= 12) {
            colorStage3.add(date);
        } else {
            colorStage4.add(date);
        }
    }
}
