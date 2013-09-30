package com.github.configurationstub;

import java.lang.instrument.Instrumentation;

/**
 * User: zhaohuiyu
 * Date: 8/30/12
 * Time: 12:33 PM
 */
public class AgentMain {
    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        inst = instrumentation;
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        inst = instrumentation;
    }

    public static Instrumentation instrumentation() {
        return inst;
    }
}
