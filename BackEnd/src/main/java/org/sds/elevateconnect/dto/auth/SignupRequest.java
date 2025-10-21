package org.sds.elevateconnect.dto.auth;

public record SignupRequest(String firstName, String lastName, String email, String password, Integer role, Integer communityId, String country, String phone, String organization) {
}
