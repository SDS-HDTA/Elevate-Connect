package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.FolderResponse;
import org.sds.elevateconnect.dto.IterationDetail;
import org.sds.elevateconnect.model.project.Iteration;

import java.util.List;

public interface IterationService {
    int createIteration(Iteration iteration);
    int updateIteration(Iteration iteration);
    int deleteIteration(Integer id);
    List<IterationDetail> getIterations(Integer id, Short status);
    List<Iteration> getIterationsByProjectId(Integer projectId);
    List<FolderResponse> getFolders(Integer projectId);
}