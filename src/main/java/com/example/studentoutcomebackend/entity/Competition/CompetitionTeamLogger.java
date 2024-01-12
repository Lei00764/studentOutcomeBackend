package com.example.studentoutcomebackend.entity.Competition;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.entity.vo.CompetitionEditingStudent;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.service.StudentInfoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CompetitionTeamLogger {
    private CompetitionTeam team;

    private CompetitionMapper competitionMapper;
    private StudentInfoService studentInfoService;
    private boolean isTeacherEditing = false;

    public void setForceEdit(boolean canForceEdit){
        this.isTeacherEditing = canForceEdit;
    }

    public CompetitionTeamLogger(CompetitionTeam team, CompetitionMapper competitionMapper, StudentInfoService studentInfoService) {
        this.team = team;
        this.competitionMapper = competitionMapper;
        this.studentInfoService = studentInfoService;
    }


    public void logEditBasicInfo(StudentInfo editor, int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc){
        Competition competition = competitionMapper.selectCompetitionInfoByCompetitionId(newCompetitionId);
        CompetitionTerm term = competitionMapper.selectTermInfoByTermId(newTermId);
        CompetitionPrize prize = competitionMapper.selectPrizeInfoByPrizeId(newPrizeId);

        log(getEditorRole() + editor.getStu_name() + "将基础信息修改为：(" +
                "竞赛名："+ competition.getCompetitionName() +
                ", 届别："+ term.getName() +
                ",奖项: " + prize.getPrize_name() +
                ", 获奖时间" + newAwardDate +
                ",描述:" + newDesc +")");
    }

    public void logEditOrder(StudentInfo editor, List<CompetitionEditingStudent> newStudents){
        StringBuilder ans = new StringBuilder(getEditorRole()  + editor.getStu_name() + "将团队的贡献顺序改为:(");
        for(CompetitionEditingStudent student: newStudents){
            ans.append(studentInfoService.selectUserByUserId(student.getUserId()).getStu_name()).append(",").append(student.getOrder());
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
        if(!isTeacherEditing)
            log("学生" + studentInfoService.selectUserByUserId(newStudentUserId).getStu_name() + "加入了团队。");
        else
            log("管理员" + studentInfoService.getCurrentUserInfo().getStu_name() + "把学生" + studentInfoService.selectUserByUserId(newStudentUserId).getStu_name() + "加入到团队中。");
    }

    public void logLeave(StudentInfo editor){
        if(!isTeacherEditing)
            log("学生" + editor.getStu_name() + "退出了团队。");
        else
            log("管理员" + studentInfoService.getCurrentUserInfo().getStu_name() + "把学生" + editor.getStu_name() + "从团队中移除。");
    }

    public void logSubmit(StudentInfo editor){
        log("学生" + editor.getStu_name() + "将团队信息提交审核。");
    }

    public void logWithdraw(StudentInfo editor){
        log("学生" + editor.getStu_name() + "将团队撤回了团队信息审核申请。");
    }

    public void logEditCertification(StudentInfo editor, boolean notClear){
        if(notClear)
            log(getEditorRole() + editor.getStu_name() + "修改了团队证书。");
        else
            log(getEditorRole() + editor.getStu_name() + "清除了团队证书。");

    }

    private String getEditorRole(){
        if(isTeacherEditing)
            return "管理员";
        else
            return "学生";
    }

    public void log(String str){
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        competitionMapper.insertCompetitionTeamLog(team.getId(), str, timeNow);
    }


    public void logCheck(int status, String msg) {
        String statusName = switch (status) {
            case 0 -> "草稿";
            case 1 -> "等待审核";
            case 2 -> "审核通过";
            case 3 -> "审核不通过";
            default -> String.valueOf(status);
        };

        String l = "管理员" + studentInfoService.getCurrentUserInfo().getStu_name() +"设置审核状态为：" + statusName;
        if(msg != null && !msg.equals(""))
            l += "，审核意见：" + msg;
        log(l);
    }
}
