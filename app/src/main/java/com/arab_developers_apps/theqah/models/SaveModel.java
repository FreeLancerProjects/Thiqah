package com.arab_developers_apps.theqah.models;

import java.io.Serializable;

public class SaveModel implements Serializable {
    private String iban;
    private String accountNumber;

    public SaveModel(String iban, String accountNumber) {
        this.iban = iban;
        this.accountNumber = accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
