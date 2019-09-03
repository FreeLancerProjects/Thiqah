package com.creative.share.apps.thiqah.models;

import java.io.Serializable;
import java.util.List;

public class CommentDataModel {

    private Testimonials testimonials;

    public Testimonials getTestimonials() {
        return testimonials;
    }

    public class Testimonials implements Serializable
    {
        private int current_page;
        private List<CommentModel> data;

        public int getCurrent_page() {
            return current_page;
        }

        public List<CommentModel> getData() {
            return data;
        }

        public class CommentModel implements Serializable
        {
            private int id;
            private int rate;
            private String comment;
            private String user_name;


            public int getId() {
                return id;
            }

            public int getRate() {
                return rate;
            }

            public String getComment() {
                return comment;
            }

            public String getUser_name() {
                return user_name;
            }
        }
    }
}
