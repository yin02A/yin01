package com.example.yin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import adapter.GoodsAdapter;
import adapter.PddCatsIconAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import constants.Constants;
import domain.PddGoodsCat;
import tools.CatData;
import tools.JsonTool;

public class PddGoodsListActivity extends BaseActivity {
    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.shop_srl)
    SmartRefreshLayout shopSrl;
    private GoodsAdapter goodsAdapter;

    private int page = 0;
    private int pageSize = 100;

    private String clientId;
    private String clientSecret;
    private PddGoodsCat cat;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
                    if (dataList != null && dataList.size() > 0) {
                        if (page == 1) {
                            goodsAdapter = new GoodsAdapter(context, dataList);
                            shopGoodsList.setAdapter(goodsAdapter);
                        } else {
                            if (goodsAdapter != null) {
                                goodsAdapter.addAllData(dataList);
                            }
                        }
                        if (dataList.size() < pageSize) {
                            shopSrl.setEnableLoadMore(false);
                        }
                    } else {
                        shopSrl.setEnableLoadMore(false);
                    }
                    shopSrl.finishLoadMore();
                    shopSrl.finishRefresh();
                    break;

            }

        }
    };


    @Override
    public void initView() {
        contentView(R.layout.pdd_goods_cat_list_activity_yin);
        ButterKnife.bind(this);
        cat = (PddGoodsCat) getIntent().getSerializableExtra("cat");
        setTitle(cat.getCat_name());
        showBackImage();
        clientId = Constants.PDD_clientId;
        clientSecret = Constants.PDD_clientSecret;
        /**
         * 下拉刷新
         */
        shopSrl.setOnRefreshListener(refreshLayout -> {
            //page = 0;
            initData();
        });
        /**
         * 上拉加载
         */
        shopSrl.setOnLoadMoreListener(refreshLayout -> {
            initData();
        });

        shopGoodsList.setLayoutManager(new LinearLayoutManager(context));
        initData();


    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setCatId(cat.getCat_id());
                request.setPage(page);
                request.setPageSize(pageSize);
                PddDdkGoodsSearchResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String data = JsonUtil.transferToJson(response);
                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = response.getGoodsSearchResponse().getGoodsList();
                Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);

            }
        }).start();
    }

}
