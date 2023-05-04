package com.example.mvptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoginInterface{
    private EditText edtEmail, edtPassword;
    private TextView tvMessage;
    private Button btnLogin;

    // khai báo presenter LoginPresenter để liên lạc
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // khởi tạo presenter LoginPresenter với instance của chính view MainActivity đã implements interface LoginInterface
        mLoginPresenter = new LoginPresenter(this);

        // lắng nghe sự kiện user click vào Button Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            // nếu user click vào Button Login, method clickLogin() sẽ được gọi
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        // 2 biến tạm dùng để lưu giá trị của 2 EditText
        String strEmail = edtEmail.getText().toString();
        String strPassword = edtPassword.getText().toString();

        // khởi tạo object model User
        User user = new User(strEmail, strPassword);

        // view MainActivity truyền dữ liệu cho presenter LoginPresenter
        mLoginPresenter.login(user);
    }

    @Override
    public void loginSuccess() {
        tvMessage.setText("Login Success");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_200, null));
        tvMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginError() {
        tvMessage.setText("Email or Password Invalid");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_700, null));
        tvMessage.setVisibility(View.VISIBLE);
    }
}