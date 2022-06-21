package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yin.PddGoodsDetailsActivity;
import com.example.yin.R;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tools.ImageLoadTool;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private Context context;
    private List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = new ArrayList<>();
    public GoodsAdapter(Context context,List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addAllData(List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataListUpdate){
        if (dataListUpdate != null && dataListUpdate.size()>0){
            dataList.addAll(dataListUpdate);
            notifyItemRangeChanged(dataList.size() -dataListUpdate.size(),dataListUpdate.size());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_item_pdd,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item = dataList.get(position);

        ImageLoadTool.imageLoad(context,item.getGoodsThumbnailUrl(),viewHolder.ivGoodsImage);

        viewHolder.tvGoodsName.setText(item.getGoodsName());
        viewHolder.tvGoodsDes.setText(item.getGoodsDesc());
        String coupon="";
        if (item.getCouponDiscount() != null){
            coupon = String.valueOf(item.getCouponDiscount()/100);
        }
        viewHolder.tvGoodsCoupon.setText(String.format("可领 %s元 优惠券", coupon));
        viewHolder.tvGoodsSales.setText(String.format("销量%s", item.getSalesTip()));
        String endTime = "";
        if(!coupon.equals("0")){
            if (item.getCouponEndTime() != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
                Date date = new Date(item.getCouponEndTime() * 1000);
                endTime = simpleDateFormat.format(date);
            }
            viewHolder.tvGoodsEndTime.setText(String.format("截至日期：%s", endTime));
        }else {
            viewHolder.tvGoodsEndTime.setText(String.format("优惠卷已被抢完"));
        }



        if(item.getBrandName()!=null)
            viewHolder.tvGoodsBrandName.setText(item.getBrandName()+" ");
        else
            viewHolder.tvGoodsBrandName.setText(item.getBrandName());

        viewHolder.tvMallName.setText(item.getMallName());








        /**
         * 当商品被点击后，跳转商品详情界面
         */
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PddGoodsDetailsActivity.class);
            intent.putExtra("goodId",item.getGoodsSign());
            intent.putExtra("searchId",item.getSearchId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivGoodsImage;
        private TextView tvGoodsName;
        private TextView tvGoodsDes;
        private TextView tvGoodsCoupon;
        private TextView tvGoodsSales;
        private TextView tvGoodsEndTime;
        private TextView tvGoodsBrandName;
        private TextView tvMallName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGoodsImage = itemView.findViewById(R.id.iv_goods_image);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsDes = itemView.findViewById(R.id.tv_goods_des);
            tvGoodsCoupon = itemView.findViewById(R.id.tv_goods_coupon);
            tvGoodsSales = itemView.findViewById(R.id.tv_goods_sales);
            tvGoodsEndTime = itemView.findViewById(R.id.tv_goods_end_time);
            tvGoodsBrandName=itemView.findViewById(R.id.tv_goods_brand_name);
            tvMallName=itemView.findViewById(R.id.tv_mall_name);
        }
    }
}

