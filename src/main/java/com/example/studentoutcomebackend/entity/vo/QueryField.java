package com.example.studentoutcomebackend.entity.vo;

public class QueryField {
    public String field;
    public String keyword;
    public int precise;

    public QueryField(String field, String keyword, int precise) {
        this.field = field;
        this.keyword = keyword;
        this.precise = precise;
    }



}
