package com.example.bachelorthesisclient.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.feed.FeedRepository;
import com.example.bachelorthesisclient.ui.activity.CreateFeedActivity;
import com.example.bachelorthesisclient.ui.activity.FeedPreviewActivity;
import com.example.bachelorthesisclient.ui.activity.ImagePreviewActivity;
import com.example.bachelorthesisclient.ui.activity.MapActivity;
import com.example.bachelorthesisclient.util.DateTimeUtil;
import com.example.bachelorthesisclient.util.ImageUtil;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FeedAdapter extends ArrayAdapter<Feed> {
    private Context context;
    private List<Feed> feedList;
    private FeedRepository feedRepository;

    public FeedAdapter(Context context, List<Feed> list) {
        super(context, 0, list);
        this.context = context;
        this.feedList = list;
        this.feedRepository = (FeedRepository) RepositoryFactory.get(RepositoryFactory.FEED_REPOSITORY);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Feed feed = feedList.get(position);

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_list_feed_details, parent, false);

        TextView userName = listItem.findViewById(R.id.text_name);
        TextView timeDate = listItem.findViewById(R.id.text_date);
        TextView feedTextContent = listItem.findViewById(R.id.text_post);
        LinearLayout imageContainer = listItem.findViewById(R.id.image_container);
        ImageButton imgbtnShowOnMap = listItem.findViewById(R.id.imgbtn_show_on_map);
        CardView cardView = listItem.findViewById(R.id.cv_feed_detail);


        if (!feed.getPost().hasLocation()) {
            imgbtnShowOnMap.setVisibility(View.GONE);
        } else {
            imgbtnShowOnMap.setVisibility(View.VISIBLE);
            imgbtnShowOnMap.setOnClickListener(handleOnClickImgbtnShowOnMap(feed));
        }

        if (LoggedInUserPersistenceUtil.getUser().isSameUser(feed.getAuthor().getId())) {
            showImgbtnEditFeed(listItem, feed);
            showImgbtnRemoveFeed(listItem, feed);
        } else {
            hideImgbtnEditFeed(listItem);
            hideImgbtnRemoveFeed(listItem);
        }

        cardView.setOnClickListener(handleOnClickCardView(feed.getPost().getId()));

        imageContainer.removeAllViews();

        for (String attachment : feed.getPost().getAttachmentNames()) {
            ImageView iv = new ImageView(this.context);
            iv.setOnClickListener(this.handleOnClickImageView(feed.getPost().getId(), attachment));
            iv.setPadding(4, 0, 4, 0);
            imageContainer.addView(iv);


            PicassoWrapper.getInstance().loadImage(
                    ImageUtil.makeImageUrl(feed.getPost().getId(), attachment),
                    iv
            );
        }


        userName.setText(String.format("%s %s", feed.getAuthor().getFirstName(), feed.getAuthor().getLastName()));
        timeDate.setText(DateTimeUtil.formatDate(feed.getPost().getUpdatedAt()));
        feedTextContent.setText(feed.getPost().getText());

        return listItem;
    }

    private void showImgbtnEditFeed(View listItem, Feed feed) {
        ImageButton imgbtnEditFeed = listItem.findViewById(R.id.imgbtn_edit_feed);
        imgbtnEditFeed.setVisibility(View.VISIBLE);
        imgbtnEditFeed.setOnClickListener(handleOnClickImgbtnEditFeed(feed));
    }

    private void hideImgbtnEditFeed(View listItem) {
        ImageButton imgbtnEditFeed = listItem.findViewById(R.id.imgbtn_edit_feed);
        imgbtnEditFeed.setVisibility(View.GONE);
    }

    private void showImgbtnRemoveFeed(View listItem, Feed feed) {
        ImageButton imgbtnRemoveFeed = listItem.findViewById(R.id.imgbtn_remove_feed);
        imgbtnRemoveFeed.setVisibility(View.VISIBLE);
        imgbtnRemoveFeed.setOnClickListener(handleOnClickImgbtnRemoveFeed(feed));
    }

    private void hideImgbtnRemoveFeed(View listItem) {
        ImageButton imgbtnRemoveFeed = listItem.findViewById(R.id.imgbtn_remove_feed);
        imgbtnRemoveFeed.setVisibility(View.GONE);
    }

    private String makeImageUrl(int postId, String imageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(postId);
        stringBuilder.append("/").append(imageName);

        return stringBuilder.toString();
    }

    private View.OnClickListener handleOnClickImageView(final int postId, final String imageName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImagePreviewActivity.class);
                i.putExtra("postId", postId);
                i.putExtra("imageName", imageName);
                context.startActivity(i);
            }
        };
    }

    private View.OnClickListener handleOnClickImgbtnShowOnMap(final Feed feed) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapActivity.class);
                i.putExtra("tag", "show_feed");
                i.putExtra("postId", feed.getPost().getId());
                i.putExtra("latitude", feed.getPost().getLocation().getLatitude());
                i.putExtra("longitude", feed.getPost().getLocation().getLongitude());
                i.putExtra("content", feed.getPost().getText());
                context.startActivity(i);
            }
        };
    }

    private View.OnClickListener handleOnClickImgbtnEditFeed(final Feed feed) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateFeedActivity.class);
                i.putExtra("postId", feed.getPost().getId());
                context.startActivity(i);
            }
        };
    }

    private View.OnClickListener handleOnClickImgbtnRemoveFeed(final Feed feed) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this feed?")
                        .setPositiveButton("Yes", handleDialogResult(feed))
                        .setNegativeButton("No", handleDialogResult(feed))
                        .show();
            }
        };
    }

    private View.OnClickListener handleOnClickCardView(final int postId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FeedPreviewActivity.class);
                i.putExtra("postId", postId);
                context.startActivity(i);
            }
        };
    }

    private DialogInterface.OnClickListener handleDialogResult(final Feed feed) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        removeFeed(feed);
                        break;

                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        break;
                    }
                }
            }
        };
    }

    private void removeFeed(final Feed feed) {
        feedRepository.deleteFeed(feed)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        if (success) {
                            feedList.remove(feed);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
