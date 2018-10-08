package com.example.meng.teacherclient;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherOperation extends AppCompatActivity {
    private TeacherClientContext clientContext;
    private FragmentManager fragmentManager;
    private CreatePracticeFragment createPracticeFragment;
    private PresentPracticeFragment presentPracticeFragment;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operations);
        fragmentManager=getFragmentManager();
        clientContext=TeacherClientContext.getClientContextInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void createPractice(View view){
        final String TAG="debug";
        //新建一个fragment
        createPracticeFragment=new CreatePracticeFragment();
        Log.i(TAG, "createPractice: "+"!!!");
        fragmentManager.beginTransaction().replace(R.id.content,createPracticeFragment).commit();
    }
    public void presentPractive(View view){
        String url=clientContext.getUrl()+"teacher_have_inclass";
        int tid=clientContext.getTeacherInfo().getTeacherId();
        int cid=clientContext.getCurrentCourse().getCourseId();
        FormBody.Builder formbuilder=new FormBody.Builder();
        FormBody body=formbuilder.add("teacher_id",String.valueOf(tid))
                .add("course_id",String.valueOf(cid)).build();

        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(url).post(body).build();
        Call call=clientContext.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                clientContext.clearInClassList();
                final String r=response.body().string();
                try {
                    JSONArray jsonArray=new JSONArray(r);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject o= (JSONObject) jsonArray.get(i);
                        int inclassId=Integer.parseInt((String) o.get("inclass_id"));
                        int cid=Integer.parseInt((String) o.get("course_id"));
                        int tid=Integer.parseInt((String)o.get("teacher_id"));
                        String name= (String) o.get("inclass_name");
                        InclassInfo inclassInfo=new InclassInfo(inclassId,cid,name,true,true);
                        clientContext.addInclassInfo(inclassInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(TeacherOperation.this,r,Toast.LENGTH_SHORT).show();
//                    }
//                });

                //新建一个fragment
                presentPracticeFragment=new PresentPracticeFragment();
                fragmentManager.beginTransaction().replace(R.id.content,presentPracticeFragment).commit();
            }
        });
    }
}
