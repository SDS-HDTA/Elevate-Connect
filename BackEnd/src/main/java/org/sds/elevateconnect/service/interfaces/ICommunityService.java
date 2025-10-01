package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.CommunityResponse;
import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.Result;

import java.util.List;

public interface ICommunityService {
    Result createNewCommunity(String name, String country, String description);
    Community getCommunityByName(String name);
    List<CommunityResponse> getAllCommunities();
    Community getCommunityById(Integer id);
    void updateCommunity(Integer id, String name, String shortDescription);
    String getCountryByCommunityId(Integer id);
}
