package com.example.meng.studentclient;

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

public class StudentLogin extends AppCompatActivity {
    private EditText studentIdEt;
    private EditText passwordEt;
    private StudentClientContext clientContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        studentIdEt=(EditText)findViewById(R.id.student_id_et);
        passwordEt=(EditText)findViewById(R.id.password_et);
        clientContext=StudentClientContext.getClientContext();
        //todo 修改ip地址
        clientContext.setUrl("http://192.168.168.101:5000/");
    }
    public void logIn(View v){
        OkHttpClient okHttpClient=clientContext.getClientContext().getOkHttpClient();
        String studentId=studentIdEt.getText().toString();
        String password=passwordEt.getText().toString();
        if(studentId==null||password==null)return;
        Log.i("debug", "logIn: "+"sid:"+studentId+"\n"+"pwd:"+password);
        final int studentIdInt=Integer.parseInt(studentId);
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        RequestBody requestBody=formBodyBuilder.add("student_id",studentId).
                add("password",password).build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(clientContext.getClientContext().getUrl()+"student_log_in")
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
                        Toast.makeText(StudentLogin.this,res,Toast.LENGTH_SHORT).show();
                        if("sucessful".equals(res)){
                            clientContext.setStudentInfo(new StudentInfo(studentIdInt));
                            Intent intent=new Intent(StudentLogin.this,ShowCourses.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}
