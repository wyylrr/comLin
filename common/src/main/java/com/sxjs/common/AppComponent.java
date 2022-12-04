package com.sxjs.common;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by admin on 2017/3/10.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {
    Context getContext();


}
