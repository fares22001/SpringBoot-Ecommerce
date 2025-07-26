package com.project.project;

import java.nio.file.Path;
import java.nio.file.Paths;

 import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


 
public class MvcConfig implements WebMvcConfigurer  {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        exposeDirectory("uploads",registry);
            }
private void exposeDirectory(String dirname , ResourceHandlerRegistry registry ){
       Path uploadDir=Paths.get(dirname);
String uploadpath=uploadDir.toFile().getAbsolutePath();

registry.addResourceHandler("/"+dirname+"/**").addResourceLocations("file:/"+uploadpath);

}





}
