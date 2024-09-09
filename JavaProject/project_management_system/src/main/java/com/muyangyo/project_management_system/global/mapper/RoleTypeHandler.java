package com.muyangyo.project_management_system.global.mapper;

import com.muyangyo.project_management_system.global.model.Role;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/5
 * Time: 10:10
 */
public class RoleTypeHandler extends BaseTypeHandler<Role> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Role parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public Role getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (rs.wasNull()) return null;
        int anInt = rs.getInt(columnName);
        return Role.getRoleFromInt(anInt);
    }

    @Override
    public Role getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.wasNull()) return null;
        int anInt = rs.getInt(columnIndex);
        return Role.getRoleFromInt(anInt);
    }

    @Override
    public Role getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.wasNull()) return null;
        int anInt = cs.getInt(columnIndex);
        return Role.getRoleFromInt(anInt);
    }
}
