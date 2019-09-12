package com.arab_developers_apps.theqah.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String token;
    private User user;

    public class User implements Serializable
    {
        private int id;
        private String email;
        private String mobile_code;
        private String mobile_number;
        private String full_name;
        private String avatar;
        private int city_id;
        private String city_title_ar;
        private String city_title_en;

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile_code() {
            return mobile_code;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getCity_id() {
            return city_id;
        }

        public String getCity_title_ar() {
            return city_title_ar;
        }

        public String getCity_title_en() {
            return city_title_en;
        }
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
