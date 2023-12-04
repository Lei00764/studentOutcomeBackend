package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.CompetitionService;
import com.example.studentoutcomebackend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/competition")
public class CompetitionController extends BaseController {

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 新建队伍
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/createNewTeam")
    public ResponseVO newTeam(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("student.competition.edit", null);

        try {
            int competitionId = (int) requestMap.get("competition_id");
            int termId = (int) requestMap.get("term_id");
            int prizeId = (int) requestMap.get("prize_id");
            String awardDate = (String) requestMap.get("award_date");
            String description = (String) requestMap.get("desc");

            // 校验 competitionId, termId, prizeId 是否对应
            competitionService.checkCompetition(competitionId, termId, prizeId);
            // 新建队伍
            int newTeamId = competitionService.createNewTeam(competitionId, termId, prizeId, awardDate, description);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("team_id", newTeamId);

            return getSuccessResponseVO(resMap);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 根据 competitionId 查看竞赛届别信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("queryTerm")
    public ResponseVO queryTerm(@RequestBody Map<String, Object> requestMap) {
        try {
            int competitionId = (int) requestMap.get("competition_id");

            // 根据 competitionId 查看竞赛届别
            Map<String, Object> CompetitionInfoList = competitionService.selectTermByCompetitionId(competitionId);

            return getSuccessResponseVO(CompetitionInfoList);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 根据 termId 查看竞赛奖项名称和含金量
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("termPrize")
    public ResponseVO termPrize(@RequestBody Map<String, Object> requestMap) {
        try {
            int termId = (int) requestMap.get("term_id");

            // 根据 termId 查看竞赛奖项名称和含金量
            Map<String, Object> termPrizeInfo = competitionService.selectPrizeInfoByTermId(termId);
            return getSuccessResponseVO(termPrizeInfo);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 根据 teamId 查看队伍信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("getTeamInfo")
    public ResponseVO getTeamInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int teamId = (int) requestMap.get("team_id");
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("team_id", teamId);

            return getSuccessResponseVO(resMap);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 根据 关键字 查询竞赛信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("queryCompetition")
    public ResponseVO queryCompetition(@RequestBody Map<String, Object> requestMap) {
        try {
            String keyword = (String) requestMap.get("keyword");

            // 根据 关键字 查询竞赛信息
            Map<String, Object> competitionInfo = competitionService.selectCompetitionInfoByKeyword(keyword);
            return getSuccessResponseVO(competitionInfo);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }

    }

    @RequestMapping("getTeam")
    public ResponseVO getTeam(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("student.competition.querySelf", null);

        try {
            String fieldName = (String) requestMap.get("field");
            String keyword = (String) requestMap.get("keyword");
            boolean prizeId = (Boolean) requestMap.get("precise");
            int pageNo = (int) requestMap.get("page");
            Map<String, Object> resMap = new HashMap<>();
            if (fieldName.equals("")) {
                List<Map<String, Object>> teams = competitionService.selectTeamByNothing(pageNo);
                int teamCount = competitionService.selectTeamCountByNothing();
                resMap.put("count", teamCount);
                resMap.put("teams", teams);
                return getSuccessResponseVO(resMap);
            }
            throw new BusinessException("NOPE.");
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 提交审核
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("submitToReview")
    public ResponseVO submitToReview(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("student.competition.edit", null);

        try {
            int teamId = (int) requestMap.get("team_id");

            // 检查队伍是否存在
            competitionService.checkTeamExist(teamId);

            // 提交审核
            competitionService.submitToReview(teamId);
            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 撤回审核申请
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("withdrawTeam")
    public ResponseVO withdrawTeam(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("student.competition.edit", null);

        try {
            int teamId = (int) requestMap.get("team_id");

            // 检查队伍是否存在
            competitionService.checkTeamExist(teamId);

            // 撤回审核申请
            competitionService.withdrawTeam(teamId);
            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 清除证书图片
     */
    @RequestMapping("imgClear")
    public ResponseVO imageClear(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("student.competition.edit", null);

        try {
            int teamId = (int) requestMap.get("team_id");

            // 检查队伍是否存在
            competitionService.checkTeamExist(teamId);

            // 清除证书图片（即修改 image_id）
            competitionService.clearCertification(teamId);
            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

}
