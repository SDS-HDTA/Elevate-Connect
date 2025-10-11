package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.InviteCode;

public interface IInviteCodeService {
    void generateCode(String email, int roleAsInt, String country, String organization);
    void deleteCodeByEmail(String email);
    InviteCode getInviteCodeByCode(String code);
}
    