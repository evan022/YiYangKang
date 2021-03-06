package com.anju.yyk.main.ui.frg.register;


import com.anju.yyk.common.base.BaseApplication;
import com.anju.yyk.common.base.BaseResponse;
import com.anju.yyk.common.entity.response.CheckRoomListResponse;
import com.anju.yyk.common.entity.response.UploadImageResponse;
import com.anju.yyk.common.utils.klog.KLog;
import com.anju.yyk.main.di.component.DaggerMainComponent;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterPresenter extends IRegisterContract.RegisterPresenter {
    @Override
    public void getCheckRoomList() {
        if (getView() == null)
            return;

        getView().showLoading(true);

        Observable<CheckRoomListResponse> observable = mModel.requestCheckRoomList();
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().hideLoading();
                    if (s.getStatus() == 0){
                        getView().checkRoomListSucc(s);
                    }else {
                        getView().showToast(s.getTitle());
                    }
                }, throwable -> {
                    getView().hideLoading();
                    getView().showToast(handleThrowableInP(throwable).message);
                });
        addDisposable(disposable);
    }

    @Override
    public void checkRoomCommit(String hugong_id, String txtContent, boolean isCheck, String laoren_id, Map<String, String> map, String imagePath) {
        if (getView() == null)
            return;

        getView().showLoading(true);

        int checkStatus = -1;

        if (isCheck){
            checkStatus = 1;
        }else {
            checkStatus = 0;
        }

        Observable<BaseResponse> observable = mModel.requestCheckRoomCommit(hugong_id, txtContent, checkStatus, laoren_id, map, imagePath);
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().hideLoading();
                    if (s.getStatus() == 0){
                        getView().checkRoomCommitSucc();
                    }else {
                        getView().showToast(s.getTitle());
                    }
                }, throwable -> {
                    getView().hideLoading();
                    getView().showToast(handleThrowableInP(throwable).message);
                });
        addDisposable(disposable);
    }

    @Override
    public void uploadImage(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("upfile", file.getName(), requestFile);

        Observable<UploadImageResponse> observable = mModel.uploadImage(file.getName(),body);
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    KLog.d("上传图片成功");
                    if ("SUCCESS".equals(s.getState())) {
                        getView().hideLoading();
                        getView().uploadSuccess(s.getUrl());
                    } else {
                        getView().hideLoading();
//                        getView().showToast(s.getMsg());
                        getView().uploadFailed(filePath);
                    }
                }, throwable -> {
                    getView().hideLoading();
                    getView().showToast(handleThrowableInP(throwable).message);
                    getView().uploadFailed(filePath);
                });
        addDisposable(disposable);
    }

    @Override
    public void setUpPresenterComponent() {
        DaggerMainComponent.builder()
                .appComponent(BaseApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
    }
}
