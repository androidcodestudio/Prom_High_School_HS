package com.biswanath.promhighschoolhs.ClassSeven;

public class ClassSevenPojo {
    private String name;
    private int set;
    private String url;

    public ClassSevenPojo() {
        // for firebase real time database
    }

    public ClassSevenPojo(String name, int set, String url) {
        this.name = name;
        this.set = set;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
