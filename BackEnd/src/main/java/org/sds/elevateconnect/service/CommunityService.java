package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.CommunityMapper;
import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.ICommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class CommunityService implements ICommunityService {
    @Autowired
    private CommunityMapper communityMapper;

    @Override
    public Result createNewCommunity(String name, String country, String description) {
        Community community = getCommunityByName(name);

        if (community != null) {
            return Result.error("Community name is already taken.");
        }

        communityMapper.create(name, country, description);

        return Result.success();
    }

    @Override
    public Community getCommunityById(int id) {
        return communityMapper.getCommunityById(id);
    }

    @Override
    public Community getCommunityByName(String name) {
        return communityMapper.getCommunityByName(name);
    }

    @Override
    public List<Community> getAllCommunities() {
        return communityMapper.getAllCommunities();
    }

    @Override
    public void updateCommunity(Integer id, String name, String country, String shortDescription) {
        Community community = communityMapper.getCommunityById(id);
        if (community == null) {
            log.warn("No community found with ID: {}", id);
            return;
        }

        communityMapper.updateCommunityById(id, name, country, shortDescription);
        return;
    }
    
    public String getCountryByCommunityId(Integer id) {
        return communityMapper.getCountryByCommunityId(id);
    }
}
