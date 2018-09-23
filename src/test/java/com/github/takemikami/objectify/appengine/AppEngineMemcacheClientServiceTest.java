package com.github.takemikami.objectify.appengine;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.cache.IdentifiableValue;
import com.googlecode.objectify.cache.MemcacheService.CasPut;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppEngineMemcacheClientServiceTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testGetPut() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    svc.put("name", "value");
    assertEquals("value", svc.get("name"));
  }

  @Test
  public void testGetPutAll() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    svc.putAll(new HashMap<String, Object>() {{
      put("name1", "value1");
      put("name2", "value2");
      put("name3", "value3");
    }});

    Map<String, Object> rtn = svc.getAll(Arrays.asList("name1", "name2"));

    assertEquals("value1", rtn.get("name1"));
    assertEquals("value2", rtn.get("name2"));
    assertNull(rtn.get("name3"));
  }

  @Test
  public void testDeleteAll() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    svc.putAll(new HashMap<String, Object>() {{
      put("name1", "value1");
      put("name2", "value2");
      put("name3", "value3");
    }});
    svc.deleteAll(Arrays.asList("name1", "name2"));

    Map<String, Object> rtn = svc.getAll(Arrays.asList("name1", "name2", "name3"));

    assertNull(rtn.get("name1"));
    assertNull(rtn.get("name2"));
    assertEquals("value3", rtn.get("name3"));
  }

  @Test
  public void testGetEmptyIdentifiables() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    Map<String, IdentifiableValue> rtn = svc
        .getIdentifiables(Arrays.asList("name1", "name2"));

    assertNull(rtn.get("name1").getValue());
    assertNull(rtn.get("name2").getValue());
  }

  @Test
  public void testGetIdentifiables() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    svc.putAll(new HashMap<String, Object>() {{
      put("name1", "value1");
      put("name2", "value2");
      put("name3", "value3");
    }});

    Map<String, IdentifiableValue> rtn = svc
        .getIdentifiables(Arrays.asList("name1", "name2"));

    assertEquals("value1", rtn.get("name1").getValue());
    assertEquals("value2", rtn.get("name2").getValue());
  }

  @Test
  public void testPutIfUntouched() {
    AppEngineMemcacheClientService svc = new AppEngineMemcacheClientService();
    svc.putAll(new HashMap<String, Object>() {{
      put("name1", "value1");
      put("name2", "value2");
      put("name3", "value3");
    }});

    Map<String, IdentifiableValue> rtn1 = svc
        .getIdentifiables(Arrays.asList("name1", "name2"));

    assertEquals("value1", rtn1.get("name1").getValue());
    assertEquals("value2", rtn1.get("name2").getValue());

    svc.putIfUntouched(new HashMap<String, CasPut>() {{
      put("name1", new CasPut(rtn1.get("name1"), "value11", 0));
      put("name2", new CasPut(rtn1.get("name2"), "value21", 0));
    }});

    Map<String, Object> rtn2 = svc.getAll(Arrays.asList("name1", "name2"));

    assertEquals("value11", rtn2.get("name1"));

    assertEquals("value21", rtn2.get("name2"));
  }

}
