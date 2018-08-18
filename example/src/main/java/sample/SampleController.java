package sample;

import com.googlecode.objectify.ObjectifyService;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @RequestMapping("/")
  public String index() {
    return "Local Date Time: " + LocalDateTime.now().toString()
        + "<ul>"
        + "<li><a href='/put'>put entity</a>"
        + "<li><a href='/get'>get entity</a>"
        + "<li><a href='/del'>del entity</a>"
        + "</ul>";
  }

  @RequestMapping("/put")
  public String put() {
    ObjectifyService.ofy().save().entity(new ItemEntity("001", "name", "desc"));

    return "put: id=001, name=desc"
        + "<br/><a href='/'>top</a>";
  }

  @RequestMapping("/get")
  public String get() {
    ItemEntity e = ObjectifyService.ofy().cache(true).load().type(ItemEntity.class).id("001").now();
    if (e != null) {
      return "get:" + String.format("%s: %s - %s", e.getId(), e.getName(), e.getDescription())
          + "<br/><a href='/'>top</a>";
    }

    return "no date.<br/><a href='/'>top</a>";
  }

  @RequestMapping("/del")
  public String del() {
    ItemEntity e = ObjectifyService.ofy().cache(true).load().type(ItemEntity.class).id("001").now();
    if (e != null) {
      ObjectifyService.ofy().delete().entity(e);
      return "delete: id=001"
          + "<br/><a href='/'>top</a>";
    }

    return "no date.<br/><a href='/'>top</a>";
  }

}
