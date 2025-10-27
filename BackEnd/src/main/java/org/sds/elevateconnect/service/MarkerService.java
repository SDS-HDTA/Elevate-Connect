package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.CreateMarkerRequest;
import org.sds.elevateconnect.dto.UpdateMarkerRequest;
import org.sds.elevateconnect.mapper.MarkerMapper;
import org.sds.elevateconnect.model.project.Marker;
import org.sds.elevateconnect.model.project.MarkerType;
import org.sds.elevateconnect.service.interfaces.IMarkerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MarkerService implements IMarkerService {
    @Autowired
    private MarkerMapper markerMapper;

    @Override
    public List<Marker> getAllByProjectId(Integer projectId) {
        return markerMapper.getAllByProjectId(projectId);
    }

    @Override
    public Marker insert(CreateMarkerRequest request) {
        Marker newMarker = Marker.builder()
                .projectId(request.getProjectId())
                .lat(request.getLat())
                .lng(request.getLng())
                .title(request.getTitle())
                .description(request.getDescription())
                .type(MarkerType.fromInt(request.getType()))
                .build();

        // Mapper will insert generated id field into newMarker
        markerMapper.insert(newMarker);

        return newMarker;
    }

    @Override
    public void update(UpdateMarkerRequest request) {
        Marker marker = markerMapper.getMarkerById(request.getId());

        if (request.getTitle() != null)
            marker.setTitle(request.getTitle());

        if (request.getDescription() != null)
            marker.setDescription(request.getDescription());

        if (request.getType() != null)
            marker.setType(request.getType());

        markerMapper.update(marker);
    }

    @Override
    public void delete(Integer id) {
        markerMapper.delete(id);
    }
}
