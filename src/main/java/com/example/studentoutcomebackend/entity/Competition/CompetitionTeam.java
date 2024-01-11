package com.example.studentoutcomebackend.entity.Competition;

import com.example.studentoutcomebackend.adapter.image.AlistImpl;
import com.example.studentoutcomebackend.adapter.image.ImageService;
import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.entity.vo.CompetitionEditingStudent;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.CompetitionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Component
public class CompetitionTeam {

    private int id;
    private int competition_id;

    private int term_id;

    private int prize_id;

    private String award_date;

    private int verify_status;

    private String image_id;

    private String description;

    @Autowired
    private CompetitionMapper competitionMapper;
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private StudentInfoService studentInfoService;

    private static CompetitionTeam me;
    private Competition cachedCompetition = null;
    private CompetitionTerm cachedCompetitionTerm = null;
    private CompetitionPrize cachedCompetitionPrize = null;

    private List<CompetitionTeamStudent> cachedCompetitionTeamStudentList = null;
    private CompetitionTeamLogger logger;

    @PostConstruct
    private void init() {
        me = this;
        me.competitionMapper = this.competitionMapper;
        me.competitionService = this.competitionService;
        me.studentInfoMapper = this.studentInfoMapper;
        me.studentInfoService = this.studentInfoService;
    }

    public int getId() {
        return id;
    }

    public Competition getCompetition() {
        if(cachedCompetition == null)
            cachedCompetition = me.competitionMapper.selectCompetitionInfoByCompetitionId(competition_id);
        return cachedCompetition;
    }

    public CompetitionTerm getTerm() {
        if(cachedCompetitionTerm == null)
            cachedCompetitionTerm = me.competitionMapper.selectTermInfoByTermId(term_id);
        return cachedCompetitionTerm;
    }

    public CompetitionPrize getPrize() {
        if(cachedCompetitionPrize == null)
            cachedCompetitionPrize = me.competitionMapper.selectPrizeInfoByPrizeId(prize_id);
        return cachedCompetitionPrize;
    }

    public String getAwardDate() {
        return award_date;
    }

    public int getVerifyStatus() {
        return verify_status;
    }

    public String getImageId() {
        return image_id;
    }

    public String getDescription() {
        return description;
    }


    public List<CompetitionTeamStudent> getCompetitionTeamStudentList(){
        if(cachedCompetitionTeamStudentList == null){
            cachedCompetitionTeamStudentList = me.competitionMapper.selectTeamCompetitionTeamStudentByTeamId(id);
        }
        return cachedCompetitionTeamStudentList;
    }

    @Transactional
    public void editBasicInfo(int newCompetitionId, int newTermId, int newPrizeId, String newAwardDate, String newDesc) {
        canEditOrThrow();
        // 检查是否匹配
        me.competitionService.checkCompetition(newCompetitionId, newTermId, newPrizeId);
        me.competitionMapper.updateTeamBasicInfo(id, newCompetitionId, newTermId, newPrizeId, newAwardDate, newDesc);
        competition_id = newCompetitionId;
        term_id = newTermId;
        prize_id = newPrizeId;
        award_date = newAwardDate;
        description = newDesc;

        cachedCompetition = null;
        cachedCompetitionPrize = null;
        cachedCompetitionTerm = null;

        getLogger().logEditBasicInfo(me.studentInfoService.getCurrentUserInfo(), newCompetitionId, newTermId, newPrizeId, newAwardDate, newDesc);

    }

    /**
     * 检查并更新队伍中的成员的贡献顺序
     * @param newStudents
     */
    @Transactional
    public void editContributionOrder(List<CompetitionEditingStudent> newStudents){
        canEditOrThrow();
        // 先检查传来的学生列表是否和数据库中的匹配
        List<CompetitionTeamStudent> oldStudents = getCompetitionTeamStudentList();
        if(oldStudents.size() > newStudents.size())
            throw new BusinessException(614, "有队伍成员未被包含");
        else if (oldStudents.size() < newStudents.size()) {
            throw new BusinessException(615, "有多余的成员");
        }

        HashSet<Integer> orderSet = new HashSet<>();
        for(int i = 1; i <= newStudents.size(); ++i){
            orderSet.add(i);
        }

        // 逐个检查队伍成员是都否在员队伍中，顺便检查贡献设置
        for(CompetitionEditingStudent newStudent : newStudents){
            CompetitionTeamStudent oldFoundStudent = null;
            if(orderSet.contains(newStudent.getOrder())){
                orderSet.remove(newStudent.getOrder());
            }else{
                throw new BusinessException(616, "贡献设置错误");
            }

            for(CompetitionTeamStudent oldStudent : oldStudents){
                if(oldStudent.getUser_id() == newStudent.getUserId()){
                    oldFoundStudent = oldStudent;
                    break;
                }
            }

            if(oldFoundStudent == null)
                throw new BusinessException(614, "有队伍成员未被包含");
            oldStudents.remove(oldFoundStudent);
        }
        if(!orderSet.isEmpty())
            throw new BusinessException(616, "贡献设置错误");

        // 验证通过，开始修改
        for(CompetitionEditingStudent newStudent : newStudents){
            me.competitionMapper.updateCompetitionTeamStudent(id, newStudent.getUserId(), newStudent.getOrder(), false);
        }

        getLogger().logEditOrder(me.studentInfoService.getCurrentUserInfo(), newStudents);
    }

    @Transactional
    public void addMember(StudentInfo newStudent){
        canEditOrThrow();
        me.competitionMapper.insertCompetitionTeamStudent(id, newStudent.getUser_id(), 0);

        getLogger().logJoin(newStudent.getUser_id());
    }

    @Transactional
    public void removeMember(StudentInfo studentToRemove){
        canEditOrThrow();
        getCompetitionTeamStudentList();
        me.competitionMapper.deleteCompetitionTeamStudent(id, studentToRemove.getUser_id());
        if(cachedCompetitionTeamStudentList.size() == 1){
            ImageService imageService = AlistImpl.get();
            if (image_id != null && !image_id.equals(""))
                imageService.removeImage(image_id);
            me.competitionMapper.deleteCompetitionTeam(id);
        }else{
            getLogger().logLeave(studentToRemove);
        }
    }

    @Transactional
    public void setStudentVerified(int userId, int verified) {
        canEditOrThrow();
        me.competitionMapper.updateStudentVerified(id, userId, verified);

        getLogger().logVerified(me.studentInfoMapper.selectUserByUserId(userId), verified);
    }

    @Transactional
    public void submitToReview() {
        canEditOrThrow();

        HashSet<Integer> orderSet = new HashSet<>();
        getCompetitionTeamStudentList();
        for(int i = 1; i <= cachedCompetitionTeamStudentList.size(); ++i){
            orderSet.add(i);
        }

        for(CompetitionTeamStudent student : cachedCompetitionTeamStudentList) {
            if (orderSet.contains(student.getOrder())) {
                orderSet.remove(student.getOrder());
            } else {
                throw new BusinessException(616, "贡献设置错误");
            }

            if(!student.isVerified()){
                throw new BusinessException(621, "有成员未确认贡献！");
            }
        }

        if(!orderSet.isEmpty())
            throw new BusinessException(616, "贡献设置错误");

        me.competitionMapper.updateTeamStatus(id, 1);

        getLogger().logSubmit(me.studentInfoService.getCurrentUserInfo());
    }

    /**
     * 撤回申请
     */
    @Transactional
    public void withdraw(){
        if(verify_status != 1)
            throw new BusinessException("当前状态不允许撤回申请");
        me.competitionMapper.updateTeamStatus(id, 0);

        getLogger().logWithdraw(me.studentInfoService.getCurrentUserInfo());
    }

    public void canEditOrThrow(){
        if(verify_status == 0 || verify_status == 3)
            return;
        throw new BusinessException("当前状态不可编辑！");
    }

    /**
     * 修改图片，如果imageFile为空就清除，返回图片的id
     * @param imageFile
     * @return
     */
    @Transactional
    public String editCertification(MultipartFile imageFile){
        canEditOrThrow();

        ImageService imageService = AlistImpl.get();
        String imageName;

        // 之前有图片了就删除
        if (image_id != null && !image_id.equals(""))
            imageService.removeImage(image_id);

        if (imageFile == null) {
            imageName = null;
            me.competitionMapper.updateTeamImage(id, null);
        } else {
            imageName = imageService.saveImage(imageFile);
            me.competitionMapper.updateTeamImage(id, imageName);
        }

        image_id = imageName;

        getLogger().logEditCertification(me.studentInfoService.getCurrentUserInfo(), imageName != null);

        return imageName;
    }

    private CompetitionTeamLogger getLogger(){
        if(logger == null)
            logger = new CompetitionTeamLogger(this, me.competitionMapper, me.studentInfoMapper);
        return logger;
    }

    public List<CompetitionOperationLog> getLogs(){
        return me.competitionMapper.selectCompetitionOperationLogByTeamId(id);
    }

}
