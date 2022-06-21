package com.example.yin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewPageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import fragment.FirstFragment;
import fragment.FourthFragment;
import fragment.SecondFragment;
import fragment.ThirdFragment;
import tools.ToastTools;
import view.NoScrollViewPager;

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.first_radio)
    RadioButton firstRadio;
    @BindView(R.id.type_radio)
    RadioButton typeRadio;
    @BindView(R.id.shopping_car_radio)
    RadioButton shoppingCarRadio;
    @BindView(R.id.mine_radio)
    RadioButton mineRadio;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;


    List<Fragment> fragmentList=new ArrayList<>();
    ViewPageAdapter viewPageAdapter;

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourthFragment fourthFragment;

    private MainReceiver mainReceiver;
    public static String MAIN_BROADCAST="com.example.yin.broadcast";

    @Override
    public void initView() {
        contentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        setTitle("购物商城");
        radioGroup.check(R.id.first_radio);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(MAIN_BROADCAST);
        mainReceiver=new MainReceiver();
        registerReceiver(mainReceiver,intentFilter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        radioGroup.check(R.id.first_radio);
                        break;
                    case 1:
                        radioGroup.check(R.id.type_radio);
                        break;
                    case 2:
                        radioGroup.check(R.id.shopping_car_radio);
                        break;
                    case 3:
                        radioGroup.check(R.id.mine_radio);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.first_radio:
                        viewPager.setCurrentItem(0);
                        setTitle("购物商城");
                        break;
                    case R.id.type_radio:
                        viewPager.setCurrentItem(1);
                        setTitle("今日头条");
                        break;
                    case R.id.shopping_car_radio:
                        viewPager.setCurrentItem(2);
                        setTitle("开心一刻");
                        break;
                    case R.id.mine_radio:
                        viewPager.setCurrentItem(3);
                        setTitle("个人主页");
                        break;
                }
            }
        });

    }

    class MainReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String flag=intent.getStringExtra("flag");
            assert flag!=null;
            switch (flag){
                case "refresh_user_info":
                    ToastTools.showTextToast(context,"刷新用户信息");
                    break;
            }
        }
    }


    private void initData() {
        firstFragment=new FirstFragment();
        secondFragment=new SecondFragment();
        thirdFragment=new ThirdFragment();
        fourthFragment=new FourthFragment();

        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);
        fragmentList.add(fourthFragment);

        viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPageAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainReceiver);
    }
}
