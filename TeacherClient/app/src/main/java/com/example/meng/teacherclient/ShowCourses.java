package com.example.meng.teacherclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private TeacherClientContext clientContext;
    private TeacherInfo teacherInfo;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        RequestBody formBody=formBodyBuilder.add("teacher_id",Integer.toString(teacherInfo.getTeacherId())).build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(clientContext.getUrl()+"teacher_have_course").post(formBody).build();
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
        clientContext=TeacherClientContext.getClientContextInstance();
        teacherInfo=clientContext.getTeacherInfo();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseInfo courseInfo=clientContext.getCourseInfoAt(position);
                clientContext.setCurrentCourse(courseInfo);
                Intent intent=new Intent(ShowCourses.this,TeacherOperation.class);
                startActivity(intent);
//                Toast.makeText(ShowCourses.this,courseInfo.toString(),Toast.LENGTH_SHORT)
//                        .show();
            }
        });
    }
}
