package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.codesignconnect.model.Iteration;

import java.util.List;

@Mapper
public interface IterationMapper {
    int insertIteration(Iteration iteration);
    int updateIteration(Iteration iteration);
    int deleteIteration(@Param("id") Integer id);
    Iteration getIterationById(@Param("id") Integer id);
    List<Iteration> getIterationsByProjectId(@Param("projectId") Integer projectId);
}