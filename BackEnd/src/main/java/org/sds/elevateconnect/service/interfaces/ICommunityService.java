package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.Result;

import java.util.List;

public interface ICommunityService {
    Result createNewCommunity(String name, String country, String description);
    Community getCommunityByName(String name);
    List<Community> getAllCommunities();
    Community getCommunityById(int id);
    void updateCommunity(Integer id, String name, String country, String shortDescription);
    String getCountryByCommunityId(Integer id);
}
