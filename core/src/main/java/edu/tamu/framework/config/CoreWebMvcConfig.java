package edu.tamu.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class CoreWebMvcConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Autowired
    private Environment env;

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
       return new ResourceUrlEncodingFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       boolean devMode = this.env.acceptsProfiles("dev");
       boolean useResourceCache = !devMode;
       Integer cachePeriod = devMode ? 0 : null;

       registry.addResourceHandler("/static/**")
          .addResourceLocations("classpath:/static/")
          .setCachePeriod(cachePeriod)
          .resourceChain(useResourceCache)
          .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
          .addTransformer(new AppCacheManifestTransformer());
    }

}