package com.creative.share.apps.thiqah.language;

import android.app.Application;
import android.content.Context;

import com.creative.share.apps.thiqah.preferences.Preferences;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base, Preferences.newInstance().getSelectedLanguage(base)));
    }
}
