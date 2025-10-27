package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.CreateMarkerRequest;
import org.sds.elevateconnect.dto.UpdateMarkerRequest;
import org.sds.elevateconnect.model.project.Marker;

import java.util.List;

public interface IMarkerService {
    Marker insert(CreateMarkerRequest request);
    List<Marker> getAllByProjectId(Integer projectId);
    void update(UpdateMarkerRequest request);
    void delete(Integer id);
}
