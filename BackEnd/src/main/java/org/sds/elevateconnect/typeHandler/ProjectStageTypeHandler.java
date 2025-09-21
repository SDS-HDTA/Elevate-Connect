package org.sds.elevateconnect.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.sds.elevateconnect.model.project.ProjectStage;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TypeHandler class for MyBatis to map tinyint from DB into ProjectStage class
public class ProjectStageTypeHandler extends BaseTypeHandler<ProjectStage> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ProjectStage projectStage, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, projectStage.getIntValue());
    }

    @Override
    public ProjectStage getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int roleIntValue = resultSet.getInt(columnName);
        return ProjectStage.fromInt(roleIntValue);
    }

    @Override
    public ProjectStage getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int roleIntValue = resultSet.getInt(columnIndex);
        return ProjectStage.fromInt(roleIntValue);
    }

    @Override
    public ProjectStage getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int roleIntValue = callableStatement.getInt(columnIndex);
        return ProjectStage.fromInt(roleIntValue);
    }
}
