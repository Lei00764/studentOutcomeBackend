package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/competition")
public class CompetitionController extends BaseController {

    @Autowired
    private CompetitionService competitionService;

    /**
     * 新建队伍
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/createNewTeam")
    public ResponseVO newTeam(@RequestBody Map<String, Object> requestMap) {
        int competitionId = (int) requestMap.get("competition_id");
        int termId = (int) requestMap.get("term_id");
        int prizeId = (int) requestMap.get("prize_id");
        String awardDate = (String) requestMap.get("award_date");
        String description = (String) requestMap.get("description");

        // 校验 competitionId, termId, prizeId 是否对应
        competitionService.checkCompetition(competitionId, termId, prizeId);
        // 新建队伍
        competitionService.createNewTeam(competitionId, termId, prizeId, awardDate, description);

        return getSuccessResponseVO();
    }

    /**
     * 根据 competitionId 查看竞赛届别信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("queryTerm")
    public ResponseVO queryTerm(@RequestBody Map<String, Object> requestMap) {
        int competitionId = (int) requestMap.get("competition_id");

        // 根据 competitionId 查看竞赛届别
        Map<String, Object> CompetitionInfoList = competitionService.selectTermByCompetitionId(competitionId);

        return getSuccessResponseVO(CompetitionInfoList);
    }

    /**
     * 根据 termId 查看竞赛奖项名称和含金量
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("termPrize")
    public ResponseVO termPrize(@RequestBody Map<String, Object> requestMap) {
        int termId = (int) requestMap.get("term_id");

        Map<String, Object> termPrizeInfo = competitionService.selectPrizeInfoByTermId(termId);  // 根据 termId 查看竞赛奖项名称和含金量
        return getSuccessResponseVO(termPrizeInfo);
    }

    /**
     * 根据 teamId 查看队伍信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("getTeamInfo")
    public ResponseVO getTeamInfo(@RequestBody Map<String, Object> requestMap) {
        int teamId = (int) requestMap.get("team_id");


        return getSuccessResponseVO();
    }

}
