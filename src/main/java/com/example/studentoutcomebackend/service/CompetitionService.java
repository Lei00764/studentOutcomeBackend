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
     * 根据 teamId 查询该队伍的全部信息
     */
    Map<String, Object> selectTeamInfoByTeamId(int teamId);

    /**
     * 根据 关键字 查询竞赛信息
     */
    Map<String, Object> selectCompetitionInfoByKeyword(String keyword);

    /**
     * 根据条件去数据库里找当前用户加入的竞赛队伍
     * @param keyword
     * @param field
     * @param precise
     * @param pageNo
     * @return map包含totalCount与teams属性
     */
    @Transactional
    Map<String, Object> selectTeamByCriteria(String keyword, String field, boolean precise, int pageNo);

    /**
     * 检查队伍是否存在
     */
    void checkTeamExist(int teamId);

    /**
     * 提交审核
     */
    void submitToReview(int teamId);

    /**
     * 撤回审核申请
     */
    void withdrawTeam(int teamId);

    /**
     * 清除证书图片
     */
    void clearCertification(int teamId);
}
