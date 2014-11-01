package com.jparkie.givesmehope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

// This call implements Parcelable such that it can be communicated among many contexts especially for configuration changes.
public class Anthology implements Parcelable {
    public static final String TAG = Anthology.class.getSimpleName();

    public static final String PARCELABLE_KEY =  TAG + ":" + "ParcelableKey";

    // This variable is utilized to minimize NullPointerException errors.
    public static final String EMPTY_FIELD_NEXT_PAGE_URL = "No Next Page URL";

    private List<Story> mStories;

    private String mNextPageUrl;

    public Anthology(List<Story> stories, String nextPageUrl) {
        mStories = stories;

        mNextPageUrl = nextPageUrl;
    }

    public Anthology() {
        this(
                new ArrayList<Story>(),
                EMPTY_FIELD_NEXT_PAGE_URL
        );
    }

    private Anthology(Parcel inputParcel) {
        this(
                new ArrayList<Story>(),
                inputParcel.readString()
        );

        inputParcel.readTypedList(mStories, Story.CREATOR);
    }

    public List<Story> getStories() {
        return mStories;
    }

    public void setStories(List<Story> stories) {
        if (stories != null) {
            mStories = stories;
        } else {
            mStories = new ArrayList<Story>();
        }
    }

    public String getNextPageUrl() {
        return mNextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        if (nextPageUrl != null) {
            mNextPageUrl = nextPageUrl;
        } else {
            mNextPageUrl = EMPTY_FIELD_NEXT_PAGE_URL;
        }
    }

    public void actionAdjoinAnthologies(Anthology newAnthology) {
        mStories.addAll(newAnthology.mStories);

        mNextPageUrl = newAnthology.mNextPageUrl;
    }

    public boolean conditionIsNextPage() {
        return !mNextPageUrl.equals(EMPTY_FIELD_NEXT_PAGE_URL);
    }

    public static Creator<Anthology> CREATOR = new Creator<Anthology>() {
        @Override
        public Anthology createFromParcel(Parcel inputParcel) {
            return new Anthology(inputParcel);
        }

        @Override
        public Anthology[] newArray(int size) {
            return new Anthology[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outputParcel, int flags) {
        outputParcel.writeTypedList(mStories);
        outputParcel.writeString(mNextPageUrl);
    }
}
