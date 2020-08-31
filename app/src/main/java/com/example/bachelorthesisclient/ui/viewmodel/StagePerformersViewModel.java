package com.example.bachelorthesisclient.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Performer;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.eventdetails.EventDetailsRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class StagePerformersViewModel extends ViewModel {
    private EventDetailsRepository eventDetailsRepository;

    private MutableLiveData<List<Performer>> performerMutableLiveData;

    public StagePerformersViewModel() {
        super();
        eventDetailsRepository = (EventDetailsRepository) RepositoryFactory.get(RepositoryFactory.EVENT_DETAILS_REPOSITORY);

        List<Performer> list = new ArrayList<>();
        performerMutableLiveData = new MutableLiveData<>(list);
    }

    public void getStagePerformers(int stageId) {
        eventDetailsRepository.getStagePerformers(stageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Performer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Performer> performers) {
                        performerMutableLiveData.setValue(performers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("StagePerformersViewMode", e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Performer>> getPerformerMutableLiveData() {
        return performerMutableLiveData;
    }


}
