package com.arab_developers_apps.theqah.models;

import java.io.Serializable;
import java.util.List;

public class Cities_Payment_Bank_Model implements Serializable {

    private List<Payment> shippingTypes;
    private List<City> cities;
    private List<Bank> banks;

    public List<Payment> getShippingTypes() {
        return shippingTypes;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public class Payment implements Serializable
    {
        private int id;
        private String cost;
        private String title;

        public int getId() {
            return id;
        }

        public String getCost() {
            return cost;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class City implements Serializable
    {
        private int id;
        private String ar_name;
        private String en_name;

        public City(String ar_name, String en_name) {
            this.ar_name = ar_name;
            this.en_name = en_name;
        }

        public int getId() {
            return id;
        }

        public String getAr_name() {
            return ar_name;
        }

        public String getEn_name() {
            return en_name;
        }
    }

    public static class Bank implements Serializable
    {
        private int id;
        private String account_name;
        private String account_iban;
        private String bank_name;
        private String account_number;

        public Bank(String bank_name) {
            this.bank_name = bank_name;
        }

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

