package com.anju.yyk.main.ui.frg.patrol;

import com.anju.yyk.common.base.BaseApplication;
import com.anju.yyk.common.base.BaseModel;
import com.anju.yyk.common.entity.response.PatrolResponse;
import com.anju.yyk.common.entity.response.UploadImageResponse;
import com.anju.yyk.common.http.ApiAction;
import com.anju.yyk.common.http.NetManager;
import com.anju.yyk.main.di.component.DaggerMainComponent;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public class PatrolModel extends BaseModel implements IPatrolContract.IPatrolModel {

    @Inject
    NetManager mNetManager;

    @Override
    public void setupModelComponent() {
        DaggerMainComponent.builder()
                .appComponent(BaseApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public boolean isSetupModelComponent() {
        return true;
    }

    @Override
    public Observable<PatrolResponse> requestPatrol() {
        return mNetManager.getApiService().patrol(ApiAction.PATROL_ACTION);
    }

    @Override
    public Observable<UploadImageResponse> uploadImage(String name, MultipartBody.Part file) {
        return mNetManager.getApiService().uploadImage(ApiAction.UPLOAD_IMAGE_ACTION, name, file);
    }
}
