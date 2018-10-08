package com.example.meng.studentclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowCourses extends AppCompatActivity {
    private TextView textView;
    private StudentClientContext clientContext;
    private StudentInfo studentInfo;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        RequestBody formBody=formBodyBuilder.add("student_id",Integer.toString(studentInfo.getStudentId())).build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(clientContext.getUrl()+"student_have_course").post(formBody).build();
        Call call=clientContext.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res=response.body().string();
                try {
                    final  String TAG="show_course";
                    JSONArray array=new JSONArray(res);
                    Log.i("show_course", "onResponse: "+array.length());
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.optJSONObject(i);
                        int cid=Integer.parseInt(object.getString("course_id"));
                        String cname=object.getString("course_name");
                        Log.i(TAG, "onResponse: "+"cid:"+cid+"\n"+"cname:"+cname);
                        CourseInfo courseInfo=new CourseInfo(cid,cname);
                        clientContext.addCourse(courseInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(res);
                    }
                });
            }
        });

        ArrayAdapter adapter=new ArrayAdapter(ShowCourses.this,android.R.layout.simple_list_item_1
                ,clientContext.getCourseList());;
        listView.setAdapter(adapter);
    }
    private void bindView(){
        setContentView(R.layout.activity_show_courses);
        listView=(ListView)findViewById(R.id.show_course_lv);
        textView=(TextView)findViewById(R.id.show_course_tv);
        clientContext=StudentClientContext.getClientContext();
        studentInfo=clientContext.getStudentInfo();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseInfo courseInfo=clientContext.getCourseInfoAt(position);
                clientContext.setCurrentCourse(courseInfo);
                Intent intent=new Intent(ShowCourses.this,StudentOperation.class);
                startActivity(intent);
            }
        });
    }
}
