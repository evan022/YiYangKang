package com.anju.yyk.main.ui.act.recorddetail;

import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.anju.yyk.common.app.arouter.RouterConstants;
import com.anju.yyk.common.app.arouter.RouterKey;
import com.anju.yyk.common.base.BaseMvpActivity;
import com.anju.yyk.common.entity.response.AccidentDetailResponse;
import com.anju.yyk.common.entity.response.CareDetailResponse;
import com.anju.yyk.common.entity.response.CheckRoomDetailResponse;
import com.anju.yyk.common.imageloader.ImageLoader;
import com.anju.yyk.common.utils.AppUtil;
import com.anju.yyk.common.widget.itemdecoration.SpacesItemDecoration;
import com.anju.yyk.main.R;
import com.anju.yyk.main.R2;
import com.anju.yyk.main.adapter.CheckRoomDetailAdapter;
import com.anju.yyk.main.adapter.TakePhotoAdapter;
import com.anju.yyk.main.entity.PhotoEntity;
import com.anju.yyk.main.entity.RecordInfoEntity;
import com.anju.yyk.main.ui.act.recorddetail.IRecordDetailContract.IRecordDetailView;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 *
 * @author LeoWang
 *
 * @Package com.anju.yyk.main.ui.act.recorddetail
 *
 * @Description 查房详情
 *
 * @Date 2019/10/24 10:26
 *
 * @modify:
 */
@Route(path = RouterConstants.ACT_URL_CHECKROOM_DETAIL)
public class CheckRoomDetailAct extends BaseMvpActivity<RecordDetailPresenter, RecordDetailModel> implements IRecordDetailView{
    @BindView(R2.id.iv_sex)
    ImageView mSexIv;

    @BindView(R2.id.tv_age)
    TextView mAgeTv;

    @BindView(R2.id.tv_name)
    TextView mNameTv;

    @BindView(R2.id.tv_bed_number)
    TextView mNumberBed;

    @BindView(R2.id.tv_care_type)
    TextView mCareTypeTv;

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R2.id.tv_room_content)
    TextView mContentTv;

    @BindView(R2.id.photo_recyclerView)
    RecyclerView mPhotoRecyclerView;

    @Autowired(name = RouterKey.RECORD_DETAIL_TAG)
    public RecordInfoEntity mInfoEntity;

    private CheckRoomDetailAdapter mAdapter;
    private TakePhotoAdapter mPhotoAdapter;
    private List<PhotoEntity> photos = new ArrayList<>();
    private List<CheckRoomDetailResponse.ListBean> mList = new ArrayList<>();
    private ImageLoader imageLoader;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.home_act_room_detail;
    }

    @Override
    protected void init() {
        ARouter.getInstance().inject(this);
        setToolbarTopic(R.string.home_checkroom_detail);
        initRecyclerView();
        imageLoader = new ImageLoader(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    public void initListener() {
        mPhotoAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPhotoDialog(photos.get(position).getPhotoPath());
        });
    }

    private void showPhotoDialog(String path) {
        final Dialog dialog = new Dialog(this, R.style.home_PhotoDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.home_dialog_photo, null);
        PhotoView photoView = contentView.findViewById(R.id.photoview);
        imageLoader.loadImgByUrl(path, photoView);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;
            window.setAttributes(layoutParams);
        }
        dialog.show();
    }

    @Override
    public void initData() {
        if (mInfoEntity != null){
            /*if (mInfoEntity.getSex() == 0){
                mSexIv.setImageResource(R.mipmap.home_ic_famale);
            }else {
                mSexIv.setImageResource(R.mipmap.home_ic_male);
            }
            mAgeTv.setText(mInfoEntity.getAge() + "岁");*/
            mNameTv.setText(mInfoEntity.getName());
            mNumberBed.setText(mInfoEntity.getBedId() + "床");
            mCareTypeTv.setText(mInfoEntity.getCareType());
            mPresenter.checkRoomDetail(mInfoEntity.getId());
        }
    }

    @Override
    protected void setupActivityComponent() {

    }

    @Override
    public boolean isRegisterEventBus() {
        return false;
    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0
                , AppUtil.dip2px(mActivity, 1), AppUtil.getColor(mActivity, R.color.common_divder_color)));

        mAdapter = new CheckRoomDetailAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        mPhotoRecyclerView.setLayoutManager(manager);
        mPhotoRecyclerView.addItemDecoration(new SpacesItemDecoration(0
                , AppUtil.dip2px(mActivity, 1), AppUtil.getColor(mActivity, R.color.common_divder_color)));

        mPhotoAdapter = new TakePhotoAdapter(photos, false);
        mPhotoAdapter.setSpanSizeLookup((gridLayoutManager, position) -> photos.get(position).getSpanSize());
        mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        mPhotoRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void checkRoomDetailSucc(CheckRoomDetailResponse response) {
        List<CheckRoomDetailResponse.ListBean> listBeans = response.getList();
        mAgeTv.setText(response.getNianling() + "岁");
        mNumberBed.setText(response.getChuangwei() + "床");
        mCareTypeTv.setText(response.getHulijibie());
        mContentTv.setText(response.getBeizhu());
        if ("女".equals(response.getSex())) {
            mSexIv.setImageResource(R.mipmap.home_ic_famale);
        } else {
            mSexIv.setImageResource(R.mipmap.home_ic_male);
        }
        if (listBeans != null && listBeans.size() > 0){
            /*mAgeTv.setText(listBeans.get(0).getNianling() + "岁");
            mNumberBed.setText(listBeans.get(0).getChuangwei() + "床");
            mCareTypeTv.setText(listBeans.get(0).getHulijibie());*/
            mList.clear();
            mList.addAll(listBeans);
            mAdapter.notifyDataSetChanged();
        }

        List<CheckRoomDetailResponse.Photo> photoList = response.getTupian();
        if (photoList != null && photoList.size() > 0) {
            for (CheckRoomDetailResponse.Photo photo : photoList) {
                PhotoEntity photoEntity = new PhotoEntity(PhotoEntity.NORMAL_TYPE);
                photoEntity.setPhotoPath(photo.getLujing());
                photoEntity.setSpanSize(1);
                photos.add(photoEntity);
            }
            mPhotoAdapter.notifyDataSetChanged();
            mPhotoRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void careDetailSucc(CareDetailResponse response) {

    }

    @Override
    public void accidentDetailSucc(AccidentDetailResponse response) {

    }
}
