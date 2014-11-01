package com.jparkie.givesmehope.fragments;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.models.Anthology;
import com.jparkie.givesmehope.models.Story;
import com.jparkie.givesmehope.network.GivesMeHopeService;
import com.jparkie.givesmehope.utils.Constants;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class VoteFragment extends Fragment implements Observer<Story> {
    private GivesMeHopeService mGivesMeHopeService;
    private Subscription mVoteDownloadSubscription;
    private Subscription mVoteUpSubscription;
    private Subscription mVoteDownSubscription;

    private SwipeRefreshLayout mVoteSwipeRefreshLayout;
    private CardView mVoteCardView;
    private ImageView mVoteImageView;
    private TextView mVoteFooterTextView;
    private FloatingActionButton mVoteUpButton;
    private FloatingActionButton mVoteDownButton;
    private ProgressBar mVoteProgressBar;
    private ImageView mVoteNetworkingErrorImageView;

    private Story mStory;

    private boolean mIsVoting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch an instance of the GivesMeHopeService such that all network requests are executed by one client.
        mGivesMeHopeService = GivesMeHopeService.getInstance(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View voteView = inflater.inflate(R.layout.fragment_vote, container, false);

        mVoteSwipeRefreshLayout = (SwipeRefreshLayout)voteView.findViewById(R.id.voteSwipeRefreshLayout);
        mVoteCardView = (CardView)voteView.findViewById(R.id.voteCardView);
        mVoteImageView = (ImageView)voteView.findViewById(R.id.voteImageView);
        mVoteFooterTextView = (TextView)voteView.findViewById(R.id.voteFooterTextView);
        mVoteUpButton = (FloatingActionButton)voteView.findViewById(R.id.voteUpButton);
        mVoteDownButton = (FloatingActionButton)voteView.findViewById(R.id.voteDownButton);
        mVoteProgressBar = (ProgressBar)voteView.findViewById(R.id.voteProgressBar);
        mVoteNetworkingErrorImageView = (ImageView)voteView.findViewById(R.id.voteNetworkingErrorImageView);

        mVoteSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accentPinkA200));
        mVoteNetworkingErrorImageView.setColorFilter(getResources().getColor(R.color.accentPinkA200), PorterDuff.Mode.MULTIPLY);

        return voteView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mVoteSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionRefresh();
            }
        });

        mVoteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVoteUp();
            }
        });

        mVoteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVoteDown();
            }
        });

        mStory = new Story();

        // If a configuration change occurred, the data set is parceled, thus persisted, so fetch the old data set and assign it to the view.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Story.PARCELABLE_KEY)) {
                mStory = savedInstanceState.getParcelable(Story.PARCELABLE_KEY);

                mVoteCardView.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(mStory.getImageUrl())
                        .placeholder(R.drawable.gmh_loading_story)
                        .error(R.drawable.gmh_no_story)
                        .into(mVoteImageView);
                mVoteFooterTextView.setText(mStory.getFooter());

                mVoteProgressBar.setVisibility(View.INVISIBLE);
                mVoteNetworkingErrorImageView.setVisibility(View.INVISIBLE);

                savedInstanceState.remove(Anthology.PARCELABLE_KEY);
                // The method ends here on configuration change as a first network request is unnecessary.
                return;
            }
        }

        // First Initialization Calls:
        actionPullStory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current data set on configuration change.
        outState.putParcelable(Story.PARCELABLE_KEY, mStory);
    }

    @Override
    public void onDestroy() {
        // Relinquish all subscriptions to network requests to free memory and prevent the activity context from leaking.
        if (mVoteDownloadSubscription != null) {
            mVoteDownloadSubscription.unsubscribe();
            mVoteDownloadSubscription = null;
        }
        if (mVoteUpSubscription != null) {
            mVoteUpSubscription.unsubscribe();
            mVoteUpSubscription = null;
        }
        if (mVoteDownSubscription != null) {
            mVoteDownSubscription.unsubscribe();
            mVoteDownSubscription = null;
        }

        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vote, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                onOptionsSubmit();
                return true;
            case R.id.action_refresh:
                onOptionsRefresh();
                return true;
            case R.id.action_disclaimer:
                onOptionsDisclaimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCompleted() {
        mVoteCardView.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(mStory.getImageUrl())
                .placeholder(R.drawable.gmh_loading_story)
                .error(R.drawable.gmh_no_story)
                .into(mVoteImageView);
        mVoteFooterTextView.setText(mStory.getFooter());

        if (mVoteSwipeRefreshLayout.isRefreshing()) {
            mVoteSwipeRefreshLayout.setRefreshing(false);
        }

        mVoteProgressBar.setVisibility(View.INVISIBLE);
        mVoteNetworkingErrorImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(Throwable e) {
        if (mVoteSwipeRefreshLayout.isRefreshing()) {
            mVoteSwipeRefreshLayout.setRefreshing(false);
        }

        mVoteProgressBar.setVisibility(View.INVISIBLE);
        if (mVoteCardView.getVisibility() == View.INVISIBLE) {
            mVoteNetworkingErrorImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNext(Story story) {
        mStory = story;
    }

    private void actionPullStory() {
        // This is a Rx call (facilitated by RxJava and RxAndroid) which fetches the network response of the required URL.
        // Then it maps the response to another HTML Observable.
        // After it maps the HTML to the current story to be voted.
        // All of these calls occur asynchronously, yet are received on the main UI thread so that the view can be populated by the story.
        // The benefit of this RxCall is the ease at which the data "pipeline" can be configured by chaining functions.
        mVoteDownloadSubscription = mGivesMeHopeService
                .getResponse(Constants.VOTE_URL)
                .flatMap(new Func1<Response, Observable<String>>() {
                    @Override
                    public Observable<String> call(Response response) {
                        return mGivesMeHopeService.mapResponseToHtml(response);
                    }
                })
                .flatMap(new Func1<String, Observable<Story>>() {
                    @Override
                    public Observable<Story> call(String html) {
                        return mGivesMeHopeService.mapHtmlToVoteStory(html);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void actionRefresh() {
        if (mVoteDownloadSubscription != null) {
            mVoteDownloadSubscription.unsubscribe();
            mVoteDownloadSubscription = null;
        }

        actionPullStory();

        mVoteSwipeRefreshLayout.setRefreshing(true);
    }

    private void actionVoteUp() {
        if (!mIsVoting && mStory.conditionHasPostId()) {
            mIsVoting = true;

            // This is a Rx call (facilitated by RxJava and RxAndroid) which posts user's vote to Gives Me Hope.
            // This call occurs asynchronously and is observed on the main UI thread to prompt the user of the status of the submission.
            // The benefit of the Rx call is the ease in concurrency and the ease in managing the status of a concurrent call through the following functions (i.e. managing the success and all the errors).
            mVoteUpSubscription = mGivesMeHopeService
                    .postVoteUp(mStory.getPostId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onCompleted() {
                            mIsVoting = false;

                            Toast.makeText(getActivity(), R.string.toast_vote_success, Toast.LENGTH_SHORT).show();

                            actionRefresh();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mIsVoting = false;

                            Toast.makeText(getActivity(), R.string.toast_submit_failure, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Response response) {
                            // Do Nothing.
                        }
                    });
        }
    }

    private void actionVoteDown() {
        if (!mIsVoting && mStory.conditionHasPostId()) {
            mIsVoting = true;

            // This is a Rx call (facilitated by RxJava and RxAndroid) which posts user's vote to Gives Me Hope.
            // This call occurs asynchronously and is observed on the main UI thread to prompt the user of the status of the submission.
            // The benefit of the Rx call is the ease in concurrency and the ease in managing the status of a concurrent call through the following functions (i.e. managing the success and all the errors).
            mVoteDownSubscription = mGivesMeHopeService
                    .postVoteDown(mStory.getPostId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onCompleted() {
                            mIsVoting = false;

                            Toast.makeText(getActivity(), R.string.toast_vote_success, Toast.LENGTH_SHORT).show();

                            actionRefresh();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mIsVoting = false;

                            Toast.makeText(getActivity(), R.string.toast_submit_failure, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Response response) {
                            // Do Nothing.
                        }
                    });
        }
    }

    private void onOptionsSubmit() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(SubmitFragment.TAG) == null) {
            SubmitFragment submitFragment = new SubmitFragment();
            submitFragment.show(getActivity().getSupportFragmentManager(), SubmitFragment.TAG);
        }
    }

    private void onOptionsRefresh() {
        if (!mVoteSwipeRefreshLayout.isRefreshing()) {
            actionRefresh();
        }
    }

    private void onOptionsDisclaimer() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(DisclaimerFragment.TAG) == null) {
            DisclaimerFragment disclaimerFragment = new DisclaimerFragment();
            disclaimerFragment.show(getActivity().getSupportFragmentManager(), DisclaimerFragment.TAG);
        }
    }
}
