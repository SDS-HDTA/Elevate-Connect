package org.sds.elevateconnect.service;

import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;

public interface EmailService {
    Result sendVerificationCode(String email);

    void sendInviteCode(InviteCode inviteCode);
}
