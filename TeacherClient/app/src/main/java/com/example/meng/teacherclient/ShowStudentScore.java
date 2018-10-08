package com.example.meng.teacherclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowStudentScore extends AppCompatActivity {
    //todo activity 展示成绩
    private TeacherClientContext clientContext;
    private TextView textView;
    private ListView listView;
    class QuestionHolder{
        private String questionContent,optionA,optionB,optionC,optionD;
        private int totalStudentNum;
        private int rightStudentNum;
        public void loadFromJson(String jsonString) throws JSONException {
            JSONObject object=new JSONObject(jsonString);
            totalStudentNum=object.getInt("total_student_num");
            rightStudentNum=object.getInt("right_student_num");
            JSONObject qObj=new JSONObject(object.getString("question_info"));
            JSONObject ctObj=new JSONObject(qObj.getString("question_content"));
            questionContent=ctObj.getString("question");
            JSONObject opObj=new JSONObject(ctObj.getString("options"));
            optionA="A."+opObj.getString("A");
            optionB="B."+opObj.getString("B");
            optionC="C."+opObj.getString("C");
            optionD="D."+opObj.getString("D");
        }

        @Override
        public String toString() {
            return questionContent+"\n"
                    +optionA+"\n"
                    +optionB+"\n"
                    +optionC+"\n"
                    +optionD+"\n"
                    +"回答情况:"+String.valueOf(rightStudentNum)+"/"
                    +String.valueOf(totalStudentNum)+"\n";
        }
    }
    private ArrayList holderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_score);
        clientContext=TeacherClientContext.getClientContextInstance();
        textView=(TextView)findViewById(R.id.inclass_state_tv);
        listView=(ListView)findViewById(R.id.inclass_state_lv);
        InclassInfo ref=clientContext.getCurrentInclassInfo();
        textView.setText(ref.toString()+"\n"+"答题情况");
        holderList=new ArrayList();
        //请求数据
        getStudentAnswerData();
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,holderList);
        listView.setAdapter(adapter);
    }
    private void getStudentAnswerData(){
        //todo getStudentAnswerData
        String inclassId=String.valueOf(clientContext.getCurrentInclassInfo().getInclassId());
        String url=clientContext.getUrl()+"teacher_get_student_answers";
        FormBody.Builder builder=new FormBody.Builder();
        RequestBody body=builder.add("inclass_id",inclassId).build();
        Request.Builder builder1=new Request.Builder();
        Request request=builder1.get().url(url).post(body).build();
        Call call=clientContext.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String s=response.body().string();
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        QuestionHolder holder = new QuestionHolder();
                        holder.loadFromJson(array.getString(i));
                        holderList.add(holder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        try {
//                            //todo 构造holderList
//                            Toast.makeText(ShowStudentScore.this,response.body().string(),
//                                    Toast.LENGTH_SHORT).show();

//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
            }
        });
    }
}
