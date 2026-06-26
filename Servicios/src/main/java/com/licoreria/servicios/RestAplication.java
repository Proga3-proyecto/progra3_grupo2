package com.licoreria.servicios;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath("/api")
public class RestAplication extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> classes = new HashSet<>();
//        classes.add(ObjectMapperContextResolver.class);
//        classes.add(ProductosRS.class);
//        return classes;
//    }
}