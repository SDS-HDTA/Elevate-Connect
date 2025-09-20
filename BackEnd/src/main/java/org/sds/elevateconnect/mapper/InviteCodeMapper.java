package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.InviteCode;

@Mapper
public interface InviteCodeMapper {
    void addCode(InviteCode inviteCode);
    InviteCode getInviteCodeByCode(String inviteCode);
    void deleteCode(InviteCode inviteCode);
}
