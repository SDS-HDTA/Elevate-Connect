package org.sds.elevateconnect.dto;

public record CreateProjectRequest(int creatorId, int communityId, String name, String description, int category, String targetDate) { }
