package com.gorrotowi.parkemeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gorrotowi.parkemeter.customelements.EditTextBariol;
import com.simplify.android.sdk.model.Card;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMethod extends AppCompatActivity {

    EditTextBariol edtxCardNumber, edtxMonth, edtxYear, edtxCVV;
    JsonObjectRequest jsonRegisterCar;
    RequestQueue rq;
    MaterialDialog.Builder progressDialogBuild;
    MaterialDialog progressDialog;
    String matricula;
    String iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        rq = Volley.newRequestQueue(this);

        progressDialogBuild = new MaterialDialog.Builder(PaymentMethod.this)
                .content(R.string.wait_dialog)
                .cancelable(false)
                .progress(true, 0);

        progressDialog = progressDialogBuild.build();

        edtxCardNumber = (EditTextBariol) findViewById(R.id.edtxPaymentMethodNumberCard);
        edtxMonth = (EditTextBariol) findViewById(R.id.edtxPaymentMethodMonth);
        edtxYear = (EditTextBariol) findViewById(R.id.edtxPaymentMethodYear);
        edtxCVV = (EditTextBariol) findViewById(R.id.edtxPaymentMethodCVV);

        matricula = getIntent().getExtras().getString("matricula");
        iduser = getIntent().getExtras().getString("iduser");

    }

    public void registerUser(View v) {
        progressDialog.show();

        JSONObject jsonaddcar = new JSONObject();

        try {
            jsonaddcar.put("license", matricula);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonRegisterCar = new JsonObjectRequest(Request.Method.PATCH, getString(R.string.base_url) + getString(R.string.url_users) + "addCar/" + iduser, jsonaddcar, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Card card = new Card();
                card.setYear(edtxYear.getText().toString());
                card.setMonth(edtxMonth.getText().toString());
                card.setCvc(edtxCVV.getText().toString());
                card.setNumber(edtxCardNumber.getText().toString());
                SingletonCard.setCard(card);
                SingletonCard.setUserId(iduser);
                startActivity(new Intent(PaymentMethod.this, WelcomeActivity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(PaymentMethod.this, "Algo ocurrio mal, intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(jsonRegisterCar);

    }

}
