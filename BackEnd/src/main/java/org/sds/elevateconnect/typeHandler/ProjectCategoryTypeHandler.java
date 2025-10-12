package org.sds.elevateconnect.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.sds.elevateconnect.model.project.ProjectCategory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TypeHandler class for MyBatis to map tinyint from DB into ProjectCategory class
public class ProjectCategoryTypeHandler extends BaseTypeHandler<ProjectCategory> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ProjectCategory projectCategory, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, projectCategory.getIntValue());
    }

    @Override
    public ProjectCategory getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int categoryIntValue = resultSet.getInt(columnName);
        return ProjectCategory.fromInt(categoryIntValue);
    }

    @Override
    public ProjectCategory getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int categoryIntValue = resultSet.getInt(columnIndex);
        return ProjectCategory.fromInt(categoryIntValue);
    }

    @Override
    public ProjectCategory getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int categoryIntValue = callableStatement.getInt(columnIndex);
        return ProjectCategory.fromInt(categoryIntValue);
    }
}
