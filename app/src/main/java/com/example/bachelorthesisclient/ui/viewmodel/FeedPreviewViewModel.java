package com.example.bachelorthesisclient.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.model.Post;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.repository.FeedRepository;
import com.example.bachelorthesisclient.repository.IamRepository;
import com.example.bachelorthesisclient.repository.RepositoryFactory;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class FeedPreviewViewModel extends ViewModel {
    private FeedRepository mFeedRepository;
    private IamRepository mIamRepository;
    private MutableLiveData<Feed> mFeed;

    public FeedPreviewViewModel() {
        super();
        mFeedRepository = (FeedRepository) RepositoryFactory.get(RepositoryFactory.FEED_REPOSITORY);
        mIamRepository = (IamRepository) RepositoryFactory.get(RepositoryFactory.IAM_REPOSITORY);
        mFeed = new MutableLiveData<>(null);
    }

    public void loadFeedDetails(int id) {
        final Feed feed = new Feed();

        mFeedRepository.getPost(id)
                .flatMap(new Function<Post, SingleSource<User>>() {
                    @Override
                    public SingleSource<User> apply(Post post) throws Exception {
                        feed.setPost(post);
                        return mIamRepository.getUser(post.getAuthorId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(User user) {
                        feed.setAuthor(user);
                        mFeed.setValue(feed);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public MutableLiveData<Feed> getFeed() {
        return mFeed;
    }
}
