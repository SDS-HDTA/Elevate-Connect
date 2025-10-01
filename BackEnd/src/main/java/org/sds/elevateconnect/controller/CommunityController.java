package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.dto.NewCommunityRequest;
import org.sds.elevateconnect.dto.UpdateCommunityRequest;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @PostMapping("/create")
    public Result createCommunity(@RequestBody NewCommunityRequest newCommunityRequest) {
        return communityService.createNewCommunity(newCommunityRequest.name(), newCommunityRequest.country(), newCommunityRequest.description());
    }

    @GetMapping
    public Result getAllCommunities() {
        return Result.success(communityService.getAllCommunities());
    }

    @PutMapping
    public Result updateCommunity(@RequestBody UpdateCommunityRequest updateRequest) {
       communityService.updateCommunity(updateRequest.getId(), updateRequest.getName(), updateRequest.getShortDescription());
       return Result.success();
    }
}
