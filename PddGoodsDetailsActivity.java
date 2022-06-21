package com.example.yin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPidQueryRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPidQueryResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.BannerImageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import constants.Constants;

public class PddGoodsDetailsActivity extends BaseActivity {
    @BindView(R.id.goods_details_banner)
    Banner goodsDetailsBanner;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_des)
    TextView tvGoodsDes;
    @BindView(R.id.viewPager)
    ViewPager2 viewPager2;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private String goodsId;
    private String searchId;
    private String longUrl;
    private String shortUrl;
    private boolean isScrollViewPager;

    PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem goodsDetailsItem;
    private List<PddDdkGoodsPidQueryResponse.PIdQueryResponsePIdListItem> pidList;
    private Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            viewPager2.setCurrentItem(msg.what + 1);
        }
    };

    @Override
    public void initView() {
        contentView(R.layout.pdd_goods_details_page_yin);
        ButterKnife.bind(this);
        setTitle("商品详情");
        goodsId = getIntent().getStringExtra("goodId");
        searchId = getIntent().getStringExtra("searchId");
        initData();
        //createPromotionUrl();
        searchPromotionUrl();
    }

    @OnClick({R.id.tv_commit})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_commit:
                System.out.println("888888888888888888");
                System.out.println(shortUrl);
                if (shortUrl!=null && !shortUrl.isEmpty()){
                    //唤起拼多多App
                    Uri uri = Uri.parse(shortUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setPackage("com.xunmeng.pinduoduo");
                    startActivity(intent);
                    System.out.println("99999999999999999");
                }
                break;
        }
    }

    /**
     * 查询产品数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = Constants.PDD_clientId;
                String clientSecret = Constants.PDD_clientSecret;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
                String goodsIdList;
                goodsIdList=goodsId;
                request.setGoodsSign(goodsIdList);
                request.setSearchId(searchId);
                System.out.println("--------------000");
                System.out.println(JsonUtil.transferToJson(request));
                PddDdkGoodsDetailResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("--------------111");
                System.out.println(JsonUtil.transferToJson(response));

                assert response != null;
                String data = JsonUtil.transferToJson(response);

                /**
                 * {"error_response":{"error_code":50001,"error_msg":"业务服务错误","request_id":"16544001604615721",
                 * "sub_code":"10002","sub_msg":"goodsSign解析错误,请检查参数是否正确"}}
                 */
                System.out.println("--------------222");
                System.out.println(JsonUtil.transferToJson(goodsDetailsItem));
                goodsDetailsItem = response.getGoodsDetailResponse().getGoodsDetails().get(0);
                System.out.println("--------------333");
                System.out.println(JsonUtil.transferToJson(goodsDetailsItem));
                System.out.println("--------------444");
                //createPromotionUrl();
                runOnUiThread(new Runnable() {
                    @SuppressLint({"DefaultLocale", "ClickableViewAccessibility"})
                    @Override
                    public void run() {
                        //createPromotionUrl();
                        goodsDetailsBanner.setAdapter(new BannerImageAdapter(context, goodsDetailsItem.getGoodsGalleryUrls()))
                                .setIndicator(new CircleIndicator(context))
                                .setOrientation(Banner.HORIZONTAL)
                                .start();

                        tvGoodsName.setText(goodsDetailsItem.getGoodsName());
                        double a = goodsDetailsItem.getMinNormalPrice();
                        double d = a / 100;
                        tvGoodsPrice.setText(String.format("¥%.2f", d));
                        tvGoodsDes.setText(goodsDetailsItem.getGoodsDesc());

                    }
                });


            }
        }).start();
    }

    /**
     * 生成推广链接
     */
    private void createPromotionUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(goodsDetailsItem);
                System.out.println(goodsDetailsItem.getGoodsSign());
                String clientId = Constants.PDD_clientId;
                String clientSecret = Constants.PDD_clientSecret;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
                request.setCustomParameters("str");
                request.setGenerateMallCollectCoupon(true);
                request.setGenerateQqApp(true);
                request.setGenerateSchemaUrl(true);
                request.setGenerateShortUrl(true);
                request.setGenerateWeApp(true);
                List<String> goodsSignList = new ArrayList<String>();
                //181行报错  on a null object reference
                goodsSignList.add(goodsDetailsItem.getGoodsSign());
                System.out.println("333333333333333");
                System.out.println(goodsSignList);
                System.out.println("444444444444444");
                request.setGoodsSignList(goodsSignList);
                request.setMultiGroup(true);
                request.setPId(pidList.get(0).getPId());
                request.setSearchId(searchId);
                PddDdkGoodsPromotionUrlGenerateResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String data = JsonUtil.transferToJson(response);

                System.out.println("--------------000");
                System.out.println(JsonUtil.transferToJson(response));

                longUrl = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0).getMobileUrl();

                System.out.println("--------------111");
                System.out.println(JsonUtil.transferToJson(longUrl));

                shortUrl = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0).getSchemaUrl();
                System.out.println(JsonUtil.transferToJson(response));
            }
        }).start();

    }

    /**
     * 获取推广位
     */
    public void searchPromotionUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = Constants.PDD_clientId;
                String clientSecret = Constants.PDD_clientSecret;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsPidQueryRequest request = new PddDdkGoodsPidQueryRequest();
                request.setPage(1);
                request.setPageSize(10);
                PddDdkGoodsPidQueryResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pidList = response.getPIdQueryResponse().getPIdList();
            }
        }).start();

    }

}