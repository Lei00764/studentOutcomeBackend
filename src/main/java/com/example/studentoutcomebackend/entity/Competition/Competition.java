package com.example.studentoutcomebackend.entity.Competition;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
public class Competition implements Serializable {

    @Autowired
    private CompetitionMapper competitionMapper;
    private static Competition me;

    @PostConstruct
    private void init() {
        me = this;
        me.competitionMapper = this.competitionMapper;
    }
    private int id;
    private int term_id;
    private int type_id;
    private String competition_name;

    // 自定义属性开始
    private CompetitionType cachedType = null;

    public int getId() {
        return id;
    }

    public int getTerm_id() {
        return term_id;
    }

    public CompetitionType getType() {
        if(cachedType == null)
            cachedType = me.competitionMapper.selectTypeInfoByTypeId(type_id);
        return cachedType;
    }



    public String getCompetitionName() {
        return competition_name;
    }

    public static Competition create(){
        return null;
    }

    public void duplicateLastTerm(){

    }

    public void delete(){

    }

}
