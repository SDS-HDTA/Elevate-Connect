package org.sds.elevateconnect.model.auth;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.sds.elevateconnect.utils.Constants;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

@Getter
public enum Permission implements GrantedAuthority {
    ACCESS_ACTIVITIES_PAGE("access:tasks"),
    ACCESS_DISCOVER_PAGE("access:discover"),
    ACCESS_INFO_PAGE("access:info"),
    ACCESS_ADMIN_PANEL_PAGE("access:admin_panel"),
    ACCESS_MAP_PAGE("access:map"),
    ACCESS_MY_PROJECTS_PAGE("access:my_projects"),
    ACCESS_PARTICIPANTS_PAGE("access:participants"),
    ACCESS_POSTS_PAGE("access:posts"),
    ACCESS_RESOURCES_PAGE("access:resources"),
    ALL_PERMISSIONS("admin:all_permissions"),
    CREATE_COMMUNITY("create:community"),
    CREATE_MAP_MARKER("create:map_marker"),
    CREATE_NEW_POST("create:post"),
    CREATE_NEW_REPLY("create:reply"),
    CREATE_NEW_TASK("create:task"),
    CREATE_PROJECT("create:project"),
    DELETE_FILE("delete:file"),
    DELETE_MAP_MARKER("delete:map_marker"),
    DELETE_PROJECT("delete:project"),
    DELETE_TASK("delete:task"),
    DELETE_USER("delete:user"),
    EDIT_COMMUNITY("edit:community"),
    EDIT_MAP_MARKER("edit:map_marker"),
    EDIT_POST("edit:post"),
    EDIT_PROJECT("edit:project"),
    EDIT_TASK("edit:task"),
    EDIT_USER("edit:user"),
    INVITE_USER("invite:user"),
    MODIFY_CURRENT_PROJECT_STAGE("modify:current_project_stage"),
    UPLOAD_FILE("upload:file");

    @JsonValue
    private final String stringValue;


    Permission(String stringValue) {
        this.stringValue = stringValue;
    }

    public static final Permission[] communityInsightPartnerPermissions = new Permission[] {
            ACCESS_ACTIVITIES_PAGE,
            ACCESS_INFO_PAGE,
            ACCESS_MAP_PAGE,
            ACCESS_MY_PROJECTS_PAGE,
            ACCESS_PARTICIPANTS_PAGE,
            ACCESS_POSTS_PAGE,
            ACCESS_RESOURCES_PAGE,
            CREATE_MAP_MARKER,
            CREATE_NEW_POST,
            CREATE_NEW_REPLY,
            CREATE_NEW_TASK,
            DELETE_FILE,
            DELETE_MAP_MARKER,
            EDIT_MAP_MARKER,
            EDIT_TASK,
            MODIFY_CURRENT_PROJECT_STAGE,
            UPLOAD_FILE
    };

    // These two roles effectively have the same permissions
    public static final Permission[] countryCollaborationPartnerPermissions = communityInsightPartnerPermissions;

    public static final Permission[] humanitarianImpactPartnerPermissions = new Permission[] {
            ACCESS_ACTIVITIES_PAGE,
            ACCESS_DISCOVER_PAGE,
            ACCESS_INFO_PAGE,
            ACCESS_MAP_PAGE,
            ACCESS_MY_PROJECTS_PAGE,
            ACCESS_PARTICIPANTS_PAGE,
            ACCESS_POSTS_PAGE,
            ACCESS_RESOURCES_PAGE
    };

    public static final Permission[] elevateFacilitationLeadPermissions = new Permission[] {
            ALL_PERMISSIONS
    };

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public String getAuthority() {
        return this.toString();
    }

    public String getValue() {
        return this.toString();
    }

    public static Map<UserRole, Permission[]> getRolePermissionsMap() {
        return Constants.rolePermissionsMap;
    }
}
