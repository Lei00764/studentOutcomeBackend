package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

@Getter
public class CompetitionTeamStudent {

    private int team_id;

    private int user_id;
    private String stu_id;
    private String stu_name;
    private int order;

    private boolean verified;

}
