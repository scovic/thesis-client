package com.example.bachelorthesisclient.repository.eventdetails;

import com.example.bachelorthesisclient.datasource.local.EventDetailsLocalDataSource;
import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.model.EventObject;
import com.example.bachelorthesisclient.model.Performer;
import com.example.bachelorthesisclient.network.api.EventDetailsApi;
import com.example.bachelorthesisclient.network.dto.EventObjectDto;
import com.example.bachelorthesisclient.network.dto.EventObjectsDto;
import com.example.bachelorthesisclient.network.dto.PerformerDto;
import com.example.bachelorthesisclient.network.dto.PerformersDto;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EventDetailsRepositoryImpl implements EventDetailsRepository {
    private EventDetailsApi eventDetailsApi;
    private EventDetailsLocalDataSource localDataSource;
    private static EventDetailsRepository instance;

    public static EventDetailsRepository getInstance() {
        if (instance == null) {
            instance = new EventDetailsRepositoryImpl();
        }

        return instance;
    }

    private EventDetailsRepositoryImpl() {
        localDataSource = EventDetailsLocalDataSource.getInstance();
        eventDetailsApi = RetrofitWrapper.getInstance().create(EventDetailsApi.class);
    }

    @Override
    public Single<EventDetails> getEventDetails() {
        return localDataSource.getEventDetails()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<EventObject>> getEventObjects() {
        return eventDetailsApi.getEventObjects(getAuthorizationHeaderValue())
                .subscribeOn(Schedulers.io())
                .map(new Function<EventObjectsDto, List<EventObject>>() {
                    @Override
                    public List<EventObject> apply(EventObjectsDto eventObjectsDto) throws Exception {
                        List<EventObject> resultList = new ArrayList<>();

                        for (EventObjectDto eventObjectDto : eventObjectsDto.getEventObjects()) {
                            resultList.add(new EventObject(eventObjectDto));
                        }

                        return resultList;
                    }
                });
    }

    @Override
    public Single<List<Performer>> getStagePerformers(int stageId) {
        return eventDetailsApi.getStagePerformers(
                getAuthorizationHeaderValue(),
                stageId
        )
                .subscribeOn(Schedulers.io())
                .map(new Function<PerformersDto, List<Performer>>() {
                    @Override
                    public List<Performer> apply(PerformersDto performersDto) throws Exception {
                        List<Performer> result = new ArrayList<>();

                        for (PerformerDto performerDto : performersDto.getPerformerDtoList()) {
                            result.add(new Performer(performerDto));
                        }

                        return result;
                    }
                });
    }

    private String getAuthorizationHeaderValue() {
        String token = LoggedInUserPersistenceUtil.getToken();
        return String.format("Bearer %s", token);
    }
}
