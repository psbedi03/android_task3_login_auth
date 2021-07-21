package com.example.loginauth;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email_et, password_et;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_et = (EditText)findViewById(R.id.email_et);
        password_et = (EditText)findViewById(R.id.password_et);

        login_btn = (Button)findViewById(R.id.login_button);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_et.getText().toString();
                String password = password_et.getText().toString();

                if(TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".")){
                    Toast.makeText(MainActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                }else{
                    //proceed to login
                    login(email, password);

                }

            }
        });
    }

    public void login(String email, String password){

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoginResponse loginResponse = response.body();

                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("first_name", loginResponse.getFirst_name());
                            intent.putExtra("last_name", loginResponse.getLast_name());
                            startActivity(intent);

                        }
                    }, 1000);
                }else{
                    Toast.makeText(MainActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong at "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}