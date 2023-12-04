package com.example.studentoutcomebackend.service;

import java.util.List;
import java.util.Map;

public interface CompetitionService {

    /**
     * 检查 competitionId, termId, prizeId 是否对应
     */
    void checkCompetition(int competitionId, int termId, int prizeId);


    /**
     * 创建一个新的队伍
     */
    void createNewTeam(int competitionId, int termId, int prizeId, String awardDate, String description);

    /**
     * 根据 competitionId 查看竞赛届别
     */
    List<Map<String, Object>> selectTermByCompetitionId(int competitionId);

    /**
     * 根据 termId 查询竞赛奖项名称和含金量
     */
    List<Map<String, Object>> selectPrizeInfoByTermId(int termId);

}
