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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditTextBariol edtxName, edtxMail, edtxMatricula, edtxCountryCode, edtxPhone;
    JsonObjectRequest jsonRegisterUser;
    RequestQueue rq;
    MaterialDialog.Builder progressDialogBuild;
    MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rq = Volley.newRequestQueue(this);

        progressDialogBuild = new MaterialDialog.Builder(RegisterActivity.this)
                .content(R.string.wait_dialog)
                .cancelable(false)
                .progress(true, 0);

        progressDialog = progressDialogBuild.build();

        edtxName = (EditTextBariol) findViewById(R.id.edtxRegisterName);
        edtxMail = (EditTextBariol) findViewById(R.id.edtxRegisterMail);
        edtxMatricula = (EditTextBariol) findViewById(R.id.edtxRegisterMatricula);
        edtxPhone = (EditTextBariol) findViewById(R.id.edtxRegisterPhone);

    }

    public void nexRegister(View v) {

        progressDialog.show();

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("fname", edtxName.getText().toString().split("\\s+")[0]);
            jsonUser.put("lname", edtxName.getText().toString().split("\\s+")[1]);
            jsonUser.put("mail", edtxMail.getText().toString());
            jsonUser.put("telephone", edtxPhone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonRegisterUser = new JsonObjectRequest(Request.Method.POST, getString(R.string.base_url) + getString(R.string.url_users), jsonUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Intent intent = new Intent(RegisterActivity.this, PaymentMethod.class);
                SingletonCard.setMail(edtxMail.getText().toString());
                intent.putExtra("matricula", edtxMatricula.getText().toString());
                try {
                    intent.putExtra("iduser", response.getString("_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(RegisterActivity.this, "Algo ocurrio mal, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });


        rq.add(jsonRegisterUser);
    }

}
