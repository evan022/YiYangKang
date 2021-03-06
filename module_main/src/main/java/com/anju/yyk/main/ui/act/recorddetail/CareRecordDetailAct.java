package com.anju.yyk.main.ui.act.recorddetail;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.anju.yyk.common.app.arouter.RouterConstants;
import com.anju.yyk.common.app.arouter.RouterKey;
import com.anju.yyk.common.base.BaseActivity;
import com.anju.yyk.common.base.BaseMvpActivity;
import com.anju.yyk.common.entity.response.AccidentDetailResponse;
import com.anju.yyk.common.entity.response.CareDetailResponse;
import com.anju.yyk.common.entity.response.CheckRoomDetailResponse;
import com.anju.yyk.common.utils.AppUtil;
import com.anju.yyk.common.widget.itemdecoration.SpacesItemDecoration;
import com.anju.yyk.main.R;
import com.anju.yyk.main.R2;
import com.anju.yyk.main.adapter.RecordDetailAdapter;
import com.anju.yyk.main.entity.RecordDetailEntity;
import com.anju.yyk.main.entity.RecordInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * 
 * @author LeoWang
 * 
 * @Package com.anju.yyk.main.ui.act.recorddetail
 * 
 * @Description 护理记录详情
 * 
 * @Date 2019/10/23 16:11
 * 
 * @modify:
 */
@Route(path = RouterConstants.ACT_URL_RECORD_DETAIL)
public class CareRecordDetailAct extends BaseMvpActivity<RecordDetailPresenter, RecordDetailModel> implements IRecordDetailContract.IRecordDetailView {

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

    @Autowired(name = RouterKey.RECORD_DETAIL_TAG)
    public RecordInfoEntity mInfoEntity;

    private RecordDetailAdapter mAdapter;
    private List<CareDetailResponse.ListBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.home_act_recorddetail;
    }

    @Override
    protected void init() {
        ARouter.getInstance().inject(this);
        setToolbarTopic(R.string.home_care_detail);
        initRecyclerView();
    }

    @Override
    public void initListener() {

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
            mPresenter.careDetail(mInfoEntity.getLaoren(), mInfoEntity.getDate());
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

        mAdapter = new RecordDetailAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void checkRoomDetailSucc(CheckRoomDetailResponse response) {

    }

    @Override
    public void careDetailSucc(CareDetailResponse response) {
        List<CareDetailResponse.ListBean> listBeans = response.getList();
        if (listBeans != null && listBeans.size() > 0){
            mAgeTv.setText(listBeans.get(0).getNianling() + "岁");
            mNumberBed.setText(listBeans.get(0).getChuangwei() + "床");
            mCareTypeTv.setText(listBeans.get(0).getHulijibie());
            if ("女".equals(listBeans.get(0).getSex())) {
                mSexIv.setImageResource(R.mipmap.home_ic_famale);
            } else {
                mSexIv.setImageResource(R.mipmap.home_ic_male);
            }
            mList.clear();
            mList.addAll(listBeans);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void accidentDetailSucc(AccidentDetailResponse response) {

    }
}
