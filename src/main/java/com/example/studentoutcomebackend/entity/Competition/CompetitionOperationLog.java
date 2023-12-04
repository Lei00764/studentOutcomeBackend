package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

@Getter
public class CompetitionOperationLog {

    private int id;

    private int team_id;

    private String operation_time;

    private String operation_text;

    private int operation_level;

}
