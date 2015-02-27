package com.jparkie.givesmehope;

import com.jparkie.givesmehope.modules.BadGMHInterpreterModule;
import com.jparkie.givesmehope.modules.BadGMHServiceModule;
import com.jparkie.givesmehope.modules.GMHModule;
import com.jparkie.givesmehope.modules.MockGMHInterpreterModule;
import com.jparkie.givesmehope.modules.MockGMHServiceModule;


import dagger.Module;

@Module(
        addsTo = GMHModule.class,
        includes = {
                // BadGMHInterpreterModule.class,
                // BadGMHServiceModule.class,
                // MockGMHInterpreterModule.class,
                // MockGMHServiceModule.class,
        },
        overrides = true
)
public final class DebugGMHModule {
    public static final String TAG = DebugGMHModule.class.getSimpleName();
}
