package org.sds.elevateconnect.model.project;

import java.time.LocalDateTime;

public record ProjectMember (Integer id, Integer projectId, Integer userId, LocalDateTime joinedTime) { }