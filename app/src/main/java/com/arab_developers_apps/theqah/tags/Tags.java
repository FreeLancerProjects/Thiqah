package com.arab_developers_apps.theqah.tags;

public class Tags {

    public static String base_url = "http://thiqah.net.sa/";
    public static final String IMAGE_USERS_URL = base_url+"storage/avatars/";
    public static final String IMAGE_ITEM_URL = base_url+"storage/orders/items/";
    public static final String IMAGE_BANK_URL = base_url+"storage/orders/transfers/";
    public static final String IMAGE_COMPLAINTS_URL = base_url+"storage/complaints/";
    public static final String IMAGE_PAYMENT_URL = base_url+"storage/orders/payments/";

    public static final String session_login = "login";
    public static final String session_logout = "logout";


    public static String convertArabicNumberToEnglish(String number){
        return number.replace("٠","0")
                .replace("١","1")
                .replace("٢","2")
                .replace("٣","3")
                .replace("٤","4")
                .replace("٥","5")
                .replace("٦","6")
                .replace("٧","7")
                .replace("٨","8")
                .replace("٩","9");
    }
}
