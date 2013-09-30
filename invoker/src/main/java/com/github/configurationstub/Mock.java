package com.github.configurationstub;

import com.github.configurationstub.codegenerator.ClassGenerator;
import com.github.configurationstub.configuration.ClassMeta;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * User: zhaohuiyu
 * Date: 8/23/12
 * Time: 6:38 PM
 */
public class Mock {
    private final static Logger logger = LoggerFactory.getLogger(Mock.class);

    public static void setUpMock(ClassMeta classMeta) throws ClassNotFoundException, UnmodifiableClassException, CannotCompileException, NotFoundException {
        Class realClass = forName(classMeta.getName());
        if (isNotConcreteClass(realClass)) {
            throw new RuntimeException("Can't mock interface or abstract class.");
        }
        ClassGenerator generator = new ClassGenerator();
        byte[] bytes = generator.generate(classMeta);
        ClassDefinition classDefinition = new ClassDefinition(realClass, bytes);
        String className = realClass.getCanonicalName();
        logger.info("start redefine class {}", className);
        instrumentation().redefineClasses(classDefinition);
        logger.info("end  redefine class {}", className);
    }

    private static Class forName(String realClassName) {
        try {
            return Class.forName(realClassName);
        } catch (ClassNotFoundException e) {
            logger.error("class not found {}", realClassName, e);
            return null;
        }
    }

    private static boolean isNotConcreteClass(Class clazz) {
        if (clazz.isInterface()) return true;
        int modifiers = clazz.getModifiers();
        return Modifier.isAbstract(modifiers);
    }

    public static Instrumentation instrumentation() {
        ClassLoader mainAppLoader = ClassLoader.getSystemClassLoader();
        try {
            Class<?> javaAgentClass = mainAppLoader.loadClass(AgentMain.class.getCanonicalName());
            Method method = javaAgentClass.getDeclaredMethod("instrumentation", new Class[0]);
            return (Instrumentation) method.invoke(null, new Object[0]);
        } catch (Exception e) {
            logger.error("can not get agent class", e);
            return null;
        }
    }
}
