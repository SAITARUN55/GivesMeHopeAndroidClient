package com.jparkie.givesmehope;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.jparkie.givesmehope.modules.GMHModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

public class GMHApplication extends Application {
    public static final String TAG = GMHApplication.class.getSimpleName();

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeObjectGraph();
        initializeStetho();
    }

    public static GMHApplication getApplication(Context context) {
        return (GMHApplication)context.getApplicationContext();
    }

    public ObjectGraph buildInitialObjectGraph(Object... modules) {
        return ObjectGraph.create(modules);
    }

    public ObjectGraph buildScopedObjectGraph(Object... modules) {
        return mObjectGraph.plus(modules);
    }

    public List<Object> getModules() {
        return Arrays.<Object>asList(
                new GMHModule(this)
        );
    }

    private void initializeObjectGraph() {
        mObjectGraph = buildInitialObjectGraph(getModules().toArray());
        mObjectGraph.inject(this);
    }

    private void initializeStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build()
            );
        }
    }
}
