package com.arab_developers_apps.theqah.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {
    private int current_page;
    private List<NotificationModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<NotificationModel> getData() {
        return data;
    }

    public static class NotificationModel implements Serializable{
        private int id;
        private int from_phone;
        private int to_phone;
        private int type;
        private int action;
        private int is_read;
        private int notification_message_id;
        private int order_id;
        private int user_id;
        private long notification_time;
        private String message_title;
        private String message_body;

        public int getId() {
            return id;
        }

        public int getFrom_phone() {
            return from_phone;
        }

        public int getTo_phone() {
            return to_phone;
        }

        public int getType() {
            return type;
        }

        public int getAction() {
            return action;
        }

        public int getIs_read() {
            return is_read;
        }

        public int getNotification_message_id() {
            return notification_message_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public long getNotification_time() {
            return notification_time;
        }

        public String getMessage_title() {
            return message_title;
        }

        public String getMessage_body() {
            return message_body;
        }
    }



}
