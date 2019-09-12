package com.arab_developers_apps.theqah.models;

import java.io.Serializable;

public class PaymentDataModel implements Serializable {

    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public class Payment implements Serializable
    {
        private int id;
        private String date;
        private String amount;
        private String image;
        private int order_status;


        public int getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }

        public String getImage() {
            return image;
        }

        public int getOrder_status() {
            return order_status;
        }
    }
}
