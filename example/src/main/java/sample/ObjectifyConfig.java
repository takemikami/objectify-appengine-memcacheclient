package sample;

import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.MemcacheService;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectifyConfig {

  final static String MEMCACHE_SERVICE = "com.github.takemikami.objectify.appengine.AppEngineMemcacheClientService";

  @Bean
  public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
    final FilterRegistrationBean<ObjectifyFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new ObjectifyFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(1);
    return registration;
  }

  @Bean
  public ServletListenerRegistrationBean<ObjectifyListener> listenerRegistrationBean() {
    ServletListenerRegistrationBean<ObjectifyListener> bean =
        new ServletListenerRegistrationBean<>();
    bean.setListener(new ObjectifyListener());
    return bean;
  }

  public class ObjectifyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
      try {
        ObjectifyService.init(new ObjectifyFactory(
            DatastoreOptions.newBuilder().setHost("http://localhost:8484")
                .setProjectId("my-project")
                .build().getService(),
            (MemcacheService) Class.forName(MEMCACHE_SERVICE).getDeclaredConstructor().newInstance()
        ));
      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }

      ObjectifyService.register(ItemEntity.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

  }

}