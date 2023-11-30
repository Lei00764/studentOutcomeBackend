package com.example.studentoutcomebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.Map;

@Mapper
public interface PermissionMapper {
    @Select("{call CheckPermission(#{userId, jdbcType=INTEGER, mode=IN}, #{permissionName, jdbcType=VARCHAR, mode=IN}, #{result,jdbcType=INTEGER, mode=OUT})}")
    @Options(statementType = StatementType.CALLABLE)
    void checkPermission(Map<String, Object> params);
}
