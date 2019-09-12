package com.arab_developers_apps.theqah.language;

import android.app.Application;
import android.content.Context;

import com.arab_developers_apps.theqah.preferences.Preferences;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base, Preferences.newInstance().getSelectedLanguage(base)));
    }
}
