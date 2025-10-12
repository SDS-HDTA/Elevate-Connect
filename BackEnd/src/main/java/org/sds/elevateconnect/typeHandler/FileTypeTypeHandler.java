package org.sds.elevateconnect.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.sds.elevateconnect.model.project.FileType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileTypeTypeHandler extends BaseTypeHandler<FileType> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, FileType fileType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, fileType.getIntValue());
    }

    @Override
    public FileType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int fileTypeIntValue = resultSet.getInt(columnName);
        return FileType.fromInt(fileTypeIntValue);
    }

    @Override
    public FileType getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int fileTypeIntValue = resultSet.getInt(columnIndex);
        return FileType.fromInt(fileTypeIntValue);
    }

    @Override
    public FileType getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int fileTypeIntValue = callableStatement.getInt(columnIndex);
        return FileType.fromInt(fileTypeIntValue);
    }
}
