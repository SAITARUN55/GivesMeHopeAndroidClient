package com.jparkie.givesmehope.views.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.models.Story;
import com.jparkie.givesmehope.views.TrendingView;

public class TrendingViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = TrendingView.class.getSimpleName();

    private TextView mTitleTextView;
    private TextView mStoryTextView;
    private TextView mFooterTextView;

    public TrendingViewHolder(View itemView) {
        super(itemView);

        mTitleTextView = (TextView)itemView.findViewById(R.id.textTitleTextView);
        mStoryTextView = (TextView)itemView.findViewById(R.id.textStoryTextView);
        mFooterTextView = (TextView)itemView.findViewById(R.id.textFooterTextView);
    }

    public void bindStoryToView(Story story) {
        mTitleTextView.setText(story.getTitle());
        mStoryTextView.setText(story.getStory());
        mFooterTextView.setText(story.getFooter());
    }
}
