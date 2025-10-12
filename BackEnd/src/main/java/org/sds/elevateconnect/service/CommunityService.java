package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.CommunityResponse;
import org.sds.elevateconnect.exceptions.CommunityException;
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
    public Community getCommunityById(Integer id) {
        return communityMapper.getCommunityById(id);
    }

    @Override
    public Community getCommunityByName(String name) {
        return communityMapper.getCommunityByName(name);
    }

    @Override
    public List<CommunityResponse> getAllCommunities() {
         List<Community> communities = communityMapper.getAllCommunities();

        return communities.stream()
            .map(c -> new CommunityResponse(
                    c.id(),
                    c.name(),
                    c.country(),
                    communityMapper.countMembersByCommunityId(c.id()),
                    communityMapper.countProjectsByCommunityId(c.id()),
                    c.shortDescription()
            ))
            .toList();
    }

    @Override
    public void updateCommunity(Integer id, String name, String shortDescription) {
        Community community = communityMapper.getCommunityById(id);
        if (community == null) {
            throw new CommunityException("No community found with ID: " + id);
        }

        communityMapper.updateCommunityById(id, name, shortDescription);
    }
    
    public String getCountryByCommunityId(Integer id) {
        return communityMapper.getCountryByCommunityId(id);
    }
}
