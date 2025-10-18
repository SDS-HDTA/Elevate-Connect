package org.sds.elevateconnect.dto;

public record FileUploadRequest(Integer iterationId, Integer creatorId, String name, Integer type) { }