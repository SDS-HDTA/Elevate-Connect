package org.sds.elevateconnect.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.sds.elevateconnect.model.project.MarkerType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TypeHandler class for MyBatis to map tinyint from DB into MarkerType class
public class MarkerTypeTypeHandler extends BaseTypeHandler<MarkerType> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MarkerType markerType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, markerType.getIntValue());
    }

    @Override
    public MarkerType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int roleIntValue = resultSet.getInt(columnName);
        return MarkerType.fromInt(roleIntValue);
    }

    @Override
    public MarkerType getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int roleIntValue = resultSet.getInt(columnIndex);
        return MarkerType.fromInt(roleIntValue);
    }

    @Override
    public MarkerType getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int roleIntValue = callableStatement.getInt(columnIndex);
        return MarkerType.fromInt(roleIntValue);
    }
}
