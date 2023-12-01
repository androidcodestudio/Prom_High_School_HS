package com.biswanath.promhighschoolhs.ClassFive;

public class ClassFiveConceptPojo {

    private String id,concept,url;
    private int set;

    public ClassFiveConceptPojo() {
    }

    public ClassFiveConceptPojo(String id, String concept, String url, int set) {
        this.id = id;
        this.concept = concept;
        this.url = url;
        this.set = set;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
