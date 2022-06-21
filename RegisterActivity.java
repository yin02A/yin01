package com.example.yin;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import domain.User;
import tools.ToastTools;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_again)
    EditText etPasswordAgain;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_register_btn)
    TextView tvRegisterBtn;

    @Override
    public void initView() {
        contentView(R.layout.register_activity_yin);
        ButterKnife.bind(this);
        setTitle("注册");
        showBackImage();
        tvRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    /**
     * 账号密码注册
     */
    private void signUp() {
        String userName=etName.getText().toString().trim();
        String userPhone=etAccount.getText().toString().trim();
        String userEmail=etEmail.getText().toString().trim();
        String userPassword=etPassword.getText().toString().trim();
        String userPasswordAgain=etPasswordAgain.getText().toString().trim();

        if(userName.isEmpty()){
            ToastTools.showTextToast(context,"请输入用户名");
            return;
        }
        if(userPhone.isEmpty()){
            ToastTools.showTextToast(context,"请输入电话号码");
            return;
        }
        if(userEmail.isEmpty()){
            ToastTools.showTextToast(context,"请输入邮箱");
            return;
        }
        if(userPassword.isEmpty()){
            ToastTools.showTextToast(context,"请输入密码");
            return;
        }
        if(userPasswordAgain.isEmpty()){
            ToastTools.showTextToast(context,"请再次输入密码");
            return;
        }
        if(!userPassword.equals(userPasswordAgain)){
            ToastTools.showTextToast(context,"两次密码输入不一致");
            return;
        }

        final User user = new User();
        user.setUsername(userName);
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setMobilePhoneNumber(userPhone);
        user.setAge(18);
        user.setGender(0);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    ToastTools.showTextToast(context,"注册成功");
                } else {
                    ToastTools.showTextToast(context,e.getMessage());
                }
            }
        });
    }

}
