package Annotations;

import java.lang.annotation.*;


public class Documentation {
    public enum TYPEDOC {
        CLASS("Documenting class"),
        FUNCTION("Documenting function"),
        PROTOTYPE("Documenting prototype"),
        METHOD("Documenting method"),
        UNKNOWN("Documenting unknown element");

        private String type;
        TYPEDOC(String givenType){
            type = givenType;
        }

        public String toString(){
            return type;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface JsonMethod {
        TYPEDOC typeDoc() default TYPEDOC.UNKNOWN;
        String revision() default "1";
        String value() default "Developer";
        String comment() default "";
        boolean generateDocumentation() default true;
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface JsonField {
        public String value() default "";
    }
}

