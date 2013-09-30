package com.github.configurationstub.example;

import com.github.configuration.example.UserService;
import com.github.configurationstub.MockTool;
import org.junit.Test;

/**
 * User: zhaohuiyu
 * Date: 9/30/13
 * Time: 10:22 PM
 */
public class StubTest {
    @Test
    public void create_configuation_stub_for_given_method() {
        UserService service = new UserService();
        service.registerUser("admin");
        MockTool.start();

        service.registerUser("admin");
    }
}
