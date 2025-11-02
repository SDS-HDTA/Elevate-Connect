package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.project.Marker;

import java.util.List;

@Mapper
public interface MarkerMapper {
    Marker getMarkerById(Integer id);
    List<Marker> getAllByProjectId(Integer projectId);
    void insert(Marker marker);
    void update(Marker marker);
    void delete(Integer id);
}
