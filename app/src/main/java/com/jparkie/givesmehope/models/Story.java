package com.jparkie.givesmehope.models;

import android.os.Parcel;
import android.os.Parcelable;

// This call implements Parcelable such that it can be communicated among many contexts especially for configuration changes.
public class Story implements Parcelable {
    public static final String TAG = Story.class.getSimpleName();

    public static final String PARCELABLE_KEY =  TAG + ":" + "ParcelableKey";

    // These variables are utilized to minimize NullPointerException errors.
    public static final String EMPTY_FIELD_URL = "No URL";
    public static final String EMPTY_FIELD_POST_ID = "No Post Id";
    public static final String EMPTY_FIELD_FOOTER = "No Footer";
    public static final String EMPTY_FIELD_TITLE = "No Title";
    public static final String EMPTY_FIELD_CATEGORY = "No Category";
    public static final String EMPTY_FIELD_STORY = "No Story";
    public static final String EMPTY_FIELD_IMAGE_URL = "No Image Url";

    public static final String CATEGORY_AMAZING_FRIENDS = "Amazing Friends";
    public static final String CATEGORY_CUTE_KIDS = "Cute Kids";
    public static final String CATEGORY_INSPIRING_FEATS = "Inspiring Feats";
    public static final String CATEGORY_OTHER = "Other";
    public static final String CATEGORY_POTTER = "Potter";
    public static final String CATEGORY_RANDOM_ACTS_OF_KINDNESS = "Random Acts of Kindness";

    private String mUrl;
    private String mPostId;

    private String mFooter;

    private String mTitle;
    private String mCategory;
    private String mStory;

    private String mImageUrl;

    public Story(String url, String postId, String footer, String title, String category, String story, String imageUrl) {
        super();

        mUrl = url;
        mPostId = postId;

        mFooter = footer;

        mTitle = title;
        mCategory = category;
        mStory = story;

        mImageUrl = imageUrl;
    }

    public Story() {
        this(
                EMPTY_FIELD_URL,
                EMPTY_FIELD_POST_ID,
                EMPTY_FIELD_FOOTER,
                EMPTY_FIELD_TITLE,
                EMPTY_FIELD_CATEGORY,
                EMPTY_FIELD_STORY,
                EMPTY_FIELD_IMAGE_URL
        );
    }

    private Story(Parcel inputParcel) {
        this(
                inputParcel.readString(),
                inputParcel.readString(),
                inputParcel.readString(),
                inputParcel.readString(),
                inputParcel.readString(),
                inputParcel.readString(),
                inputParcel.readString()
        );
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        if (url != null) {
            mUrl = url;
        } else {
            mUrl = EMPTY_FIELD_URL;
        }
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        if (postId != null) {
            mPostId = postId;
        } else {
            mPostId = EMPTY_FIELD_POST_ID;
        }
    }

    public String getFooter() {
        return mFooter;
    }

    public void setFooter(String footer) {
        if (footer != null) {
            mFooter = footer;
        } else {
            mFooter = EMPTY_FIELD_FOOTER;
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        if (title != null) {
            mTitle = title;
        } else {
            mTitle = EMPTY_FIELD_TITLE;
        }
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        if (category != null) {
            mCategory = category;
        } else {
            mCategory = EMPTY_FIELD_CATEGORY;
        }
    }

    public String getStory() {
        return mStory;
    }

    public void setStory(String story) {
        if (story != null) {
            mStory = story;
        } else {
            mStory = EMPTY_FIELD_STORY;
        }
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            mImageUrl = imageUrl;
        } else {
            mImageUrl = EMPTY_FIELD_IMAGE_URL;
        }
    }

    public boolean conditionHasPostId() {
        return !mPostId.equals(EMPTY_FIELD_POST_ID);
    }

    public boolean conditionIsImage() {
        return !mImageUrl.equals(EMPTY_FIELD_IMAGE_URL);
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel inputParcel) {
            return new Story(inputParcel);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outputParcel, int flags) {
        outputParcel.writeString(mUrl);
        outputParcel.writeString(mPostId);

        outputParcel.writeString(mFooter);

        outputParcel.writeString(mTitle);
        outputParcel.writeString(mCategory);
        outputParcel.writeString(mStory);

        outputParcel.writeString(mImageUrl);
    }
}
