package org.sds.elevateconnect.model;

import java.time.LocalDateTime;

public record Community(int id, String name, String country, String shortDescription, LocalDateTime createTime) {
}
