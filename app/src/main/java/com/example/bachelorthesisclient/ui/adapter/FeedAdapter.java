package com.example.bachelorthesisclient.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends ArrayAdapter<Feed> {
    private Context context;
    private List<Feed> feedList = new ArrayList<>();

    public FeedAdapter(Context context, List<Feed> list) {
        super(context, 0, list);
        this.context = context;
        this.feedList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Feed feed = feedList.get(position);

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_home_feed, parent, false);

        TextView userName = listItem.findViewById(R.id.text_name);
        TextView timeDate = listItem.findViewById(R.id.text_date);
        TextView feedTextContent = listItem.findViewById(R.id.text_post);
        LinearLayout imageContainer = listItem.findViewById(R.id.image_container);

        imageContainer.removeAllViews();

        for (String attachment : feed.getPost().getAttachmentNames()) {
            ImageView iv = new ImageView(this.context);
            imageContainer.addView(iv);

            PicassoWrapper.getInstance().loadImage(this.makeImageUrl(feed.getPost().getId(), attachment), iv);
        }


        userName.setText(String.format("%s %s", feed.getAuthor().getFirstName(), feed.getAuthor().getLastName()));
        timeDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(feed.getPost().getUpdatedAt()));
        feedTextContent.setText(feed.getPost().getText());

        return listItem;
    }

    private String makeImageUrl(int postId, String imageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(postId);
        stringBuilder.append("/").append(imageName);

        return stringBuilder.toString();
    }
}
