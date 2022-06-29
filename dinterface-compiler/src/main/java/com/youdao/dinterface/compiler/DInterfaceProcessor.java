package com.youdao.dinterface.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.youdao.dinterface.annotation.DInterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 有道精品课
 * dinterface
 * Description:
 * Created by Disguiser on 2022/4/12 14:25
 * Copyright @ 2022 网易有道. All rights reserved.
 **/
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DInterfaceProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager msg;
    private String moduleName = "";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        msg = processingEnvironment.getMessager();
        Map<String, String> options = processingEnv.getOptions();
        if (options != null && !options.isEmpty()) {
            moduleName = options.get("DINTERFACE_MODULE_NAME");
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(DInterface.class.getCanonicalName());
    }

    Map<String, List<Element>> elementMap = new HashMap<String, List<Element>>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations == null || annotations.size() <=0) {
            return false;
        }
        Set<? extends Element> dInterfaceElements = roundEnv.getElementsAnnotatedWith(DInterface.class);
        List<Element> elementList;
        // 根据groupName 分类
        for (Element element: dInterfaceElements) {
            elementList = elementMap.get(element.getAnnotation(DInterface.class).groupName());
            if (elementList == null) {
                elementList = new ArrayList<Element>();
            }
            elementList.add(element);
            elementMap.put(element.getAnnotation(DInterface.class).groupName(), elementList);
        }
        // 生成文件
        generateFile();
        return false;
    }

    private void generateFile() {
        for (String groupName :elementMap.keySet()) {
            List<Element> elementList = elementMap.get(groupName);
            generateInterfaceFile(groupName, elementList);
            generateManagerFile(groupName, elementList);
        }
    }

    private void generateManagerFile(String groupName, List<Element> elementList) {
        BufferedWriter writer = null;
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(generateManagerFileName(groupName));
            writer = new BufferedWriter(sourceFile.openWriter());
            writer.write("package com.youdao.dinterface.core;\n");
            writer.write("import com.youdao.dinterface.core." +generateInterfaceFileName(groupName) + ";\n" );
            writer.write("public class " + generateManagerFileName(groupName) + " { \n");
            writer.write("    private static " + generateInterfaceFileName(groupName) + " mInterface; \n\n");
            writer.write("    public static void setInterface(" + generateInterfaceFileName(groupName) + " inter) {\n");
            writer.write("        mInterface = inter;\n");
            writer.write("    }\n\n");
            writer.write("    public static " + generateInterfaceFileName(groupName) + " getInterface() {\n");
            writer.write("        return mInterface;\n");
            writer.write("    }\n");
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //Silent
                }
            }
        }
    }

    private void generateInterfaceFile(String groupName, List<Element> elementList) {

        // 创建接口文件
        TypeSpec.Builder interfaceFileBuilder = TypeSpec.interfaceBuilder(generateInterfaceFileName(groupName))
                .addModifiers(Modifier.PUBLIC);

        for (Element element : elementList) {
            interfaceFileBuilder.addSuperinterface(ClassName.get((TypeElement) element));
        }

        JavaFile javaFile = JavaFile.builder("com.youdao.dinterface.core", interfaceFileBuilder.build()).build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String generateManagerFileName(String groupName) {
        StringBuilder builder = new StringBuilder();
        if (moduleName != null && !moduleName.equals("")) {
            builder.append(moduleName);
        }
        builder.append(groupName);
        builder.append("Manager");
        return builder.toString();
    }


    private String generateInterfaceFileName(String groupName) {
        StringBuilder fileNameBuilder = new StringBuilder();
        if (moduleName != null && !moduleName.equals("")) {
            fileNameBuilder.append(moduleName);
        }
        fileNameBuilder.append(groupName);
        fileNameBuilder.append("DInterface");
        return fileNameBuilder.toString();
    }
}
