package com.arab_developers_apps.theqah.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.arab_developers_apps.theqah.BR;
import com.arab_developers_apps.theqah.R;

import java.io.Serializable;

public class BuyerModel extends BaseObservable implements Serializable {
    private String order_number;
    private String city_id;
    private String transfer_purpose;
    private String item_value;
    private String shipping_method;
    private String price;
    private String bank_id;
    private String image_uri;
    private String period;
    private String condition;
    private String phone2;
    private boolean isCondition;
    private boolean isAcceptRule1;
    private boolean isAcceptRule2;


    public ObservableField<String> error_order_num = new ObservableField<>();
    public ObservableField<String> error_transfer_purpose = new ObservableField<>();
    public ObservableField<String> error_item_value = new ObservableField<>();
    public ObservableField<String> error_shipping_method = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_image = new ObservableField<>();
    public ObservableField<String> error_period = new ObservableField<>();
    public ObservableField<String> error_condition = new ObservableField<>();
    public ObservableField<String> error_phone2 = new ObservableField<>();

    public BuyerModel() {
        order_number = "";
        city_id="";
        transfer_purpose ="";
        item_value="";
        shipping_method="";
        price="";
        bank_id="";
        image_uri = "";
        period="";
        condition="";
        phone2="";
        isCondition = false;
        isAcceptRule1 =false;
        isAcceptRule2 = false;
    }

    public BuyerModel(String order_number, String name, String phone, String email, String city_id, String transfer_purpose, String item_value, String shipping_method, String price, String bank_id, String image_uri, String period, String condition, String phone2, boolean isCondition, boolean isAcceptRule1, boolean isAcceptRule2) {
        setOrder_number(order_number);
        setCity_id(city_id);
        setTransfer_purpose(transfer_purpose);
        setItem_value(item_value);
        setShipping_method(shipping_method);
        setPrice(price);
        setBank_id(bank_id);
        setImage_uri(image_uri);
        setPeriod(period);
        setCondition(condition);
        setPhone2(phone2);
        setCondition(isCondition);
        setAcceptRule1(isAcceptRule1);
        setAcceptRule2(isAcceptRule2);
    }

    @Bindable
    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
        notifyPropertyChanged(BR.order_number);
    }


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
    public String getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(String shipping_method) {
        this.shipping_method = shipping_method;
        notifyPropertyChanged(BR.shipping_method);

    }
    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }
    @Bindable
    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
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
        this.phone2 = phone2.startsWith("0")?phone2.replaceFirst("0",""):phone2;
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
        Log.e("city_id",city_id+"_");
        Log.e("transfer_purpose",transfer_purpose+"_");
        Log.e("item_value",item_value+"_");
        Log.e("shipping_method",shipping_method+"_");
        Log.e("price",price+"_");
        Log.e("bank_id",bank_id+"_");
        Log.e("image_uri",image_uri+"_");
        Log.e("period",period+"_");
        Log.e("phone2",phone2+"_");
        Log.e("isAcceptRule1",isAcceptRule1+"_");
        Log.e("isAcceptRule2",isAcceptRule2+"_");


        if (!TextUtils.isEmpty(city_id)&&
                !TextUtils.isEmpty(transfer_purpose)&&
                !TextUtils.isEmpty(item_value)&&
                !TextUtils.isEmpty(shipping_method)&&
                !TextUtils.isEmpty(price)&&
                !TextUtils.isEmpty(bank_id)&&
                !TextUtils.isEmpty(image_uri)&&
                !TextUtils.isEmpty(period)&&
                !TextUtils.isEmpty(phone2)&&
                phone2.length()==9&&
                isAcceptRule1
        )
        {
            //isAcceptRule2

            error_order_num.set(null);
            error_transfer_purpose.set(null);
            error_item_value.set(null);
            error_shipping_method.set(null);
            error_price.set(null);
            error_image.set(null);
            error_period.set(null);
            error_phone2.set(null);

            Log.e("1","1");
            if (isCondition)
            {

                if (TextUtils.isEmpty(condition))
                {
                    Log.e("2","2");

                    error_condition.set(context.getString(R.string.field_req));
                    return false;
                }else
                    {
                        return true;
                    }
            }else
                {
                    Log.e("3","3");

                    error_condition.set(null);

                    return true;
                }


        }else
            {

                Log.e("4","4");

                /*if (TextUtils.isEmpty(order_number))
                {
                    error_order_num.set(context.getString(R.string.field_req));
                }else
                    {
                        error_order_num.set(null);
                    }*/


                if (TextUtils.isEmpty(phone2))
                {
                    error_phone2.set(context.getString(R.string.field_req));
                }
                else
                {
                    error_phone2.set(null);
                }
                if(phone2.length()!=9){
                    error_phone2.set(context.getString(R.string.most2));

                }
                else {
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

                if (TextUtils.isEmpty(shipping_method))
                {
                    error_shipping_method.set(context.getString(R.string.field_req));
                }else
                {
                    error_shipping_method.set(null);
                }

                if (TextUtils.isEmpty(price))
                {
                    error_price.set(context.getString(R.string.field_req));
                }else
                {
                    error_price.set(null);
                }

                if (TextUtils.isEmpty(bank_id))
                {
                    Toast.makeText(context, R.string.ch_bank, Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(image_uri))
                {

                    Toast.makeText(context, R.string.ch_image, Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(period))
                {
                    Toast.makeText(context, R.string.ch_period, Toast.LENGTH_SHORT).show();
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

                /*if (!isAcceptRule2)
                {

                    Toast.makeText(context, R.string.cont_comp2, Toast.LENGTH_SHORT).show();
                }*/
                return false;
            }

    }
}
