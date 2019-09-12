package com.arab_developers_apps.theqah.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.arab_developers_apps.theqah.BR;
import com.arab_developers_apps.theqah.R;

public class ContactModel extends BaseObservable {

    private String name;
    private String email;
    private String message;
    private String phone_code;
    private String phone;
    public ObservableField<String> error_name= new ObservableField<>();
    public ObservableField<String> error_email= new ObservableField<>();
    public ObservableField<String> error_message= new ObservableField<>();
    public ObservableField<String> error_phone_code= new ObservableField<>();
    public ObservableField<String> error_phone= new ObservableField<>();

    public ContactModel() {
        name = "";
        email = "";
        phone_code = "";
        phone = "";
        message = "";
    }

    public ContactModel(String name, String email, String message, String phone_code, String phone) {
        setName(name);
        setEmail(email);
        setPhone_code(phone_code);
        setPhone(phone);
        setMessage(message);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);
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
    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);

    }


    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(message)&&
                !TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
        {
            error_name.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_message.set(null);
            error_email.set(null);

            return true;
        }else
        {
            if (name.isEmpty())
            {
                error_name.set(context.getString(R.string.field_req));
            }else
            {
                error_name.set(null);
            }

            if (email.isEmpty())
            {
                error_email.set(context.getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                error_email.set(context.getString(R.string.inv_email));
            }else
            {
                error_email.set(null);

            }

            if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }

            if (phone.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
            }

            if (message.isEmpty())
            {
                error_message.set(context.getString(R.string.field_req));
            }else
            {
                error_message.set(null);
            }


            return false;
        }
    }

}
