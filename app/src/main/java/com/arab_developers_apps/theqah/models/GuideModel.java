package com.arab_developers_apps.theqah.models;

import java.io.Serializable;
import java.util.List;

public class GuideModel implements Serializable {

    private List<Guide> userGuides;


    public List<Guide> getUserGuides() {
        return userGuides;
    }

    public class Guide implements Serializable
    {
        private int id;
        private String desc;
        private String title;

        public int getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        public String getTitle() {
            return title;
        }
    }
}
