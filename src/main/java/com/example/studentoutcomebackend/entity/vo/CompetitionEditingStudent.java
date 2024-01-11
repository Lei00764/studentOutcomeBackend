package com.example.studentoutcomebackend.entity.vo;

import lombok.Getter;

@Getter
public class CompetitionEditingStudent {
    private int userId;
    private int order;


    public CompetitionEditingStudent(int userId, int order) {
        this.userId = userId;
        this.order = order;
    }
}
