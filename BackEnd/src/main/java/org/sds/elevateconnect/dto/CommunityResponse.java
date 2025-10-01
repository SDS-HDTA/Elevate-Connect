package org.sds.elevateconnect.dto;

public class CommunityResponse {
    private Integer id;
    private String name;
    private String country;
    private Integer memberCount;
    private Integer projectCount;
    private String shortDescription;

    public CommunityResponse(Integer id, String name, String country, Integer memberCount, Integer projectCount, String shortDescription) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.memberCount = memberCount;
        this.projectCount = projectCount;
        this.shortDescription = shortDescription;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
