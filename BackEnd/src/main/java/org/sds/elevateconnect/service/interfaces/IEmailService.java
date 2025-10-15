package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;

public interface IEmailService {
    Result sendVerificationCode(String email);

    void sendInviteCode(InviteCode inviteCode);
}
