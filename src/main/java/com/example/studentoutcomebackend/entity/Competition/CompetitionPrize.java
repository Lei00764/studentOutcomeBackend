package com.example.studentoutcomebackend.entity.Competition;

import lombok.Getter;

@Getter
public class CompetitionPrize {

    private int id;

    private int term_id;

    private String prize_name;  // 如一等奖、二等奖

    private int prize_order;  // 奖的含金量

}
