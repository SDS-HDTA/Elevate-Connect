package org.sds.elevateconnect.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.sds.elevateconnect.model.auth.UserRole;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TypeHandler class for MyBatis to map tinyint from DB into UserRole class
public class UserRoleTypeHandler extends BaseTypeHandler<UserRole> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserRole userRole, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, userRole.getIntValue());
    }

    @Override
    public UserRole getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int roleIntValue = resultSet.getInt(columnName);
        return UserRole.fromInt(roleIntValue);
    }

    @Override
    public UserRole getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int roleIntValue = resultSet.getInt(columnIndex);
        return UserRole.fromInt(roleIntValue);
    }

    @Override
    public UserRole getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int roleIntValue = callableStatement.getInt(columnIndex);
        return UserRole.fromInt(roleIntValue);
    }
}
