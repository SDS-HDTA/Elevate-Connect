package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;

public interface IInviteCodeService {
    Result generateCode(String email, int intRole);
    void deactivateCode(InviteCode inviteCode);
    InviteCode getInviteCodeByCode(String code);
}
    