package fr.wastemart.maven.javaclient.processor;

import fr.wastemart.maven.javaclient.annotation.AutoImplement;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes(
        {"fr.wastemart.maven.javaclient.annotation.AutoImplement"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AutoGenerateProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoImplement.class);


        List<String> uniqueIdCheckList = new ArrayList<>();

        for (Element element : elements) {
            AutoImplement autoImplement = element.getAnnotation(AutoImplement.class);

            if (element.getKind() != ElementKind.INTERFACE) {
                System.out.println("The annotation @AutoImplement can only be used on interfaces: " + element);

            } else {
                boolean error = false;

                if (uniqueIdCheckList.contains(autoImplement.as())) {
                    System.out.println("AutoImplement \"as\" should be uniquely defined" + element);
                    error = true;
                }

                error = !checkIdValidity(autoImplement.as(), element);

                if (!error) {
                    uniqueIdCheckList.add(autoImplement.as());
                    try {
                        generateClass(autoImplement, element);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return false;
    }

    private void generateClass(AutoImplement autoImplement, Element element)
            throws Exception {

        String pkg = getPackageName(element);

        //delegate some processing to FieldInfo class
        FieldInfo fieldInfo = FieldInfo.get(element);

        //the target interface name
        String interfaceName = getTypeName(element);

        //using JClass to delegate most of the string appending there
        JClass implClass = new JClass();
        implClass.definePackage(pkg);
        implClass.defineClass("public class ", autoImplement.as(), "implements " + interfaceName);

        //nested builder class
        JClass builder = null;
        String builderClassName = null;

        if (autoImplement.builder()) {
            builder = new JClass();
            builder.defineClass("public static class",
                    builderClassName = autoImplement.as() + "Builder", null);
        }

        //adding class fields
        implClass.addFields(fieldInfo.getFields());
        if (builder != null) {
            builder.addFields(fieldInfo.getFields());
        }

        //adding constructor with mandatory fields
        implClass.addConstructor(builder == null ? "public" : "private",
                fieldInfo.getMandatoryFields());
        if (builder != null) {
            builder.addConstructor("private", fieldInfo.getMandatoryFields());
        }

        //generate methods
        for (Map.Entry<String, String> entry : fieldInfo.getFields().entrySet()) {
            String name = entry.getKey();
            String type = entry.getValue();
            boolean mandatory = fieldInfo.getMandatoryFields().contains(name);

            implClass.createGetterForField(name);

            //if no builder generation specified then crete setters for non mandatory fields
            if (builder == null && !mandatory) {
                implClass.createSetterForField(name);
            }

            if (builder != null && !mandatory) {
                builder.addMethod(new JMethod()
                        .defineSignature("public", false, builderClassName)
                        .name(name)
                        .addParam(type, name)
                        .defineBody(" this." + name + " = " + name + ";"
                                + JClass.LINE_BREAK
                                + " return this;"
                        )
                );
            }
        }

        if (builder != null) {

            //generate create() method of the Builder class
            JMethod createMethod = new JMethod()
                    .defineSignature("public", true, builderClassName)
                    .name("create");


            String paramString = "(";
            int i = 0;
            for (String s : fieldInfo.getMandatoryFields()) {
                createMethod.addParam(fieldInfo.getFields().get(s), s);
                paramString += (i != 0 ? ", " : "") + s;
                i++;
            }
            paramString += ");";

            createMethod.defineBody("return new " + builderClassName
                    + paramString);

            builder.addMethod(createMethod);

            //generate build() method of the builder class.
            JMethod buildMethod = new JMethod()
                    .defineSignature("public", false, autoImplement.as())
                    .name("build");
            StringBuilder buildBody = new StringBuilder();
            buildBody.append(autoImplement.as())
                    .append(" a = new ")
                    .append(autoImplement.as())
                    .append(paramString)
                    .append(JClass.LINE_BREAK);
            for (String s : fieldInfo.getFields().keySet()) {
                if (fieldInfo.getMandatoryFields().contains(s)) {
                    continue;
                }
                buildBody.append("a.")
                        .append(s)
                        .append(" = ")
                        .append(s)
                        .append(";")
                        .append(JClass.LINE_BREAK);
            }
            buildBody.append("return a;")
                    .append(JClass.LINE_BREAK);
            buildMethod.defineBody(buildBody.toString());

            builder.addMethod(buildMethod);
            implClass.addNestedClass(builder);

        }
        //finally generate class via Filer
        generateClass(pkg + "." + autoImplement.as(), implClass.end());
    }

    //
    private String getPackageName(Element element) {
        List<PackageElement> packageElements =
                ElementFilter.packagesIn(Arrays.asList(element.getEnclosingElement()));

        Optional<PackageElement> packageElement = packageElements.stream().findAny();
        return packageElement.isPresent() ?
                packageElement.get().getQualifiedName().toString() : null;

    }

    // Create the class file
    private void generateClass(String qfn, String end) throws IOException {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
        Writer writer = sourceFile.openWriter();
        writer.write(end);
        writer.close();
    }

    // Check if the class to be generated is a valid java id and the name is not the same as interface
    private boolean checkIdValidity(String name, Element e) {
        boolean valid = true;
        for (int i = 0; i < name.length(); i++) {
            if (i == 0 ? !Character.isJavaIdentifierStart(name.charAt(i)) :
                    !Character.isJavaIdentifierPart(name.charAt(i))) {
                System.out.println("AutoImplement as should be valid java identifier: " + name + e);
                valid = false;
            }
        }
        if (name.equals(getTypeName(e))) {
            System.out.println("AutoImplement as should be different than the Interface name "+ e);
        }
        return valid;
    }


    // Get the type name (ex : Java.lang.String -> String)
    private static String getTypeName(Element e) {
        TypeMirror typeMirror = e.asType();
        String[] splitedTypeName = typeMirror.toString().split("\\.");
        return splitedTypeName.length > 0 ? splitedTypeName[splitedTypeName.length - 1] : null;
    }
}