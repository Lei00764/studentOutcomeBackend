package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.Competition.Competition;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.service.CompetitionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Resource
    CompetitionMapper competitionMapper;

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
    public void createNewTeam(int competitionId, int termId, int prizeId, String awardDate, String description) {
        competitionMapper.insertCompetitionTeam(competitionId, termId, prizeId, awardDate, description);
    }

    /**
     * 根据 competitionId 查看竞赛届别相关信息
     *
     * @param competitionId
     */
    @Override
    @Transactional
    public List<Map<String, Object>> selectTermByCompetitionId(int competitionId) {
        List<Map<String, Object>> termInfoList = competitionMapper.selectTermInfoByCompetitionId(competitionId);

        Map<String, Object> competitionInfo = competitionMapper.selectCompetitionInfoByCompetitionId(competitionId);
        String competitionName = (String) competitionInfo.get("competition_name");
        int typeId = (int) competitionInfo.get("type_id");

        String typeName = competitionMapper.selectTypeNameByTypeId(typeId);

        List<Map<String, Object>> CompetitionInfoList = new ArrayList<>();

        for (Map<String, Object> termInfo : termInfoList) {
            Map<String, Object> CompetitionInfo = new HashMap<>();

            CompetitionInfo.put("competition_name", competitionName);
            CompetitionInfo.put("type_name", typeName);

            CompetitionInfo.put("id", termInfo.get("id"));
            CompetitionInfo.put("term_name", termInfo.get("term_name"));
            CompetitionInfo.put("level_id", termInfo.get("level_id"));
            CompetitionInfo.put("organizer", termInfo.get("organizer"));

            CompetitionInfoList.add(CompetitionInfo);
        }

        return CompetitionInfoList;
    }

    /**
     * 根据 termId 查询竞赛奖项名称和含金量
     */
    @Override
    @Transactional
    public List<Map<String, Object>> selectPrizeInfoByTermId(int termId) {
        List<Map<String, Object>> prizeInfoList = competitionMapper.selectPrizeInfoByTermId(termId);

        return prizeInfoList;
    }

}
