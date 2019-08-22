package fr.wastemart.maven.javaclient.services.Details;

public class StringDetail implements Detail {
    private String str;

    public StringDetail(String string){
        str = string;
    }

    public String getStringDetail(){
        return str;
    }
}