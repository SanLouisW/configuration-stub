package com.github.configurationstub;

import com.github.configurationstub.configuration.ClassMeta;
import com.github.configurationstub.configuration.Configuration;
import com.github.configurationstub.configuration.RootElementMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class MockTool {
    private final static Logger logger = LoggerFactory.getLogger(MockTool.class);

    public static void start() {
        if (!loadAgent()) return;
        Configuration configuration = new Configuration();
        RootElementMeta root = configuration.configure(MockTool.class.getClassLoader().getResourceAsStream("mock.properties"));
        for (ClassMeta classMeta : root.getClasses()) {
            try {
                Mock.setUpMock(classMeta);
            } catch (Exception e) {
                logger.error("Mock class {} failed", classMeta.getName(), e);
            }
        }
    }

    private static boolean loadAgent() {
        JavaAgentLoader loader = new JavaAgentLoader();
        boolean agentLoaded = loader.loadAgent();
        if (!agentLoaded) return false;
        Instrumentation inst = Mock.instrumentation();
        if (inst == null) {
            logger.error("can not get instrumentation");
            return false;
        }
        return true;
    }
}
