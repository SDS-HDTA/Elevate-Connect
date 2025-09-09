package org.sds.elevateconnect.service.impl;

import org.sds.elevateconnect.mapper.MarkerMapper;
import org.sds.elevateconnect.model.Marker;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.MarkerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MarkerServiceImpl implements MarkerService {
    @Autowired
    private MarkerMapper markerMapper;

    @Override
    public List<Marker> findAllByProjectId(Integer projectId) {
        return markerMapper.findAllByProjectId(projectId);
    }

    @Override
    public void insert(Marker marker) {
        markerMapper.insert(marker);
    }

    @Override
    public void update(Marker marker) {
        markerMapper.update(marker);
    }

    @Override
    public void delete(Integer id) {
        markerMapper.delete(id);
    }
}
