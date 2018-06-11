package com.example.prasanna.autosavepic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


//import cz.msebera.android.httpclient.HttpEntity;
//import cz.msebera.android.httpclient.HttpResponse;
//import cz.msebera.android.httpclient.client.HttpClient;
//import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
//import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
//import cz.msebera.android.httpclient.client.methods.HttpPost;
//import cz.msebera.android.httpclient.entity.StringEntity;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;






public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    ImageView imageView;


    OkHttpClient client ;

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

//    String bowlingJson(String strImg) {
//        strImg = "kkk";
//        return ("{ \"pic_str\" : "+strImg+" }");
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.img);
        client = new OkHttpClient();
    }

    public void takePic(View view) {

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            // lz compression
            byte[] b = byteArrayOutputStream.toByteArray();

            String image_string = Base64.encodeToString(b, Base64.DEFAULT);


            Toast.makeText(this, image_string, Toast.LENGTH_SHORT).show();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("pic_str", image_string);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String json = jsonObject.toString();
            String response = null;
            try {
                 this.post("http://192.168.1.107:2935/picPost", json, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Something went wrong
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseStr = response.body().string();
                            // Do what you want to do with the response.
//                            Toast.makeText(MainActivity.this,responseStr,Toast.LENGTH_SHORT).show();

                        } else {
                                   // Request not successful
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }



}
