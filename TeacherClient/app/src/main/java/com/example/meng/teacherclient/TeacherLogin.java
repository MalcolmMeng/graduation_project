package com.example.meng.teacherclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeacherLogin extends AppCompatActivity {
    private EditText teacherIdEt;
    private EditText passwordEt;
    private TeacherClientContext clientContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        teacherIdEt=(EditText)findViewById(R.id.teacher_id_et);
        passwordEt=(EditText)findViewById(R.id.password_et);
        clientContext=TeacherClientContext.getClientContextInstance();
        //todo 修改ip地址
        clientContext.setUrl("http://192.168.168.101:5000/");
    }
    public void logIn(View v){
        OkHttpClient okHttpClient=TeacherClientContext.getClientContextInstance().getOkHttpClient();
        String teacherId=teacherIdEt.getText().toString();
        String password=passwordEt.getText().toString();
        if(teacherId==null||password==null)return;
        Log.i("debug", "logIn: "+"tid:"+teacherId+"\n"+"pwd:"+password);
        final int teacherIdInt=Integer.parseInt(teacherId);
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        RequestBody requestBody=formBodyBuilder.add("teacher_id",teacherId).
                add("password",password).build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(TeacherClientContext.getClientContextInstance().getUrl()+"teacher_log_in")
                .post(requestBody).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TeacherLogin.this,res,Toast.LENGTH_SHORT).show();
                        if("sucessful".equals(res)){
                            clientContext.setTeacherInfo(new TeacherInfo(teacherIdInt,""));
                            Intent intent=new Intent(TeacherLogin.this,ShowCourses.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}
