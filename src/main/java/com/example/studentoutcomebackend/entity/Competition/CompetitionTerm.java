package com.example.studentoutcomebackend.entity.Competition;

import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompetitionTerm {

    @Autowired
    private CompetitionMapper competitionMapper;
    private static CompetitionTerm me;

    @PostConstruct
    private void init() {
        me = this;
        me.competitionMapper = this.competitionMapper;
    }

    private int id;

    private String term_name;

    private int competition_id;

    private int level_id;  // 赛事的等级 id，每届可能不一样

    private String organizer;  // 同一赛事的组织者，每届可能不一样

    // 自定义属性开始
    private CompetitionLevel cachedLevel = null;


    public int getId() {
        return id;
    }

    public String getName() {
        return term_name;
    }

    public int getCompetition_id() {
        return competition_id;
    }

    public int getLevel_id() {
        return level_id;
    }

    public String getOrganizer() {
        return organizer;
    }

    public CompetitionLevel getLevel(){
        if(cachedLevel == null)
            cachedLevel = me.competitionMapper.selectLevelNameByLevelId(level_id);
        return cachedLevel;
    }
}
