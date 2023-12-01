package com.biswanath.promhighschoolhs.StudentZone;

import java.util.Date;

public class UserPojo {

    String Key;
    String Image;
    String Name;


    public UserPojo() {
    }

    public UserPojo(String image, String name, String key) {
        Image = image;
        Name = name;
        this.Key = key;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
