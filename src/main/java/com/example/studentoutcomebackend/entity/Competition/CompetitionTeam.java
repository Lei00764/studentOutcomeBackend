package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

@Getter
public class CompetitionTeam {

    private int id;

    private int competition_id;

    private int term_id;

    private int prize_id;

    private String award_date;

    private int verify_status;

    private String image_id;

    private String description;
    
}
