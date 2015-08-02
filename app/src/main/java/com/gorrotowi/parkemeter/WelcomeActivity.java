package com.gorrotowi.parkemeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

import com.gorrotowi.parkemeter.customelements.TextViewBariol;

public class WelcomeActivity extends AppCompatActivity {

    TextViewBariol txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String message = getString(R.string.welcome_message) + " <font color='#09a6d1'>" + SingletonCard.getMail() + "</font> para que puedas empezar a disfrutar de nuestra app";
        txtWelcome = (TextViewBariol) findViewById(R.id.txtWelcomeMail);

        txtWelcome.setText(Html.fromHtml(message));

    }

    public void passtomaps(View v) {
        startActivity(new Intent(WelcomeActivity.this, MapParkerActivity.class));
    }

}
