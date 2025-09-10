package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.UserRole;

public interface IInviteCodeService {
    Result generateCode(String email, int intType);
    void deactivateCode(InviteCode inviteCode);
    InviteCode getInviteCodeByCode(String code);
}
