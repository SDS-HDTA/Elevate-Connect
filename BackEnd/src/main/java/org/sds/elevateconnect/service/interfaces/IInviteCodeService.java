package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.InviteCode;

public interface IInviteCodeService {
    void generateCode(String email, int roleAsInt, String country);
    void deleteCode(InviteCode inviteCode);
    InviteCode getInviteCodeByCode(String code);
}
    