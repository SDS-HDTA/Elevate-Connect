package org.sds.elevateconnect.config.security;

public record JwtClaims(String name, Integer role, Integer userId) {
}
