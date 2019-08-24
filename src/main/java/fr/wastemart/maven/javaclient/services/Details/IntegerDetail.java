package fr.wastemart.maven.javaclient.services.Details;

public class IntegerDetail implements Detail {
    private Integer value;

    public IntegerDetail(Integer v){
        value = v;
    }

    public Integer getValue(){
        return value;
    }
}