package com.jparkie.givesmehope.modules;

import com.jparkie.givesmehope.data.services.BadGMHServiceImpl;
import com.jparkie.givesmehope.data.services.GMHService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        overrides = true
)
public final class BadGMHServiceModule {
    public static final String TAG = BadGMHServiceModule.class.getSimpleName();

    @Provides
    @Singleton
    public GMHService provideGMHService() {
        return new BadGMHServiceImpl();
    }
}
