package com.example.studentoutcomebackend.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * @author asahi
 * @version 1.0
 * @project sebok-course-project
 * @description COMPETITION 表的导出内容
 * @date 2024/3/27 15:25:36
 */

@Data
@ExcelTarget("competition")
public class CompetitionExportForm implements Serializable {
    @ExcelIgnore
    private int id;
    @Excel(name = "竞赛名称", width = 20.0)
    private String competition_name;

    @ExcelIgnore
    private int term_id;
    @Excel(name = "竞赛届数")
    private String term_name;


    @ExcelIgnore
    private int type_id;
    @Excel(name = "类别")
    private String type_name;

    @ExcelIgnore
    private int level_id;
    @Excel(name = "奖别", width = 25.0)
    private String level_name;

    @Excel(name = "组织部门", width = 20.0)
    private String organizer;

    // 实际上一个竞赛包含的奖项是一个多值属性，所以使用列表
    @ExcelIgnore
    private String prize_name;  // 用于存储当前行的奖种
    @ExcelIgnore
    private List<String> prize_names;  // 用于存储所有奖种
    @Excel(name = "奖种", width = 35.0)
    private String prize_names_string;  // 用于调整导出的格式
    public String getPrize_names_string() {
        StringBuilder stringBuilder = new StringBuilder();
        prize_names.forEach(e->{stringBuilder.append(e).append("，");});
        return stringBuilder.toString();
    }

    // 多值属性
    @ExcelIgnore
    private Integer prize_order;
    @ExcelIgnore
    private List<Integer> prize_orders;
    @Excel(name = "奖项权重(整数值)", width = 25.0)
    private String prize_orders_string;
    public String getPrize_orders_string() {
        StringBuilder stringBuilder = new StringBuilder();
        prize_orders.forEach(e->{stringBuilder.append(e.toString()).append("，");});
        return stringBuilder.toString();
    }

    /**
     * @Author asahi
     * @Description //TODO
     * @Date 下午9:31 2024/3/28
     * @Param 从数据库中读到的竞赛名单信息
     * @param forms
     * @return 经过去重复后的信息
     * @return java.util.List<com.example.studentoutcomebackend.utils.CompetitionExportForm>
     **/
    public static List<CompetitionExportForm> process(List<CompetitionExportForm> forms) {
        // 使用 Map 来存储相同 competition_name 和 term_name 的行的 prize_order 和 prize_name
        Map<String, List<Integer>> prizeOrdersMap = new HashMap<>();
        Map<String, List<String>> prizeNamesMap = new HashMap<>();

        // 使用 Set 来跟踪已经处理过的 competition_name 和 term_name 组合
        Set<String> processedKeys = new HashSet<>();

        for (CompetitionExportForm form : forms) {
            String key = form.getCompetition_name() + "_" + form.getTerm_name();

            // 添加 prize_order
            if (!prizeOrdersMap.containsKey(key)) {
                prizeOrdersMap.put(key, new ArrayList<>());
            }
            prizeOrdersMap.get(key).add(form.getPrize_order());

            // 添加 prize_name
            if (!prizeNamesMap.containsKey(key)) {
                prizeNamesMap.put(key, new ArrayList<>());
            }
            prizeNamesMap.get(key).add(form.getPrize_name());

            // 将当前 key 添加到 processedKeys 中
            processedKeys.add(key);
        }
        System.out.print(processedKeys);

        // 使用迭代器遍历 forms 列表，并删除重复的行
        Iterator<CompetitionExportForm> iterator = forms.iterator();
        while (iterator.hasNext()) {
            CompetitionExportForm form = iterator.next();
            String key = form.getCompetition_name() + "_" + form.getTerm_name();

            // 如果第一次遇到这一行，则将 prize_order 和 prize_name 存入对应的属性中
            if (processedKeys.contains(key)) {
                form.setPrize_orders(prizeOrdersMap.get(key));
                form.setPrize_names(prizeNamesMap.get(key));
                processedKeys.remove(key);
            } else {
                // 如果遇到了重复行，则使用迭代器安全地删除该行
                iterator.remove();
            }
        }

        return forms;
    }
}
