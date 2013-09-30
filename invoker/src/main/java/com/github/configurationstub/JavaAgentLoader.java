package com.github.configurationstub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * User: zhaohuiyu
 * Date: 8/30/12
 * Time: 2:07 PM
 */
public class JavaAgentLoader {
    private final static Logger logger = LoggerFactory.getLogger(JavaAgentLoader.class);

    public boolean loadAgent() {
        JDK6AgentLoader loader = new JDK6AgentLoader(getAgentPath());
        try {
            loader.loadAgent();
        } catch (Exception e) {
            logger.error("can not load agent", e);
            return false;
        }
        return true;
    }

    private String getAgentPath() {
        ProtectionDomain domain = AgentMain.class.getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        return source.getLocation().getPath();
    }
}
