package com.example.studentoutcomebackend.entity.Competition;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.entity.vo.CompetitionEditingStudent;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.StudentInfoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CompetitionTeamLogger {
    private CompetitionTeam team;

    private CompetitionMapper competitionMapper;
    private StudentInfoMapper studentInfoMapper;

    public CompetitionTeamLogger(CompetitionTeam team, CompetitionMapper competitionMapper, StudentInfoMapper studentInfoMapper) {
        this.team = team;
        this.competitionMapper = competitionMapper;
        this.studentInfoMapper = studentInfoMapper;
    }


    public void logEditBasicInfo(StudentInfo editor, int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc){
        Competition competition = competitionMapper.selectCompetitionInfoByCompetitionId(newCompetitionId);
        CompetitionTerm term = competitionMapper.selectTermInfoByTermId(newTermId);
        CompetitionPrize prize = competitionMapper.selectPrizeInfoByPrizeId(newPrizeId);

        log("学生" + editor.getStu_name() + "将基础信息修改为：(" +
                "竞赛名："+ competition.getCompetitionName() +
                ", 届别："+ term.getName() +
                ",奖项: " + prize.getPrize_name() +
                ", 获奖时间" + newAwardDate +
                ",描述:" + newDesc +")");
    }

    public void logEditOrder(StudentInfo editor, List<CompetitionEditingStudent> newStudents){
        StringBuilder ans = new StringBuilder("学生"  + editor.getStu_name() + "将团队的贡献顺序改为:(");
        for(CompetitionEditingStudent student: newStudents){
            ans.append(studentInfoMapper.selectUserByUserId(student.getUserId()).getStu_name()).append(",").append(student.getOrder());
        }
        ans.append(")");
        log(ans.toString());
    }

    public void logVerified(StudentInfo editor, int verified){
        if(verified != 0)
            log("学生" + editor.getStu_name() +"确认了自己的贡献顺序。");
        else
            log("学生" + editor.getStu_name() +"取消确认了自己的贡献顺序。");
    }

    public void logCreate(StudentInfo editor, int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc) {
        Competition competition = competitionMapper.selectCompetitionInfoByCompetitionId(newCompetitionId);
        CompetitionTerm term = competitionMapper.selectTermInfoByTermId(newTermId);
        CompetitionPrize prize = competitionMapper.selectPrizeInfoByPrizeId(newPrizeId);

        log("学生" + editor.getStu_name() + "创建了团队，基础信息为：(" +
                "竞赛名：" + competition.getCompetitionName() +
                ", 届别：" + term.getName() +
                ",奖项: " + prize.getPrize_name() +
                ", 获奖时间" + newAwardDate +
                ",描述:" + newDesc + ")");
    }

    public void logJoin(int newStudentUserId){
        log("学生" + studentInfoMapper.selectUserByUserId(newStudentUserId).getStu_name() + "加入了团队。");

    }

    public void logLeave(StudentInfo editor){
        log("学生" + editor.getStu_name() + "退出了团队。");

    }

    public void logSubmit(StudentInfo editor){
        log("学生" + editor.getStu_name() + "将团队信息提交审核。");
    }

    public void logWithdraw(StudentInfo editor){
        log("学生" + editor.getStu_name() + "将团队撤回了团队信息审核申请。");
    }

    public void logEditCertification(StudentInfo editor, boolean notClear){
        if(notClear)
            log("学生" + editor.getStu_name() + "修改了团队证书。");
        else
            log("学生" + editor.getStu_name() + "清除了团队证书。");

    }

    public void log(String str){
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        competitionMapper.insertCompetitionTeamLog(team.getId(), str, timeNow);
    }



}
