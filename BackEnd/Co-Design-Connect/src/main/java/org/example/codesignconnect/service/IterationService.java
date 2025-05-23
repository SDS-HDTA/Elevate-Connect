package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Iteration;

import java.util.List;

public interface IterationService {
    int createIteration(Iteration iteration);
    int updateIteration(Iteration iteration);
    int deleteIteration(Integer id);
    Iteration getIterationById(Integer id);
    List<Iteration> getIterationsByProjectId(Integer projectId);
}