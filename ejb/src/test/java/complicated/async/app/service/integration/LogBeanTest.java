package complicated.async.app.service.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import complicated.async.app.model.Log;
import complicated.async.app.service.LogBean;
import complicated.async.app.utils.Resources;

@RunWith(Arquillian.class)
public class LogBeanTest {
  @Inject
  private LogBean bean;

  @Deployment(name = "logtest.jar", managed = true)
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class, "logtest.jar")
        .addClasses(LogBean.class, Resources.class)
        .addPackage("complicated.async.app.model")
        .addAsResource("complicated-async-app-ds.xml", "META-INF/complicated-async-app-ds.xml")
        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
        .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Test
  public void should_insert_log() throws IOException {
    Log entity = bean.insert("test");
    assertNotNull(entity);
  }

  @Test
  public void should_find_logs() {
    List<Log> actual = bean.listAll();
    assertNotNull(actual);
  }

}
