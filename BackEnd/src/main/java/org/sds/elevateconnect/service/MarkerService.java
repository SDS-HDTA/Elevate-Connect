package org.sds.elevateconnect.service;

import org.sds.elevateconnect.model.Marker;
import org.sds.elevateconnect.model.Result;

import java.util.List;

public interface MarkerService {
    List<Marker> findAllByProjectId(Integer projectId);

    void insert(Marker marker);

    void update(Marker marker);

    void delete(Integer id);
}
