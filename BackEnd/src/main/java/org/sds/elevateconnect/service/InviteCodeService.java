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
    public Result generateCode(String email, int roleAsInt) {
        String code;

        do {
            code = CodeGenerator.generateInviteCode(8);
        } while (inviteCodeMapper.getInviteCodeByCode(code) != null);

        try {
            InviteCode inviteCode = new InviteCode();

            inviteCode.setEmail(email);
            inviteCode.setCode(code);
            inviteCode.setUserRole(UserRole.fromInt(roleAsInt));

            inviteCodeMapper.addCode(inviteCode);
            emailService.sendInviteCode(inviteCode);
        } catch (Exception e) {
            System.out.println("Error generating invite code: " + e.getMessage());
            return Result.error("Error generating invite code. Please try again.");
        }

        return Result.success();
    }

    @Override
    public void deleteCode(InviteCode code) {
        inviteCodeMapper.deleteCode(code);
    }

    @Override
    public InviteCode getInviteCodeByCode(String code) {
        return inviteCodeMapper.getInviteCodeByCode(code);
    }
}
