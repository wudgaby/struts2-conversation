package com.github.overengineer.scope.session;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.github.overengineer.scope.mocks.actions.MockPojoController;
import com.github.overengineer.scope.struts2.StrutsScopeConstants;
import com.github.overengineer.scope.testutil.SerializationTestingUtil;
import com.github.overengineer.scope.testutil.StrutsScopeTestCase;
import com.github.overengineer.scope.testutil.TestConstants;
import com.google.code.struts2.test.junit.StrutsConfiguration;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.inject.Inject;

@StrutsConfiguration(locations = "struts.xml")
public class DefaultSessionConfigurationProviderTest extends
        StrutsScopeTestCase<Object> {

    @Inject("defaultSessionConfigurationProvider")
    SessionConfigurationProvider provider;

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetSessionFieldConfig() {
        SessionConfiguration config = provider
                .getSessionConfiguration(MockPojoController.class);
        assertNotNull(config);
        for (Class clazz : TestConstants.SESSION_FIELD_ACTION_CLASSES) {
            String failMessage = "SessionConfigurationProvider failed to provide config for class:  "
                    + clazz.getName();
            assertNotNull(failMessage, config.getFields(clazz));
        }
        for (Class clazz : TestConstants.NO_SESSION_FIELD_ACTION_CLASSES) {
            String failMessage = "SessionConfigurationProvider erroneously provided config for class:  "
                    + clazz.getName();
            assertNull(failMessage, config.getFields(clazz));
        }

    }

    @Test
    public void testSessionFieldNaming() throws Exception {

        SessionConfiguration config = provider
                .getSessionConfiguration(MockPojoController.class);
        assertNotNull(config);
        Set<String> fieldNames = config.getFields(MockPojoController.class)
                .keySet();
        assertTrue(fieldNames.contains("java.lang.String.sessionField"));

        // this next assertion isn't really related but i did it for the shit of
        // it
        request.addParameter("sessionField", "hola");
        ActionProxy proxy = this.getActionProxy("begin");
        proxy.execute();

        @SuppressWarnings("unchecked")
        Map<String, Object> sessionFieldMap = (Map<String, Object>) session
                .get(StrutsScopeConstants.SESSION_FIELD_MAP_KEY);
        assertTrue(sessionFieldMap
                .containsKey("java.lang.String.sessionString"));
    }

    @Test
    public void testSerialization() throws Exception {
        provider.getSessionConfiguration(MockPojoController.class).getFields(
                MockPojoController.class);
        provider = SerializationTestingUtil.getSerializedCopy(provider);
        provider.getSessionConfiguration(MockPojoController.class).getFields(
                MockPojoController.class);
    }

}