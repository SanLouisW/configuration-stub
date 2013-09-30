package com.github.configurationstub.codegenerator;

import com.github.configurationstub.MethodWrapper;
import com.github.configurationstub.configuration.ClassMeta;
import com.github.configurationstub.configuration.MethodMeta;
import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: zhaohuiyu
 * Date: 8/23/12
 * Time: 6:38 PM
 */
public class ClassGenerator {
    private final static Logger logger = LoggerFactory.getLogger(ClassGenerator.class);

    public byte[] generate(ClassMeta classMeta) throws CannotCompileException, NotFoundException {
        logger.debug("start mock class for {}", classMeta.getName());
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = getClass(pool, classMeta.getName());
        ctClass.defrost();
        List<MethodMeta> mockMethods = classMeta.getMethods();

        replaceMethods(ctClass, mockMethods);

        return toBytes(ctClass);
    }

    private void replaceMethods(CtClass ctClass, List<MethodMeta> mockMethods) throws CannotCompileException, NotFoundException {
        removeRealMethods(ctClass, mockMethods);
        addRedefinedMethods(ctClass, mockMethods);
    }

    private void removeRealMethods(CtClass ctClass, List<MethodMeta> methodMetas) {
        ClassFile classFile = ctClass.getClassFile();
        List<MethodInfo> ctMethods = classFile.getMethods();
        CtMethod[] methods = ctClass.getMethods();
        try {
            for (MethodMeta methodMeta : methodMetas) {
                removeRealMethod(methods, ctMethods, methodMeta);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeRealMethod(CtMethod[] ctMethods, List<MethodInfo> methods, MethodMeta methodMeta) throws NotFoundException {
        for (CtMethod method : ctMethods) {
            MethodWrapper methodWrapper = new MethodWrapper(method);
            if (methodMeta.match(methodWrapper)) {
                methodMeta.setMethod(method);
                methods.remove(method.getMethodInfo());
            }
        }
    }

    private void addRedefinedMethods(CtClass ctClass, List<MethodMeta> mockMethods) throws CannotCompileException, NotFoundException {
        for (MethodMeta mockMethod : mockMethods) {
            MethodCodeGenerator generator = new MockMethodCodeGenerator(mockMethod);
            CtMethod ctMethod = CtNewMethod.make(generator.code(), ctClass);
            ctClass.addMethod(ctMethod);
        }
    }

    private CtClass getClass(ClassPool pool, String className) {
        try {
            Class clazz = forName(className);
            ClassClassPath classPath = new ClassClassPath(clazz);
            pool.insertClassPath(classPath);
            return pool.get(clazz.getCanonicalName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Class forName(String realClassName) {
        try {
            return Class.forName(realClassName);
        } catch (ClassNotFoundException e) {
            logger.error("class not found {}", realClassName, e);
            return null;
        }
    }

    private byte[] toBytes(CtClass ctClass) {
        try {
            return ctClass.toBytecode();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
