package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

@Getter
public class CompetitionTerm {

    private int id;

    private String term_name;

    private int competition_id;

    private int level_id;  // 赛事的等级 id，每届可能不一样

    private String organizer;

}
