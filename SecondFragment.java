package fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yin.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import adapter.NewsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import domain.NewsResultData;
import tools.HttpTools;
import tools.JsonTool;

public class SecondFragment extends Fragment {
    @BindView(R.id.news_list)
    RecyclerView newsList;
    @BindView(R.id.news_srl)
    SmartRefreshLayout newsSrl;
    private Context context;
    private View view;
    private int page=0;
    private int pageSize=30;

    NewsResultData newsResultData;
    NewsAdapter newsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view != null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
            return view;
        }

        view = inflater.inflate(R.layout.second_fragment_yin, container, false);
        ButterKnife.bind(this, view);

        //下拉刷新
        newsSrl.setOnRefreshListener(refreshLayout -> {
            page++;
            initData();
        });

        //上拉加载
        newsSrl.setOnLoadMoreListener(refreshLayout -> {
            page++;
            initData();
        });


        newsList.setLayoutManager(new LinearLayoutManager(context));
        initData();
        return view;
    }

    public void initData(){
        page++;
//        HttpTools.getData("https://v.juhe.cn/toutiao/index?type=top&key=2e28441af02131d9d5ca81494c2fc0df");

        HttpTools.postData("https://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {
                    //fastjson解析 WebView展现
                    newsResultData= JsonTool.jsonToObject(data, NewsResultData.class);

                    if(newsResultData!=null && newsResultData.getError_code()==0){
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(newsResultData!=null&&newsResultData.getResult().getData().size()>0){
                                    if(page==1){
                                        newsAdapter=new NewsAdapter(context, newsResultData.getResult().getData());
                                        newsList.setAdapter(newsAdapter);
                                    }else {
                                        if(newsAdapter!=null){
                                            newsAdapter.addAllData(newsResultData.getResult().getData());
                                        }
                                    }
                                    if(newsResultData.getResult().getData().size()>pageSize){
                                        newsSrl.setEnableLoadMore(false);
                                    }
                                }else {
                                    newsSrl.setEnableLoadMore(false);
                                }

                            }
                        });
                    }else {

                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsSrl.setEnableLoadMore(false);
                                if(newsResultData!=null){
                                    Toast.makeText(context,newsResultData.getReason(),Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context,"数据异常",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                newsSrl.finishRefresh();
                newsSrl.finishLoadMore();
            }

            @Override
            public void onError(String error, int code) {

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsSrl.setEnableLoadMore(false);
                        if(error!=null && !error.isEmpty()){
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"数据异常",Toast.LENGTH_SHORT).show();
                        }
                        newsSrl.setEnableLoadMore(false);
                        newsSrl.finishRefresh();
                        newsSrl.finishLoadMore();
                    }
                });

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
