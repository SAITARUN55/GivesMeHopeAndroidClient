package com.jparkie.givesmehope.views.adapters.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.models.Story;
import com.squareup.picasso.Picasso;

public class HotViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = HotViewHolder.class.getSimpleName();

    private ImageView mImageView;
    private TextView mFooterTextView;

    public HotViewHolder(View itemView) {
        super(itemView);

        mImageView = (ImageView)itemView.findViewById(R.id.imageImageView);
        mFooterTextView = (TextView)itemView.findViewById(R.id.imageFooterTextView);
    }

    public void bindStoryToView(Context context, Story story) {
        Picasso.with(context)
                .load(story.getImageUrl())
                .placeholder(R.mipmap.gmh_loading_story)
                .error(R.mipmap.gmh_no_story)
                .into(mImageView);

        mFooterTextView.setText(story.getFooter());
    }
}
