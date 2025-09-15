package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.Permission;
import org.sds.elevateconnect.model.UserRole;

import java.util.Map;

public interface IPermissionsService {
    Map<UserRole, Permission[]> getMapOfRoleToPermissions();
}
