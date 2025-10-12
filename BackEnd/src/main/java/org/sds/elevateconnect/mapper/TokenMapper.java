package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.Token;

@Mapper
public interface TokenMapper {
    Token getTokenByType(String type);
    void insertToken(Token token);
    void deleteTokenByType(String type);
    void updateTokenByType(Token token);
}
