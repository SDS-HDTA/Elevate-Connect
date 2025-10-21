package org.sds.elevateconnect.service;

import org.sds.elevateconnect.exceptions.InviteCodeException;
import org.sds.elevateconnect.mapper.InviteCodeMapper;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.service.interfaces.IEmailService;
import org.sds.elevateconnect.service.interfaces.IInviteCodeService;
import org.sds.elevateconnect.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InviteCodeService implements IInviteCodeService {
    @Autowired
    private IEmailService emailService;

    @Autowired
    private InviteCodeMapper inviteCodeMapper;

    @Override
    public void generateCode(String email, int roleAsInt, Integer communityId, String country, String organization) {
        try {
            String code;

            do {
                code = CodeGenerator.generateInviteCode(8);
            } while (inviteCodeMapper.getInviteCodeByCode(code) != null);

            InviteCode inviteCode = new InviteCode();

            inviteCode.setEmail(email);
            inviteCode.setCode(code);
            inviteCode.setUserRole(UserRole.fromInt(roleAsInt));
            inviteCode.setCommunityId(communityId);
            inviteCode.setCountry(country);
            inviteCode.setOrganization(organization);

            inviteCodeMapper.addCode(inviteCode);
            emailService.sendInviteCode(inviteCode);
        } catch (Exception e) {
            throw new InviteCodeException("Error creating invite code.");
        }
    }

    @Override
    public void deleteCodeByEmail(String email) {
        inviteCodeMapper.deleteCodeByEmail(email);
    }

    @Override
    public InviteCode getInviteCodeByCode(String code) {
        return inviteCodeMapper.getInviteCodeByCode(code);
    }
}
