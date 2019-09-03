package com.creative.share.apps.thiqah.models;

import java.io.Serializable;

public class FireBaseNotModel implements Serializable {

    private boolean isNewNotificaton;

    public FireBaseNotModel(boolean isNewNotificaton) {
        this.isNewNotificaton = isNewNotificaton;
    }

    public boolean isNewNotificaton() {
        return isNewNotificaton;
    }
}
