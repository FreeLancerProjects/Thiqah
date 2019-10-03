package com.arab_developers_apps.theqah.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private int current_page;
    private List<OrderModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<OrderModel> getData() {
        return data;
    }

    public class OrderModel implements Serializable
    {
        private int id;
        private int order_type;
        private int status;
        private int buyer_done;
        private int seller_done;
        private String buyer_phone;
        private String seller_phone;
        private String reason;
        private int price;
        private int shipping_cost;
        private String bank_transfer_pic;
        private String item_pic;
        private String days_left;
        private String conditions;
        private String seller_bank_name;
        private String seller_bank_account;
        private String seller_bank_iban;
        private int shipping_type_id;
        private int bank_account_id;
        private int user_id;
        private Shipping shippingType;
        private BankAccount bank_account;
        private String seller_name;
        private String seller_email;
        private String seller_city_id;
        private String seller_city_title;
        private String buyer_name;
        private String buyer_email;
        private String buyer_city_id;
        private String buyer_city_title;
        private int buyer_complained;
        private int seller_complained;




        public int getId() {
            return id+100;
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

        public String getDays_left() {
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

        public String getSeller_name() {
            return seller_name;
        }

        public String getSeller_city_title() {
            return seller_city_title;
        }

        public String getSeller_email() {
            return seller_email;
        }

        public String getSeller_city_id() {
            return seller_city_id;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public String getBuyer_email() {
            return buyer_email;
        }

        public String getBuyer_city_id() {
            return buyer_city_id;
        }

        public String getBuyer_city_title() {
            return buyer_city_title;
        }

        public int getBuyer_done() {
            return buyer_done;
        }

        public int getSeller_done() {
            return seller_done;
        }

        public int getBuyer_complained() {
            return buyer_complained;
        }

        public int getSeller_complained() {
            return seller_complained;
        }

        public class Shipping implements Serializable
        {
            private int id;
            private String title;
            private int shipping_type_id;

            public int getId() {
                return id+100;
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


}
