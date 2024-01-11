package com.example.studentoutcomebackend.entity.Competition;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.service.CompetitionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompetitionTeam {

    private int id;
    private int competition_id;

    private int term_id;

    private int prize_id;

    private String award_date;

    private int verify_status;

    private String image_id;

    private String description;

    @Autowired
    private CompetitionMapper competitionMapper;
    @Autowired
    private CompetitionService competitionService;
    private static CompetitionTeam me;
    private Competition cachedCompetition = null;
    private CompetitionTerm cachedCompetitionTerm = null;
    private CompetitionPrize cachedCompetitionPrize = null;

    private List<CompetitionTeamStudent> cachedCompetitionTeamStudentList = null;

    @PostConstruct
    private void init() {
        me = this;
        me.competitionMapper = this.competitionMapper;
        me.competitionService = this.competitionService;
    }

    public int getId() {
        return id;
    }

    public Competition getCompetition() {
        if(cachedCompetition == null)
            cachedCompetition = me.competitionMapper.selectCompetitionInfoByCompetitionId(competition_id);
        return cachedCompetition;
    }

    public CompetitionTerm getTerm() {
        if(cachedCompetitionTerm == null)
            cachedCompetitionTerm = me.competitionMapper.selectTermInfoByTermId(term_id);
        return cachedCompetitionTerm;
    }

    public CompetitionPrize getPrize() {
        if(cachedCompetitionPrize == null)
            cachedCompetitionPrize = me.competitionMapper.selectPrizeInfoByPrizeId(prize_id);
        return cachedCompetitionPrize;
    }

    public String getAwardDate() {
        return award_date;
    }

    public int getVerifyStatus() {
        return verify_status;
    }

    public String getImageId() {
        return image_id;
    }

    public String getDescription() {
        return description;
    }


    public List<CompetitionTeamStudent> getCompetitionTeamStudentList(){
        if(cachedCompetitionTeamStudentList == null){
            cachedCompetitionTeamStudentList = me.competitionMapper.selectTeamCompetitionTeamStudentByTeamId(id);
        }
        return cachedCompetitionTeamStudentList;
    }

    public void editBasicInfo(int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc) {
        // 检查是否匹配
        me.competitionService.checkCompetition(newCompetitionId, newTermId, newPrizeId);
        me.competitionMapper.updateTeamBasicInfo(id, newCompetitionId, newTermId, newPrizeId, newAwardDate, newDesc);
        competition_id = newCompetitionId;
        term_id = newTermId;
        prize_id = newPrizeId;
        award_date = newAwardDate;
        description = newDesc;

        cachedCompetition = null;
        cachedCompetitionPrize = null;
        cachedCompetitionTerm = null;

    }

    public void addMember(StudentInfo newStudent){
        me.competitionMapper.insertCompetitionTeamStudent(id, newStudent.getUser_id());
    }
    
}
