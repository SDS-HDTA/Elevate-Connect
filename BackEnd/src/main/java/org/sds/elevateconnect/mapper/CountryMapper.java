package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper {
    String[] getAllCountries();
}
