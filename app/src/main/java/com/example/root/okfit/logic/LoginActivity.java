package com.example.root.okfit.logic;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseActivity;
import com.example.root.okfit.base.BaseWebviewActivity;
import com.example.root.okfit.net.bean.Token;
import com.example.root.okfit.util.UserManager;
import com.fei.root.common.Rxbus.BusEvent;
import com.fei.root.common.Rxbus.BusSubscriber;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.functions.Action1;

import static com.example.root.okfit.CrRxbus.BusEvents.TEST;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.sign_up)
    TextView signUp;

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        resgisBusEvent();

    }


    @OnClick(R.id.login)
    public void onLoginClicked() {
        if (TextUtils.isEmpty(username.getText().toString())
                || TextUtils.isEmpty(password.getText().toString())) {
            return;
        }
        UserManager.login(this, username.getText().toString(), password.getText().toString(), new UserManager.OnLogin() {
            @Override
            public void onGetoken(Token token) {
                toast("登录成功");
            }
        }, (error -> {
            toast(error.getErrMsg());
            return true;
        }));
    }

    @OnLongClick(R.id.login)
    public boolean onHidenLogin() {
        UserManager.login(this);
        return false;
    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {
        BaseWebviewActivity.openLink(this, "https://www.diycode.cc/account/sign_up");
        /*BusObservable.bind().sendEvent(new BusEvent(SWITCH_TAB, "mine"));
        resgisBusEvent();*/
    }

    private void resgisBusEvent() {
        BusSubscriber.bind(this).bindEvent(TEST)
                .onNext(new Action1<BusEvent>() {
                    @Override
                    public void call(BusEvent busEvent) {
                        toast(busEvent.getContent());
                    }
                }).create(true);
    }

}
