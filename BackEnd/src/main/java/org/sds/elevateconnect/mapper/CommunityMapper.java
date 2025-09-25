package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.Community;

import java.util.List;

@Mapper
public interface CommunityMapper {
    void create(String name, String country, String shortDescription);
    Community getCommunityById(Integer id);
    Community getCommunityByName(String name);
    List<Community> getAllCommunities();
    void updateCommunityById(Integer id, String name, String country, String shortDescription);
    String getCountryByCommunityId(Integer id);
}
