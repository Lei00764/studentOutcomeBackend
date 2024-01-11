package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

public class CompetitionLevel {

    public int getId() {
        return id;
    }

    private int id;

    private String level_name;

    public String getLevelName(){
        return level_name;
    }

}
