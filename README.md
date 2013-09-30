configuration-stub
==================

A configuration stub for java

USAGE:

* Place a mock.properties to your classpath:

\\# mock.properties

\\# You want to stub these methods of UserService
com.github.configurationstub.example.UserService.methods=registerUser
\\# The UserServiceInvoker is custom stub for registerUser
com.github.configurationstub.example.UserService.registerUser.invoker=com.github.configuration.example.UserServiceInvoker

* Write your custom stub class

```java
package com.github.configuration.example;

import com.github.configurationstub.Invoker;

import java.lang.reflect.Method;
import java.util.Map;

public class UserServiceInvoker extends Invoker {
    @Override
    public Object invoke(Object self, Method method, Map<String, Object> args) {
        System.out.println("stub for user service registerUeser method");
        System.out.println("args:");
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue().toString());
        }
        return null;
    }
}

```

* Start stub

```java
    @Test
    public void create_configuration_stub_for_given_method() {
        UserService service = new UserService();
        service.registerUser("admin"); // call real method

        //start stub
        MockTool.start();

        service.registerUser("admin"); //call stub method
    }

```
