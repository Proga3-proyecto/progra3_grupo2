package com.licoreria.servicios;

import com.licoreria.servicios.config.ObjectMapperContextResolver;
import com.licoreria.servicios.servicios.catologo.ProductosRS;  // Añade este import
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

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