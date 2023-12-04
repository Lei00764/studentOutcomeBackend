package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.service.CompetitionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Resource
    CompetitionMapper competitionMapper;

    @Autowired
    StudentInfoService studentInfoService;

    @Override
    @Transactional
    public void checkCompetition(int competitionId, int termId, int prizeId) {
        Map<String, Object> PrizeInfo = competitionMapper.selectPrizeInfoByPrizeId(prizeId);

        if (PrizeInfo == null) {
            throw new BusinessException("prizeId 不存在");
        }
        if (termId != (int) PrizeInfo.get("term_id")) {
            throw new BusinessException("termId, prizeId 不对应");
        }

        Map<String, Object> TermInfo = competitionMapper.selectTermInfoByTermId(termId);

        if (TermInfo == null) {
            throw new BusinessException("termId 不存在");
        }
        if (competitionId != (int) TermInfo.get("competition_id")) {
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
     * 根据 competitionId 查看竞赛届别相关信息
     *
     * @param competitionId
     */
    @Override
    @Transactional
    public Map<String, Object> selectTermByCompetitionId(int competitionId) {
        List<Map<String, Object>> termInfoList = competitionMapper.selectTermInfoByCompetitionId(competitionId);

        Map<String, Object> competitionInfo = competitionMapper.selectCompetitionInfoByCompetitionId(competitionId);
        String competitionName = (String) competitionInfo.get("competition_name");
        int typeId = (int) competitionInfo.get("type_id");

        String typeName = competitionMapper.selectTypeInfoByTypeId(typeId).get("type_name").toString();


        List<Map<String, Object>> CompetitionInfoList = new ArrayList<>();

        for (Map<String, Object> termInfo : termInfoList) {
            Map<String, Object> CompetitionInfo = new HashMap<>();

            CompetitionInfo.put("competition_name", competitionName);
            CompetitionInfo.put("type_name", typeName);
            CompetitionInfo.put("id", termInfo.get("id"));
            CompetitionInfo.put("term_name", termInfo.get("term_name"));
            String levelName = competitionMapper.selectLevelNameByLevelId((int) termInfo.get("level_id"));
            CompetitionInfo.put("level_name", levelName);
            CompetitionInfo.put("organizer", termInfo.get("organizer"));

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
        List<Map<String, Object>> prizeInfoList = competitionMapper.selectPrizeInfoByTermId(termId);

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
        Map<String, Object> teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);

        if (teamInfo == null) {
            throw new BusinessException(601, "队伍不存在");
        }

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> competitionInfo = new HashMap<>();
        Map<String, Object> termInfo = new HashMap<>();
        Map<String, Object> prizeInfo = new HashMap<>();
        List<Map<String, Object>> membersInfo = new ArrayList<>();
        List<Map<String, Object>> logsInfo = new ArrayList<>();

        result.put("status", teamInfo.get("verify_status"));
        result.put("award_date", teamInfo.get("award_date"));
        result.put("certification_img_url", teamInfo.get("image_id"));
        result.put("desc", teamInfo.get("description"));
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
    public List<Map<String, Object>> selectTeamByNothing(int pageNo) {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();
        return competitionMapper.selectTeamByNothing(userId, (pageNo - 1) * 20);
    }

    @Override
    public int selectTeamCountByNothing() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();
        return competitionMapper.selectTeamCountByNothing(userId);
    }

    /**
     * 检查队伍是否存在
     */
    @Override
    @Transactional
    public void checkTeamExist(int teamId) {
        Map<String, Object> teamInfo = competitionMapper.selectTeamInfoByTeamId(teamId);
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
        competitionMapper.updateTeamStatus(teamId, 1);
    }

    /**
     * 撤回审核申请，回到“草稿”阶段
     */
    @Override
    @Transactional
    public void withdrawTeam(int teamId) {
        competitionMapper.updateTeamStatus(teamId, 0);
    }

    /**
     * 清空证书图片
     *
     * @param teamId
     */
    @Override
    @Transactional
    public void clearCertification(int teamId) {
        competitionMapper.updateTeamImage(teamId, null);
    }
}
