package com.arab_developers_apps.theqah.models;

import java.io.Serializable;
import java.util.List;

public class ComplainDataModel implements Serializable {

    private Complain complaint;

    public Complain getComplaint() {
        return complaint;
    }

    public class Complain implements Serializable
    {
        private int id;
        private String body;
        private int user_role;
        private List<String> attachments;
        private int user_id;
        private int order_id;


        public int getId() {
            return id;
        }

        public String getBody() {
            return body;
        }

        public int getUser_role() {
            return user_role;
        }

        public List<String> getAttachments() {
            return attachments;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getOrder_id() {
            return order_id;
        }
    }
}
