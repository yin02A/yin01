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

import com.example.yin.R;
import com.example.yin.WebViewActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import domain.News;
import tools.ImageLoadTool;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<News> dataList = new ArrayList<>();
    public NewsAdapter(Context context, List<News> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addAllData(List<News> dataListUpdate){
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
        News item = dataList.get(position);
        ImageLoadTool.imageLoad(context,item.getThumbnail_pic_s(),viewHolder.ivGoodsImage);
        viewHolder.tvGoodsName.setText(item.getTitle());
        viewHolder.tvGoodsDes.setText(item.getAuthor_name());

        viewHolder.tvGoodsCoupon.setText(item.getCategory());
        viewHolder.tvGoodsSales.setText("");

        viewHolder.tvGoodsEndTime.setText(item.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",item.getUrl());
                context.startActivity(intent);
            }
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
            tvGoodsBrandName.setText("");
            tvMallName.setText("");
        }
    }
}