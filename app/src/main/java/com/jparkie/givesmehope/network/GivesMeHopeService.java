package com.jparkie.givesmehope.network;

import android.content.Context;

import com.jparkie.givesmehope.models.Anthology;
import com.jparkie.givesmehope.models.Story;
import com.jparkie.givesmehope.utils.Constants;
import com.jparkie.givesmehope.utils.GivesMeHopeParser;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class GivesMeHopeService {
    // These variables are utilized in the customization of the OkHTTP client.
    private static final int PARAMETER_CACHE_SIZE = 10 * 1024 * 1024;
    private static final int PARAMETER_CONNECT_TIMEOUT = 10;
    private static final int PARAMETER_WRITE_TIMEOUT = 10;
    private static final int PARAMETER_READ_TIMEOUT = 30;

    // This allows the GivesMeHopeService to act as a singleton accessible from any context.
    private static GivesMeHopeService mInstance;

    // The OkHTTP 2.0 library was utilized over the native HttpUrlConnections because http://square.github.io/okhttp/.
    // Retrofit was not used because http://mobile.givesmehope.com/ does not have a REST API which responds in JSON.
    private OkHttpClient mClient;

    public static GivesMeHopeService getInstance(Context baseContext) {
        if (mInstance == null) {
            mInstance = new GivesMeHopeService(baseContext.getApplicationContext());
        }

        return mInstance;
    }

    private GivesMeHopeService(Context applicationContext) {
        mClient = new OkHttpClient();
        mClient.setConnectTimeout(PARAMETER_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        mClient.setWriteTimeout(PARAMETER_WRITE_TIMEOUT, TimeUnit.SECONDS);
        mClient.setReadTimeout(PARAMETER_READ_TIMEOUT, TimeUnit.SECONDS);

        try {
            mClient.setCache(new Cache(applicationContext.getCacheDir(), PARAMETER_CACHE_SIZE));
        } catch (IOException e) {
            // Do Nothing.
        }
    }

    /* GivesMeHopeService Methods:
     * All of the GivesMeHopeService implement RxJava and RxAndroid to handle concurrency instead of AsyncTask.
     * Thus, all of the methods behave asynchronously. Nonetheless, the underlying logic of most of the methods come from GivesMeHopeParser through static methods.
     * This allows modularity of having static blocking APIs (which can be utilized elsewhere if it needs to block synchronously) wrapped as asynchronous methods.
     */

    // This method wraps a blocking, synchronous OkHTTP network request as an asynchronous Observable to be computed on a I/O thread pool.
    public Observable<Response> getResponse(final String url) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    subscriber.onNext(mClient.newCall(request).execute());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<String> mapResponseToHtml(final Response response) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    if (!response.isSuccessful()) {
                        throw new IOException();
                    }

                    subscriber.onNext(response.body().string());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    // This method wraps a blocking, synchronous HTML parsing computation as an asynchronous Observable to be computed on a I/O thread pool.
    public Observable<Anthology> mapHtmlToTrendingAnthology(final String html) {
        return Observable.create(new Observable.OnSubscribe<Anthology>() {
            @Override
            public void call(Subscriber<? super Anthology> subscriber) {
                try {
                    subscriber.onNext(GivesMeHopeParser.mapHtmlToTrendingAnthology(html));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    // This method wraps a blocking, synchronous HTML parsing computation as an asynchronous Observable to be computed on a I/O thread pool.
    public Observable<Anthology> mapHtmlToHotAnthology(final String html) {
        return Observable.create(new Observable.OnSubscribe<Anthology>() {
            @Override
            public void call(Subscriber<? super Anthology> subscriber) {
                try {
                    subscriber.onNext(GivesMeHopeParser.mapHtmlToHotAnthology(html));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    // This method wraps a blocking, synchronous HTML parsing computation as an asynchronous Observable to be computed on a I/O thread pool.
    public Observable<Story> mapHtmlToVoteStory(final String html) {
        return Observable.create(new Observable.OnSubscribe<Story>() {
            @Override
            public void call(Subscriber<? super Story> subscriber) {
                try {
                    subscriber.onNext(GivesMeHopeParser.mapHtmlToVoteStory(html));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    // This method wraps a POST request as an asynchronous Observable to be computed on a new thread.
    public Observable<Response> postVoteUp(final String postId) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this POST request was constructed by monitoring the required network request.
                    final MediaType CONSTANT_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                    final String CONSTANT_CHAR_SET = "UTF-8";
                    final String CONSTANT_FORM_ACTION = "action";
                    final String CONSTANT_FORM_POST_ID = "post_id";
                    final String CONSTANT_FORM_LIST = "list";
                    final String CONSTANT_FORM_DECISION = "decision";

                    StringBuilder submissionContent = new StringBuilder();
                    submissionContent.append(CONSTANT_FORM_ACTION + "=" + URLEncoder.encode("vote", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_POST_ID + "=" + URLEncoder.encode(postId, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_LIST + "=" + URLEncoder.encode("0", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_DECISION + "=" + URLEncoder.encode("yes", CONSTANT_CHAR_SET));

                    RequestBody body = RequestBody.create(CONSTANT_MEDIA_TYPE, submissionContent.toString());
                    Request request = new Request.Builder()
                            .url(Constants.VOTE_ACTION_URL)
                            .post(body)
                            .build();

                    subscriber.onNext(mClient.newCall(request).execute());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    // This method wraps a POST request as an asynchronous Observable to be computed on a new thread.
    public Observable<Response> postVoteDown(final String postId) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this POST request was constructed by monitoring the required network request.
                    final MediaType CONSTANT_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                    final String CONSTANT_CHAR_SET = "UTF-8";
                    final String CONSTANT_FORM_ACTION = "action";
                    final String CONSTANT_FORM_POST_ID = "post_id";
                    final String CONSTANT_FORM_LIST = "list";
                    final String CONSTANT_FORM_DECISION = "decision";

                    StringBuilder submissionContent = new StringBuilder();
                    submissionContent.append(CONSTANT_FORM_ACTION + "=" + URLEncoder.encode("vote", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_POST_ID + "=" + URLEncoder.encode(postId, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_LIST + "=" + URLEncoder.encode("0", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_DECISION + "=" + URLEncoder.encode("no", CONSTANT_CHAR_SET));

                    RequestBody body = RequestBody.create(CONSTANT_MEDIA_TYPE, submissionContent.toString());
                    Request request = new Request.Builder()
                            .url(Constants.VOTE_ACTION_URL)
                            .post(body)
                            .build();

                    subscriber.onNext(mClient.newCall(request).execute());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    // This method wraps a POST request as an asynchronous Observable to be computed on a new thread.
    public Observable<Response> postSubmitStory(final String submitName, final String submitLocation, final String submitTitle, final String submitStory, final String submitCategory) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this POST request was constructed by monitoring the required network request.
                    final MediaType CONSTANT_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                    final String CONSTANT_CHAR_SET = "UTF-8";
                    final String CONSTANT_FORM_STORY_SUBMIT = "story_submit";
                    final String CONSTANT_FORM_USERNAME = "username";
                    final String CONSTANT_FORM_LOCATION = "location";
                    final String CONSTANT_FORM_TITLE = "title";
                    final String CONSTANT_FORM_CATEGORY = "category";
                    final String CONSTANT_FORM_CONTENT = "content";
                    final String CONSTANT_FORM_SEND = "send";
                    final String CONSTANT_FORM_SUBMITTED = "submitted";
                    final String CONSTANT_FORM_CALLER = "caller";

                    StringBuilder submissionContent = new StringBuilder();
                    submissionContent.append(CONSTANT_FORM_STORY_SUBMIT + "=" + URLEncoder.encode("1", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_USERNAME + "=" + URLEncoder.encode(submitName, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_LOCATION + "=" + URLEncoder.encode(submitLocation, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_TITLE + "=" + URLEncoder.encode(submitTitle, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_CATEGORY + "=" + URLEncoder.encode(submitCategory, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_CONTENT + "=" + URLEncoder.encode(submitStory, CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_SEND + "=" + URLEncoder.encode("Submit", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_SUBMITTED + "=" + URLEncoder.encode("1", CONSTANT_CHAR_SET));
                    submissionContent.append("&" + CONSTANT_FORM_CALLER + "=" + URLEncoder.encode("builder.js", CONSTANT_CHAR_SET));

                    RequestBody body = RequestBody.create(CONSTANT_MEDIA_TYPE, submissionContent.toString());
                    Request request = new Request.Builder()
                            .url(Constants.SUBMIT_URL)
                            .post(body)
                            .build();

                    subscriber.onNext(mClient.newCall(request).execute());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }
}
