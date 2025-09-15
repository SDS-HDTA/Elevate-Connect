package org.sds.elevateconnect.service;

import org.sds.elevateconnect.model.Permission;
import org.sds.elevateconnect.model.UserRole;
import org.sds.elevateconnect.service.interfaces.IPermissionsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PermissionsService implements IPermissionsService {
    @Override
    public Map<UserRole, Permission[]> getMapOfRoleToPermissions() {
        Map<UserRole, Permission[]> roleToPermMap = new HashMap<>();

        for (UserRole role : UserRole.values()) {
            roleToPermMap.put(role, role.getPermissions());
        }

        return roleToPermMap;
    }
}
