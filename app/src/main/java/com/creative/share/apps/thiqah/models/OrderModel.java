package com.creative.share.apps.thiqah.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private int id;
    private int order_type;
    private int status;
    private String buyer_phone;
    private String seller_phone;
    private String reason;
    private int price;
    private int shipping_cost;
    private String bank_transfer_pic;
    private String item_pic;
    private int days_left;
    private String conditions;
    private String seller_bank_name;
    private String seller_bank_account;
    private String seller_bank_iban;
    private int shipping_type_id;
    private int bank_account_id;
    private int user_id;
    private Shipping shippingType;
    private BankAccount bank_account;

    public int getId() {
        return id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public int getStatus() {
        return status;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public String getReason() {
        return reason;
    }

    public int getPrice() {
        return price;
    }

    public int getShipping_cost() {
        return shipping_cost;
    }

    public String getBank_transfer_pic() {
        return bank_transfer_pic;
    }

    public String getItem_pic() {
        return item_pic;
    }

    public int getDays_left() {
        return days_left;
    }

    public String getConditions() {
        return conditions;
    }

    public String getSeller_bank_name() {
        return seller_bank_name;
    }

    public String getSeller_bank_account() {
        return seller_bank_account;
    }

    public String getSeller_bank_iban() {
        return seller_bank_iban;
    }

    public int getShipping_type_id() {
        return shipping_type_id;
    }

    public int getBank_account_id() {
        return bank_account_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public Shipping getShippingType() {
        return shippingType;
    }

    public BankAccount getBank_account() {
        return bank_account;
    }

    public class Shipping implements Serializable
    {
        private int id;
        private String title;
        private int shipping_type_id;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getShipping_type_id() {
            return shipping_type_id;
        }
    }

    public class BankAccount implements Serializable
    {
        private int id;
        private String account_name;
        private String account_iban;
        private String bank_name;
        private String account_number;

        public int getId() {
            return id;
        }

        public String getAccount_name() {
            return account_name;
        }

        public String getAccount_iban() {
            return account_iban;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getAccount_number() {
            return account_number;
        }
    }
}
