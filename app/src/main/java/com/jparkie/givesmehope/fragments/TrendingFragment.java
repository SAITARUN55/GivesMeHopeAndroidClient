package com.jparkie.givesmehope.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.models.Anthology;
import com.jparkie.givesmehope.models.Story;
import com.jparkie.givesmehope.network.GivesMeHopeService;
import com.jparkie.givesmehope.utils.Constants;
import com.jparkie.givesmehope.utils.GivesMeHopeInitializer;
import com.squareup.okhttp.Response;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class TrendingFragment extends Fragment {
    private GivesMeHopeService mGivesMeHopeService;
    private Subscription mTrendingSubscription;
    private Observable<Anthology> mTrendingObservable;

    private TrendingAnthologyAdapter mTrendingAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ImageView mNetworkingErrorImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch an instance of the GivesMeHopeService such that all network requests are executed by one client.
        mGivesMeHopeService = GivesMeHopeService.getInstance(getActivity());

        // Set this fragment as a retained fragment to persist mHotObservable from the fragment lifecycle on configuration change (i.e. orientation).
        // This allows the network requests to persist.
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_general, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)fragmentView.findViewById(R.id.fragmentSwipeRefreshLayout);
        mListView = (ListView)fragmentView.findViewById(R.id.fragmentListView);
        mProgressBar = (ProgressBar)fragmentView.findViewById(R.id.fragmentProgressBar);
        mNetworkingErrorImageView = (ImageView)fragmentView.findViewById(R.id.fragmentNetworkingErrorImageView);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accentPinkA200));
        mNetworkingErrorImageView.setColorFilter(getResources().getColor(R.color.accentPinkA200), PorterDuff.Mode.MULTIPLY);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionRefresh();
            }
        });

        mTrendingAdapter = new TrendingAnthologyAdapter();
        mListView.setAdapter(mTrendingAdapter);

        // If mTrendingObservable persisted a configuration change, send a cached emission of the observable back to the list view.
        // Specifically, if the network request finished on configuration change but the subscriber (i.e. the list view adapter) un-subscribed, return the result.
        if (mTrendingObservable != null) {
            mTrendingSubscription = mTrendingObservable.subscribe(mTrendingAdapter);
        }

        // If a configuration change occurred, the data set is parceled, thus persisted, so fetch the old data set and assign it to the list view adapter.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Anthology.PARCELABLE_KEY)) {
                Anthology oldAnthology = savedInstanceState.getParcelable(Anthology.PARCELABLE_KEY);
                mTrendingAdapter.setAnthology(oldAnthology);
                mTrendingAdapter.notifyDataSetChanged();

                savedInstanceState.remove(Anthology.PARCELABLE_KEY);
                // The method ends here on configuration change as a first network request is unnecessary.
                return;
            }
        }

        // First Initialization Calls:
        mTrendingAdapter.actionPullNextAnthology();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current data set on configuration change.
        outState.putParcelable(Anthology.PARCELABLE_KEY, mTrendingAdapter.getAnthology());
    }

    @Override
    public void onDestroyView() {
        // When the user navigates away from the fragment, relinquish the adapter from subscribing to network requests to free memory and prevent the activity context from leaking.
        if (mTrendingSubscription != null) {
            mTrendingSubscription.unsubscribe();
            mTrendingSubscription = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.general, menu);

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
            case R.id.action_to_top:
                onOptionsToTop();
                return true;
            case R.id.action_disclaimer:
                onOptionsDisclaimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionRefresh() {
        // Because a refresh is requested, disregard prior requests.
        if (mTrendingSubscription != null) {
            mTrendingSubscription.unsubscribe();
            mTrendingSubscription = null;
        }

        // Reinitialize the list view adapter.
        mTrendingAdapter = new TrendingAnthologyAdapter();
        mTrendingAdapter.actionPullNextAnthology();
        mListView.setAdapter(mTrendingAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void onOptionsSubmit() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(SubmitFragment.TAG) == null) {
            SubmitFragment submitFragment = new SubmitFragment();
            submitFragment.show(getActivity().getSupportFragmentManager(), SubmitFragment.TAG);
        }
    }

    private void onOptionsRefresh() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            actionRefresh();
        }
    }

    private void onOptionsToTop() {
        mListView.smoothScrollToPosition(0);
    }

    private void onOptionsDisclaimer() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(DisclaimerFragment.TAG) == null) {
            DisclaimerFragment disclaimerFragment = new DisclaimerFragment();
            disclaimerFragment.show(getActivity().getSupportFragmentManager(), DisclaimerFragment.TAG);
        }
    }

    // The list view adapter acts as the observer which subscribes to the requests constructed by the GivesMeHopeService in a functional and reactive manner.
    private class TrendingAnthologyAdapter extends BaseAdapter implements Observer<Anthology> {
        private Anthology mAnthology;

        private boolean mIsLoading;

        public TrendingAnthologyAdapter() {
            this(GivesMeHopeInitializer.initializeTrendingAnthology());
        }

        public TrendingAnthologyAdapter(Anthology anthology) {
            mAnthology = anthology;

            mIsLoading = false;
        }

        @Override
        public int getCount() {
            return mAnthology.getStories().size();
        }

        @Override
        public Object getItem(int position) {
            return mAnthology.getStories().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            mProgressBar.setVisibility(View.INVISIBLE);

            if (conditionPullNextAnthology(position)) {
                actionPullNextAnthology();
            }

            // A view holder is used to prevent constant inflation of the list view item and calls to findViewById.
            final ViewHolder currentViewHolder;
            View currentView = convertView;

            if (currentView == null) {
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = layoutInflater.inflate(R.layout.list_view_item_text, null);

                currentViewHolder = new ViewHolder();

                currentViewHolder.mTitleTextView = (TextView)currentView.findViewById(R.id.textTitleTextView);
                currentViewHolder.mStoryTextView = (TextView)currentView.findViewById(R.id.textStoryTextView);
                currentViewHolder.mFooterTextView = (TextView)currentView.findViewById(R.id.textFooterTextView);
                currentViewHolder.mShareImageView = (ImageView)currentView.findViewById(R.id.textShareImageView);
                // A color filter is used as mostly all the resources are white, allowing dynamic styling to reduce application size as extra resources for multiple colors are not required.
                currentViewHolder.mShareImageView.setColorFilter(getResources().getColor(R.color.accentPinkA200), PorterDuff.Mode.MULTIPLY);
                currentViewHolder.mShareImageView.setImageResource(R.drawable.ic_share_white_24dp);
                currentViewHolder.mShareImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // This assigns the "Share" image of the list view item a listener to trigger an intent for other activities to consume.
                        // Specifically, this allows the current story to be shared by URL.
                        Story currentStory = mAnthology.getStories().get(position);

                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gives Me Hope");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, currentStory.getUrl());
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                    }
                });

                currentView.setTag(currentViewHolder);
            } else {
                currentViewHolder = (ViewHolder)currentView.getTag();
            }

            Story currentStory = (Story)getItem(position);
            if (currentStory == null) {
                currentStory = new Story();
            }

            currentViewHolder.mTitleTextView.setText(currentStory.getTitle());
            currentViewHolder.mStoryTextView.setText(currentStory.getStory());
            currentViewHolder.mFooterTextView.setText(currentStory.getFooter());

            return currentView;
        }

        @Override
        public void onCompleted() {
            mIsLoading = false;

            notifyDataSetChanged();

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            mProgressBar.setVisibility(View.INVISIBLE);
            mNetworkingErrorImageView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(Throwable e) {
            mIsLoading = false;

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            mProgressBar.setVisibility(View.INVISIBLE);
            if (getCount() == 0) {
                mNetworkingErrorImageView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNext(Anthology anthology) {
            mAnthology.actionAdjoinAnthologies(anthology);
        }

        public Anthology getAnthology() {
            return mAnthology;
        }

        public void setAnthology(Anthology anthology) {
            if (anthology != null) {
                mAnthology = anthology;
            } else {
                mAnthology = new Anthology();
            }
        }

        public void actionPullNextAnthology() {
            mIsLoading = true;

            // This is a Rx call (facilitated by RxJava and RxAndroid) which fetches the network response of the required URL.
            // Then it maps the response to another HTML Observable.
            // After it maps the HTML to an anthology of all the hottest stories.
            // All of these calls occur asynchronously, yet are received on the main UI thread so that the list view can be populated with new data.
            // The benefit of this RxCall is the ease at which the data "pipeline" can be configured by chaining functions.
            // This call is cached in-case a configuration change occurs when this call completes.
            mTrendingObservable = mGivesMeHopeService
                    .getResponse(mAnthology.getNextPageUrl())
                    .flatMap(new Func1<Response, Observable<String>>() {
                        @Override
                        public Observable<String> call(Response response) {
                            return mGivesMeHopeService.mapResponseToHtml(response);
                        }
                    })
                    .flatMap(new Func1<String, Observable<Anthology>>() {
                        @Override
                        public Observable<Anthology> call(String html) {
                            return mGivesMeHopeService.mapHtmlToTrendingAnthology(html);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();
            // The list view adapter subscribes to this call so that is can update the list view upon completion.
            mTrendingSubscription = mTrendingObservable.subscribe(this);
        }

        public boolean conditionPullNextAnthology(int position) {
            return !mIsLoading
                    && mAnthology.conditionIsNextPage()
                    && position > getCount() - Constants.PULL_TOLERANCE;
        }
    }

    private static class ViewHolder {
        TextView mTitleTextView;
        TextView mStoryTextView;
        TextView mFooterTextView;
        ImageView mShareImageView;
    }
}
