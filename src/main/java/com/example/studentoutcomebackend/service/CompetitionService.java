package com.example.studentoutcomebackend.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CompetitionService {

    /**
     * 检查 competitionId, termId, prizeId 是否对应
     */
    void checkCompetition(int competitionId, int termId, int prizeId);
    
    /**
     * 创建一个新的队伍
     *
     * @return 创建的新队伍的teamId
     */
    int createNewTeam(int competitionId, int termId, int prizeId, String awardDate, String description);

    /**
     * 根据 competitionId 查看竞赛届别
     */
    Map<String, Object> selectTermByCompetitionId(int competitionId);

    /**
     * 根据 termId 查询竞赛奖项名称和含金量
     */
    Map<String, Object> selectPrizeInfoByTermId(int termId);

    /**
     * 根据 关键字 查询竞赛信息
     */
    Map<String, Object> selectCompetitionInfoByKeyword(String keyword);

    @Transactional
    List<Map<String, Object>> selectTeamByNothing(int pageNo);

    int selectTeamCountByNothing();
}
