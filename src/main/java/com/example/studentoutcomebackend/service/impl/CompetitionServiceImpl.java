package com.example.studentoutcomebackend.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.example.studentoutcomebackend.entity.Competition.*;
import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.entity.vo.CompetitionEditingStudent;
import com.example.studentoutcomebackend.entity.vo.QueryField;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.service.CompetitionService;
import com.example.studentoutcomebackend.service.NoticeService;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.utils.CompetitionExportForm;
import com.example.studentoutcomebackend.utils.SM3Util;
import jakarta.annotation.Resource;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Resource
    CompetitionMapper competitionMapper;

    @Autowired
    StudentInfoService studentInfoService;

    @Autowired
    PermissionService permissionService;
    @Autowired
    NoticeService noticeService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 检查这三个是否匹配，不匹配直接抛异常
     * @param competitionId
     * @param termId
     * @param prizeId
     */
    @Override
    @Transactional
    public void checkCompetition(int competitionId, int termId, int prizeId) {
        CompetitionPrize PrizeInfo = competitionMapper.selectPrizeInfoByPrizeId(prizeId);

        if (PrizeInfo == null) {
            throw new BusinessException("prizeId 不存在");
        }
        if (termId != PrizeInfo.getTerm_id()) {
            throw new BusinessException("termId, prizeId 不对应");
        }

        CompetitionTerm TermInfo = competitionMapper.selectTermInfoByTermId(termId);

        if (TermInfo == null) {
            throw new BusinessException("termId 不存在");
        }
        if (competitionId != TermInfo.getCompetition_id()) {
            throw new BusinessException("competitionId, termId, prizeId 不对应");
        }
    }

    @Override
    @Transactional
    public int createNewTeam(int competitionId, int termId, int prizeId, String awardDate, String description) {
        Map<String, Object> params = new HashMap<>();
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int creatorId = studentInfo.getUser_id();

        params.put("creatorId", creatorId);
        params.put("competitionId", competitionId);
        params.put("termId", termId);
        params.put("prizeId", prizeId);
        params.put("awardDate", awardDate);
        params.put("certDescription", description);
        params.put("resultCode", 0);
        params.put("newTeamId", 0);

        // 创建一个新的队伍
        competitionMapper.insertCompetitionTeam(params);
        if ((Integer) params.get("resultCode") != 0) {
            throw new BusinessException(630, "您已经填报了本届比赛的参赛信息，无需重复填报");
        }
        return (int) params.get("newTeamId");
    }

    /**
     * 根据 competitionId 查看竞赛届别相关信息，最后还包含主办方之类的信息
     *
     * @param competitionId
     */
    @Override
    @Transactional
    public Map<String, Object> selectTermByCompetitionId(int competitionId) {

        // 把这个竞赛的所有届数都拉出来
        List<CompetitionTerm> termInfoList = competitionMapper.selectTermInfoByCompetitionId(competitionId);

        // 获得竞赛相关的信息
        Competition competitionInfo = competitionMapper.selectCompetitionInfoByCompetitionId(competitionId);


        List<Map<String, Object>> CompetitionInfoList = new ArrayList<>();

        for (CompetitionTerm termInfo : termInfoList) {
            Map<String, Object> CompetitionInfo = new HashMap<>();

            // CompetitionInfo.put("competition_name", );
            CompetitionInfo.put("type_name", competitionInfo.getType().getName());
            CompetitionInfo.put("id", termInfo.getId());
            CompetitionInfo.put("term_name", termInfo.getName());
            CompetitionInfo.put("level_name", termInfo.getLevel().getLevelName());
            CompetitionInfo.put("organizer", termInfo.getOrganizer());

            CompetitionInfoList.add(CompetitionInfo);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("terms", CompetitionInfoList);

        return result;
    }

    /**
     * 根据 termId 查询竞赛奖项名称和含金量
     */
    @Override
    @Transactional
    public Map<String, Object> selectPrizeInfoByTermId(int termId) {
        List<CompetitionPrize> prizeInfoList = competitionMapper.selectPrizeInfoByTermId(termId);

        Map<String, Object> result = new HashMap<>();
        result.put("prizes", prizeInfoList);

        return result;
    }

    /**
     * 根据 teamId 查询该队伍的全部信息
     */
    @Override
    @Transactional
    public Map<String, Object> selectTeamInfoByTeamId(int teamId) {
        throwIfNotInTeam(teamId);
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);

        if (teamInfo == null) {
            throw new BusinessException(601, "队伍不存在");
        }

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> competitionInfo = new HashMap<>();
        Map<String, Object> termInfo = new HashMap<>();
        Map<String, Object> prizeInfo = new HashMap<>();

        List<CompetitionTeamStudent> membersInfo = teamInfo.getCompetitionTeamStudentList();
        List<Map<String, Object>> logsInfo = new ArrayList<>();

        result.put("status", teamInfo.getVerifyStatus());
        result.put("award_date", teamInfo.getAwardDate());
        String imgUrl = teamInfo.getImageId();
        if(imgUrl == null)
            result.put("certification_img_url", "");
        else
            result.put("certification_img_url", "/certImg/" + teamInfo.getImageId());
        result.put("desc", teamInfo.getDescription());

        competitionInfo.put("id", teamInfo.getCompetition().getId());
        competitionInfo.put("competition_name", teamInfo.getCompetition().getCompetitionName());
        competitionInfo.put("type_name", teamInfo.getCompetition().getType().getName());

        termInfo.put("organizer", teamInfo.getTerm().getOrganizer());
        termInfo.put("id", teamInfo.getTerm().getId());
        termInfo.put("level_name", teamInfo.getTerm().getLevel().getLevelName());
        termInfo.put("name", teamInfo.getTerm().getName());

        prizeInfo.put("id", teamInfo.getPrize().getId());
        prizeInfo.put("name", teamInfo.getPrize().getPrize_name());

        List<CompetitionOperationLog> logs = teamInfo.getLogs();
        for(CompetitionOperationLog log: logs){
            Map<String, Object> l = new HashMap<>();
            l.put("time", log.getOperation_time());
            l.put("msg", log.getOperation_text());
            logsInfo.add(l);
        }

        result.put("competition", competitionInfo);
        result.put("term", termInfo);
        result.put("prize", prizeInfo);
        result.put("members", membersInfo);
        result.put("logs", logsInfo);

        return result;
    }

    /**
     * 根据 关键字 查询竞赛信息
     *
     * @param keyword
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> selectCompetitionInfoByKeyword(String keyword) {
        List<Map<String, Object>> competitionInfoList = competitionMapper.selectCompetitionInfoByKeyword(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("competitions", competitionInfoList);

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> selectTeamByCriteria(String keyword, String field, boolean precise, int pageNo) {
        if (!field.equals("") && !field.equals("status_code") && !field.equals("competition_name") && !field.equals("team_id")) {
            throw new BusinessException(601, "参数错误");
        }

        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();
        Map<String, Object> callParams = new HashMap<>();
        callParams.put("userId", userId);
        callParams.put("keyword", keyword);
        callParams.put("fieldName", field);
        callParams.put("precise", precise ? 1 : 0);
        callParams.put("pageNo", pageNo);
        callParams.put("totalCount", -1);

        var teams = competitionMapper.selectTeamByCriteria(callParams);
        Map<String, Object> ans = new HashMap<>();
        ans.put("teams", teams);
        ans.put("totalCount", callParams.get("totalCount"));
        return ans;
    }

    /**
     * 检查队伍是否存在
     */
    @Override
    @Transactional
    public void checkTeamExist(int teamId) {
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
        if (teamInfo == null) {
            throw new BusinessException(601, "队伍不存在");
        }
    }

    /**
     * 提交审核
     */
    @Override
    @Transactional
    public void submitToReview(int teamId) {
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
        teamInfo.submitToReview();
    }

    /**
     * 撤回审核申请，回到“草稿”阶段
     */
    @Override
    @Transactional
    public void withdrawTeam(int teamId) {
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
        teamInfo.withdraw();
    }

    /**
     * 设置/清除证书图片
     *
     * @param imageFile 如果imageFile为空就是清除
     * @param teamId
     */
    @Override
    @Transactional
    public String uploadCertification(MultipartFile imageFile, int teamId) {
        throwIfNotInTeam(teamId);
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
        if(studentInfoService.getCurrentUserInfo().getMenuGroupId() == 3)
            teamInfo.setForceEdit(true);

        return teamInfo.editCertification(imageFile);
    }

    @Override
    public void throwIfNotInTeam(int teamId) {
        Object ans = competitionMapper.select1IfUserInTeam(studentInfoService.getCurrentUserInfo().getUser_id(), teamId);
        if (ans == null)
            permissionService.throwIfDontHave("teacher.competition.record.edit", "您不属于该竞赛队伍");
    }

    @Override
    public void editTeamBasicInfo(int teamId, int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc) {
        throwIfNotInTeam(teamId);
        CompetitionTeam teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
        if(studentInfoService.getCurrentUserInfo().getMenuGroupId() == 3)
            teamInfo.setForceEdit(true);

        teamInfo.editBasicInfo(newCompetitionId, newTermId, newPrizeId, newAwardDate, newDesc);

    }

    @Override
    public String createInvitationCode(int teamId) {
        // 先检查当前用户能不能修改
        throwIfNotInTeam(teamId);

        String code = SM3Util.getRandomString();
        String teamIdStr = String.valueOf(teamId);
        // 先删除旧的
        String codeOld = redisTemplate.opsForValue().get("competition:teamCode:" + teamIdStr);
        if(codeOld != null){
            redisTemplate.delete("competition:codeTeam:" + codeOld);
        }

        redisTemplate.opsForValue().set("competition:codeTeam:" + code, teamIdStr, 1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("competition:teamCode:" + teamIdStr, code, 1, TimeUnit.DAYS);

        return code;
    }

    @Override
    public void invitationCode(String code) {
        String teamIdStr = redisTemplate.opsForValue().get("competition:codeTeam:" + code);
        if(teamIdStr == null)
            throw new BusinessException(606, "邀请码无效或过期");
        int teamId = Integer.parseInt(teamIdStr);
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);

        Object ans = competitionMapper.select1IfUserInTeam(studentInfoService.getCurrentUserInfo().getUser_id(), teamId);
        if (ans != null)
            throw new BusinessException("您已经加入该队伍了！");

        if(team == null)
            throw new BusinessException("队伍不存在");

        team.addMember(studentInfoService.getCurrentUserInfo());

    }

    @Override
    public void editTeamStudents(int teamId, LinkedList<CompetitionEditingStudent> newStuObj) {
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);

        if(studentInfoService.getCurrentUserInfo().getMenuGroupId() == 3)
            team.setForceEdit(true);

        team.editContributionOrder(newStuObj);
    }

    @Override
    public void leaveTeam(int teamId){
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);
        team.removeMember(studentInfoService.getCurrentUserInfo());
    }

    @Override
    public void setStudentVerified(int teamId, int verified) {
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);
        team.setStudentVerified(studentInfoService.getCurrentUserInfo().getUser_id(), verified);
    }

    private Map<String, Object> toTeacherTeamListJson(List<CompetitionTeam> teams, int totalCount){
        Map<String, Object> ans = new HashMap<>();
        List<Map<String, Object>> teamsJson = new ArrayList<>();
        for(CompetitionTeam team: teams){
            Map<String, Object> nowTeamJson = new HashMap<>();
            nowTeamJson.put("team_id", team.getId());
            nowTeamJson.put("competition_name", team.getCompetition().getCompetitionName());
            nowTeamJson.put("term_name", team.getTerm().getName());
            nowTeamJson.put("prize_name", team.getPrize().getPrize_name());
            nowTeamJson.put("status_code", team.getVerifyStatus());
            ArrayList<Map<String, Object>> nowTeamStudentsJson = new ArrayList<>();
            List<CompetitionTeamStudent> students = team.getCompetitionTeamStudentList();
            for(CompetitionTeamStudent student : students){
                Map<String, Object> nowStudentJson = new HashMap<>();
                nowStudentJson.put("stu_id", student.getStu_id());
                nowStudentJson.put("stu_name", student.getStu_name());
                nowTeamStudentsJson.add(nowStudentJson);
            }
            nowTeamJson.put("students", nowTeamStudentsJson);
            teamsJson.add(nowTeamJson);
        }
        ans.put("teams", teamsJson);
        ans.put("count", totalCount);

        return ans;
    }

    @Override
    public Map<String, Object> selectTeamByCriteriaTeacher(List<QueryField> fields, int pageNo) {
        List<CompetitionTeam> teams = competitionMapper.selectTeamByCriteriaTeacher(fields, (pageNo - 1) * 20);
        int totalCount = competitionMapper.selectTeamCountByCriteriaTeacher(fields);

        return toTeacherTeamListJson(teams, totalCount);
    }

    @Override
    public Map<String, Object> selectTeamByStudentTeacher(int userId, int pageNo) {
        StudentInfo student = studentInfoService.selectUserByUserId(userId);
        List<CompetitionTeam> teams = competitionMapper.selectTeamByStudentTeacher(student.getUser_id(), (pageNo - 1) * 20);
        int totalCount = competitionMapper.selectTeamCountByStudentTeacher(student.getUser_id());
        return toTeacherTeamListJson(teams, totalCount);
    }

    @Override
    public void addStudentToTeam(Integer teamId, Integer userId) {
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);
        team.setForceEdit(true);

        noticeService.sendPersonalNotice(userId, "管理员" +
                studentInfoService.getCurrentUserInfo().getStu_name() +"把您加入到队伍"+ teamId + "中，点击查看。", "competition/view/" + teamId);

        team.addMember(studentInfoService.selectUserByUserId(userId));
    }

    @Override
    public void removeStudentFromTeam(Integer teamId, Integer userId) {
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);
        team.setForceEdit(true);

        noticeService.sendPersonalNotice(userId, "管理员" +
                studentInfoService.getCurrentUserInfo().getStu_name() +"将您从队伍"+ teamId + "中移出。", null);

        team.removeMember(studentInfoService.selectUserByUserId(userId));
    }

    @Override
    public void changeVerifyStatus(int teamId, int status, String msg){
        CompetitionTeam team = competitionMapper.selectTeamInfoByTeamId(teamId);
        team.setForceEdit(true);

        for(CompetitionTeamStudent student : team.getCompetitionTeamStudentList()){
            noticeService.sendPersonalNotice(student.getUser_id(), "管理员" +
                    studentInfoService.getCurrentUserInfo().getStu_name() +"将您的队伍审核状态设置为："+ CompetitionTeamLogger.getStatusString(status) + "，点击查看。", "competition/view/" + teamId);
        }

        team.setVerifyStatus(status, msg);
    }

    /**
     * @Author asahi
     * @Description 导出所有竞赛列表
     * @Date 下午9:35 2024/3/24
     * @Param
     * @return
     **/
    @Override
    public ByteArrayResource exportAllCompetition() throws IOException {
        // get info from database
        List<CompetitionExportForm> competitions = competitionMapper.selectCompetitionInfo();
        competitions = CompetitionExportForm.process(competitions);

        // export by easypoi
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(
                        "竞赛列表",
                        "竞赛sheet"),
                CompetitionExportForm.class,
                competitions);

        // save & convert
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
