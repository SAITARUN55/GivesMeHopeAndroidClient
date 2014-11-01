package com.jparkie.givesmehope.utils;

import com.jparkie.givesmehope.models.Anthology;

public class GivesMeHopeInitializer {
    public static Anthology initializeTrendingAnthology() {
        final String CONSTANT_INITIAL_NEXT_PAGE_URL = "http://mobile.givesmehope.com/page/1/";

        Anthology latestAnthology = new Anthology();
        latestAnthology.setNextPageUrl(CONSTANT_INITIAL_NEXT_PAGE_URL);

        return latestAnthology;
    }

    public static Anthology initializeHotAnthology() {
        final String CONSTANT_INITIAL_NEXT_PAGE_URL = "http://mobile.givesmehope.com/bestof/month/";

        Anthology popularAnthology = new Anthology();
        popularAnthology.setNextPageUrl(CONSTANT_INITIAL_NEXT_PAGE_URL);

        return popularAnthology;
    }
}
