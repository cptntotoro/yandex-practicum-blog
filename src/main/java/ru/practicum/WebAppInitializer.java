package ru.practicum;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.practicum.config.JdbcConfig;
import ru.practicum.config.WebMvcConfig;
//import ru.practicum.config.JdbcConfig;
//import ru.practicum.config.WebMvcConfig;

//@Configuration
//@EnableWebMvc
////@EnableAspectJAutoProxy
//@ComponentScan(basePackages = {"ru.practicum.controller"})
//@PropertySource("classpath:application.properties")
//public class WebAppInitializer {
////    @Override
////    public void onStartup(ServletContext servletContext) {
////        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
////        appContext.register(WebMvcConfig.class);
////        appContext.register(JdbcConfig.class);
////
////        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
////                "YPBlog", new DispatcherServlet(appContext)
////        );
////        dispatcher.setLoadOnStartup(1);
////        dispatcher.addMapping("/");
////    }
//}

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.practicum"})
@PropertySource("classpath:application.properties")
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(WebMvcConfig.class);
        appContext.register(JdbcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "SpringDispatcher", new DispatcherServlet(appContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}