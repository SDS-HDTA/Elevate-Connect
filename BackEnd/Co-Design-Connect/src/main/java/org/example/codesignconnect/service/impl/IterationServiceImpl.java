package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.IterationMapper;
import org.example.codesignconnect.model.Iteration;
import org.example.codesignconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IterationServiceImpl implements IterationService {

    @Autowired
    private IterationMapper iterationMapper;

    @Override
    public int createIteration(Iteration iteration) {
        return iterationMapper.insertIteration(iteration);
    }

    @Override
    public int updateIteration(Iteration iteration) {
        return iterationMapper.updateIteration(iteration);
    }

    @Override
    public int deleteIteration(Integer id) {
        return iterationMapper.deleteIteration(id);
    }

    @Override
    public Iteration getIterationById(Integer id) {
        return iterationMapper.getIterationById(id);
    }

    @Override
    public List<Iteration> getIterationsByProjectId(Integer projectId) {
        return iterationMapper.getIterationsByProjectId(projectId);
    }
}

