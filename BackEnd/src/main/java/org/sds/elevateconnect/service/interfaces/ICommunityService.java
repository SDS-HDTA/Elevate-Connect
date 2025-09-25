package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.Result;

import java.util.List;

public interface ICommunityService {
    Result createNewCommunity(String name, String country, String description);
    Community getCommunityById(Integer id);
    Community getCommunityByName(String name);
    List<Community> getAllCommunities();
    String getCountryByCommunityId(Integer id);
}
