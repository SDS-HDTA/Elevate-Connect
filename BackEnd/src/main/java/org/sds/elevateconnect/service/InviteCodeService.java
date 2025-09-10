package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.InviteCodeMapper;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.UserRole;
import org.sds.elevateconnect.service.interfaces.IInviteCodeService;
import org.sds.elevateconnect.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InviteCodeService implements IInviteCodeService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private InviteCodeMapper inviteCodeMapper;

    @Override
    public Result generateCode(String email, int typeAsInt) {
        String code;

        do {
            code = CodeGenerator.generateInviteCode(8);
        } while (inviteCodeMapper.getInviteCodeByCode(code) != null);

        try {
            InviteCode inviteCode = new InviteCode();

            inviteCode.setEmail(email);
            inviteCode.setCode(code);
            inviteCode.setType(UserRole.fromInt(typeAsInt));

            inviteCodeMapper.addCode(inviteCode);
            emailService.sendInviteCode(inviteCode);
        } catch (Exception e) {
            return Result.error("Error generating invite code. Please try again.");
        }

        return Result.success();
    }

    @Override
    public void deactivateCode(InviteCode code) {
        inviteCodeMapper.deactivateCode(code);
    }

    @Override
    public InviteCode getInviteCodeByCode(String code) {
        return inviteCodeMapper.getInviteCodeByCode(code);
    }
}
