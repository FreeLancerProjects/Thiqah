package com.creative.share.apps.thiqah.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.thiqah.BR;
import com.creative.share.apps.thiqah.R;

import java.io.Serializable;

public class SellerModel extends BaseObservable implements Serializable {
   // private String order_number;
    /*private String name;
    private String phone;
    private String email;*/
    private String city_id;
    private String transfer_purpose;
    private String item_value;
    private String bank_name;
    private String account_number;
    private String iban_number;
    private String image_uri;
    private String period;
    private String condition;
    private String phone2;
    private boolean isCondition;
    private boolean isAcceptRule1;
    private boolean isAcceptRule2;


    //public ObservableField<String> error_order_num = new ObservableField<>();
   /* public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();*/
    public ObservableField<String> error_bank_name = new ObservableField<>();

    public ObservableField<String> error_transfer_purpose = new ObservableField<>();
    public ObservableField<String> error_item_value = new ObservableField<>();
    public ObservableField<String> error_account_number = new ObservableField<>();
    public ObservableField<String> error_iban_number = new ObservableField<>();
    public ObservableField<String> error_image = new ObservableField<>();
    public ObservableField<String> error_period = new ObservableField<>();
    public ObservableField<String> error_condition = new ObservableField<>();
    public ObservableField<String> error_phone2 = new ObservableField<>();

    public SellerModel() {
        //order_number = "";
        /*name="";
        phone = "";
        email = "";*/
        city_id="";
        transfer_purpose ="";
        item_value="";
        account_number="";
        iban_number="";
        bank_name ="";
        image_uri = "";
        period="";
        condition="";
        phone2="";
        isCondition = false;
        isAcceptRule1 =false;
        isAcceptRule2 = false;
    }

    public SellerModel(String city_id, String transfer_purpose, String item_value, String account_number, String iban_number, String bank_name, String image_uri, String period, String condition, String phone2, boolean isCondition, boolean isAcceptRule1, boolean isAcceptRule2) {
        //setOrder_number(order_number);
      /*  setName(name);
        setPhone(phone);
        setEmail(email);*/
        setCity_id(city_id);
        setTransfer_purpose(transfer_purpose);
        setItem_value(item_value);

        setBank_name(bank_name);
        setImage_uri(image_uri);
        setPeriod(period);
        setCondition(condition);
        setPhone2(phone2);
        setCondition(isCondition);
        setAcceptRule1(isAcceptRule1);
        setAcceptRule2(isAcceptRule2);
    }

   /* @Bindable
    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
        notifyPropertyChanged(BR.order_number);
    }*/
   /* @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }
    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }*/

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;

    }
    @Bindable
    public String getTransfer_purpose() {
        return transfer_purpose;
    }

    public void setTransfer_purpose(String transfer_purpose) {
        this.transfer_purpose = transfer_purpose;
        notifyPropertyChanged(BR.transfer_purpose);

    }
    @Bindable
    public String getItem_value() {
        return item_value;
    }

    public void setItem_value(String item_value) {
        this.item_value = item_value;
        notifyPropertyChanged(BR.item_value);

    }

    @Bindable
    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
        notifyPropertyChanged(BR.account_number);

    }

    @Bindable
    public String getIban_number() {
        return iban_number;
    }

    public void setIban_number(String iban_number) {
        this.iban_number = iban_number;
        notifyPropertyChanged(BR.iban_number);
    }

    @Bindable
    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
    @Bindable
    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
    @Bindable
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    @Bindable
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyPropertyChanged(BR.condition);

    }
    @Bindable
    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
        notifyPropertyChanged(BR.phone2);

    }
    @Bindable
    public boolean isCondition() {
        return isCondition;
    }

    public void setCondition(boolean condition) {
        isCondition = condition;
    }
    @Bindable
    public boolean isAcceptRule1() {
        return isAcceptRule1;
    }

    public void setAcceptRule1(boolean acceptRule1) {
        isAcceptRule1 = acceptRule1;
    }
    @Bindable
    public boolean isAcceptRule2() {
        return isAcceptRule2;
    }

    public void setAcceptRule2(boolean acceptRule2) {
        isAcceptRule2 = acceptRule2;
    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(city_id)&&
                !TextUtils.isEmpty(transfer_purpose)&&
                !TextUtils.isEmpty(item_value)&&
                !TextUtils.isEmpty(account_number)&&
                !TextUtils.isEmpty(iban_number)&&
                !TextUtils.isEmpty(bank_name)&&
                !TextUtils.isEmpty(image_uri)&&
                !TextUtils.isEmpty(period)&&
                !TextUtils.isEmpty(phone2)&&
                isAcceptRule1&&isAcceptRule2
        )
        {

           /* error_order_num.set(null);
            error_name.set(null);
            error_phone.set(null);
            error_email.set(null);*/
            error_transfer_purpose.set(null);
            error_item_value.set(null);
            error_account_number.set(null);
            error_iban_number.set(null);
            error_image.set(null);
            error_period.set(null);
            error_phone2.set(null);
            error_bank_name.set(null);

            if (isCondition)
            {
                if (TextUtils.isEmpty(condition))
                {
                    error_condition.set(context.getString(R.string.field_req));
                    return false;
                }else
                    {
                        return true;
                    }
            }else
                {
                    error_condition.set(null);

                    return true;
                }


        }else
            {


                /*if (TextUtils.isEmpty(order_number))
                {
                    error_order_num.set(context.getString(R.string.field_req));
                }else
                    {
                        error_order_num.set(null);
                    }

                if (TextUtils.isEmpty(name))
                {
                    error_name.set(context.getString(R.string.field_req));
                }else
                {
                    error_name.set(null);
                }

                if (TextUtils.isEmpty(phone))
                {
                    error_phone.set(context.getString(R.string.field_req));
                }else if (phone.length()!=10)
                {
                    error_phone.set(context.getString(R.string.inv_phone));
                }
                else
                {
                    error_phone.set(null);
                }

                if (TextUtils.isEmpty(email))
                {
                    error_email.set(context.getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    error_email.set(context.getString(R.string.inv_phone));
                }
                else
                {
                    error_email.set(null);
                }*/

                if (TextUtils.isEmpty(phone2))
                {
                    error_phone2.set(context.getString(R.string.field_req));
                }
                else
                {
                    error_phone2.set(null);
                }


                if (TextUtils.isEmpty(city_id))
                {
                    Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(transfer_purpose))
                {
                    error_transfer_purpose.set(context.getString(R.string.field_req));
                }else
                {
                    error_transfer_purpose.set(null);
                }

                if (TextUtils.isEmpty(item_value))
                {
                    error_item_value.set(context.getString(R.string.field_req));
                }else
                {
                    error_item_value.set(null);
                }

                if (TextUtils.isEmpty(account_number))
                {
                    error_account_number.set(context.getString(R.string.field_req));
                }else
                {
                    error_account_number.set(null);
                }

                if (TextUtils.isEmpty(iban_number))
                {
                    error_iban_number.set(context.getString(R.string.field_req));
                }else
                {
                    error_iban_number.set(null);
                }

                if (TextUtils.isEmpty(bank_name))
                {
                    error_bank_name.set(context.getString(R.string.field_req));
                }else
                    {
                        error_bank_name.set(null);

                    }

                if (TextUtils.isEmpty(image_uri))
                {

                    Toast.makeText(context, R.string.ch_image, Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(period))
                {
                    error_period.set(context.getString(R.string.field_req));
                }else
                {
                    error_period.set(null);
                }

                if (isCondition)
                {
                    if (TextUtils.isEmpty(condition))
                    {
                        error_condition.set(context.getString(R.string.field_req));
                    }
                }else
                {
                    error_condition.set(null);

                }

                if (!isAcceptRule1)
                {

                    Toast.makeText(context, R.string.cnt_comp, Toast.LENGTH_SHORT).show();
                }

                if (!isAcceptRule2)
                {

                    Toast.makeText(context, R.string.cont_comp2, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

    }
}
