package com.emily.infrastructure.sample.web.service.impl;

import com.emily.infrastructure.sample.web.mapper.oracle.OracleMapper;
import com.emily.infrastructure.sample.web.service.OracleService;
import org.springframework.stereotype.Service;

/**
 * @author Emily
 * @program: spring-parent
 * @since 2022/01/17
 */
@Service
public class OracleServiceImpl implements OracleService {

    private final OracleMapper oracleMapper;

    public OracleServiceImpl(OracleMapper oracleMapper) {
        this.oracleMapper = oracleMapper;
    }

    @Override
    public String getOracle() {
        return oracleMapper.getOracle();
    }

    @Override
    public String getTarget(String param) {
        return oracleMapper.getOracle();
    }
}
