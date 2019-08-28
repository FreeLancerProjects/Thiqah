package com.creative.share.apps.thiqah.interfaces;

import com.creative.share.apps.thiqah.models.BuyerModel;
import com.creative.share.apps.thiqah.models.SellerModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String phone_code, String phone, String password);
    }

    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name,String email,String phone_code, String phone, String password);

    }

    interface BackListener
    {
        void back();
    }

    interface OrderActionListener
    {
        void orderBuyer();
        void orderSeller();
    }

    interface ContactListener
    {
        void sendContact(String name,String email,String phone_code,String phone,String message);
    }

    interface BuyerListener
    {
        void send(BuyerModel buyerModel);
    }

    interface SellerListener
    {
        void send(SellerModel sellerModel);
    }
}
