package com.licoreria.servicios.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;  // ← Import necesario
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new BlackbirdModule());
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        System.out.println(">>> USANDO OBJECTMAPPER CON BLACKBIRD");
        return mapper;
    }
}