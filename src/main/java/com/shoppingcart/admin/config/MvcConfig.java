
package com.shoppingcart.admin.config;

import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("user-photos",registry);
	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern + "/**";
		
		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
	}
}