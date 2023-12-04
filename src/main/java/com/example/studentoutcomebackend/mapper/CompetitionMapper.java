package com.example.studentoutcomebackend.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompetitionMapper {

    /**
     * 通过调用CreateTeamRecord创建队伍基本信息。如果成功创建，resultCode=0，newTeamId为新创建的记录的id
     * 如果不成功，resultCode=1，说明已经有记录了，此时newTeamId为原有的记录的id
     */
    @Select("{call CreateTeamRecord(" +
            "#{creatorId,jdbcType=INTEGER,mode=IN}," +
            "#{competitionId,jdbcType=INTEGER,mode=IN}," +
            "#{termId,jdbcType=INTEGER,mode=IN}," +
            "#{prizeId,jdbcType=INTEGER,mode=IN}," +
            "#{awardDate,jdbcType=VARCHAR,mode=IN}," +
            "#{certDescription,jdbcType=VARCHAR,mode=IN}," +
            "#{resultCode,jdbcType=INTEGER,mode=OUT}," +
            "#{newTeamId,jdbcType=INTEGER,mode=OUT})}")
    @Options(statementType = StatementType.CALLABLE)
    void insertCompetitionTeam(Map<String, Object> params);

    /**
     * 在 COMPETITION 表，通过 competitionId 查 type_id 和 competition_name
     */
    @Select("SELECT type_id, competition_name FROM COMPETITION WHERE id = #{competitionId}")
    Map<String, Object> selectCompetitionInfoByCompetitionId(int competitionId);

    /**
     * 在 COMPETITION_PRIZE 表，通过 prizeId 查 prizeInfo
     */
    @Select("SELECT term_id, prize_name, prize_order FROM COMPETITION_PRIZE WHERE id = #{prizeId}")
    Map<String, Object> selectPrizeInfoByPrizeId(int TermId);

    /**
     * 在 COMPETITION_TERM 表，通过 termId 查 competitionId
     */
    @Select("SELECT term_name, competition_id, level_id, organizer FROM COMPETITION_TERM WHERE id = #{TermId}")
    Map<String, Object> selectTermInfoByTermId(int TermId);

    /**
     * 在 COMPETITION_TYPE 表，通过 typeId 查 type_name
     */
    @Select("SELECT type_name FROM COMPETITION_TYPE WHERE id = #{typeId}")
    String selectTypeNameByTypeId(int typeId);

    /**
     * 在 COMPETITION_TERM 表，通过 competitionId 查 termId, termName, levelId, organizer
     */
    @Select("SELECT id, term_name, level_id, organizer FROM COMPETITION_TERM WHERE competition_id = #{competitionId}")
    List<Map<String, Object>> selectTermInfoByCompetitionId(int competitionId);

    /**
     * 在 COMPETITION_PRIZE 表中，通过 termId 查 prizeId, prizeName, prizeOrder
     */
    @Select("SELECT id, prize_name, prize_order FROM COMPETITION_PRIZE WHERE term_id = #{termId}")
    List<Map<String, Object>> selectPrizeInfoByTermId(int termId);

    /**
     * 在 COMPETITION 表中，通过关键字查找竞赛信息
     */
    @MapKey("id")
    List<Map<String, Object>> selectCompetitionInfoByKeyword(String keyword);

    /**
     * 在 COMPETITION_TEAM_STUDENT 表中，插入 teamId 和 studentId
     */
    @Insert("INSERT INTO COMPETITION_TEAM_STUDENT (team_id, student_id, contribution_order) VALUES (#{teamId}, #{studentId}, 0)")
    void insertTeamStudent(int teamId, int studentId);

    /**
     * 在 COMPETITION_LEVEL 表中，通过 levelId 查 levelName
     */
    @Select("SELECT level_name FROM COMPETITION_LEVEL WHERE id = #{levelId}")
    String selectLevelNameByLevelId(int levelId);

    /**
     * 不使用任何条件查询学生加入的参赛队伍
     * @param userId 用户id
     * @param offset 偏移量（(页号-1)*20）
     * @return
     */
    List<Map<String,Object>> selectTeamByNothing(int userId, int offset);

    /**
     * 不使用任何条件查询学生加入的参赛队伍数量
     * @param userId 用户id
     * @return
     */
    int selectTeamCountByNothing(int userId);

}
