package com.example.meng.studentclient;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerQuestion extends AppCompatActivity {
    private TextView content,optionA,optionB,optionC,optionD;
    private Spinner spinner;
    private Button previousQuestion,nextQuestion;
    private StudentClientContext clientContext;
    private int postionOfQuestion;
    private int questionNum;
    private ArrayList questionList;
    private boolean finished;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        bindView();
        postionOfQuestion=0;
        questionList=clientContext.getQuestionList();
        questionNum=clientContext.getQuestionList().size();
        Log.i("AnswerQuestion", "onCreate: "+questionNum);
        showQuestion();
    }
    private void bindView(){
        clientContext=StudentClientContext.getClientContext();
        content=(TextView)findViewById(R.id.show_content_tv);
        optionA=(TextView)findViewById(R.id.option_A_tv);
        optionB=(TextView)findViewById(R.id.option_B_tv);
        optionC=(TextView)findViewById(R.id.option_C_tv);
        optionD=(TextView)findViewById(R.id.option_D_tv);
        spinner=(Spinner)findViewById(R.id.student_option_spn);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Question question=clientContext.getQuestionAt(postionOfQuestion);
                if(position==0)question.setAnswer("A");
                else if(position==1)question.setAnswer("B");
                else if(position==2)question.setAnswer("C");
                else if(position==3)question.setAnswer("D");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void prePostion(){
        if(postionOfQuestion>0)
            postionOfQuestion-=1;
    }
    private void nextPostion(){
        postionOfQuestion=(++postionOfQuestion)%questionNum;
        if(postionOfQuestion==questionList.size()-1)finished=true;
    }
    public void nextQuestion(View view){
        Question question=clientContext.getQuestionAt(postionOfQuestion);
        int p=spinner.getSelectedItemPosition();
        if(p==0)question.setAnswer("A");
        else if(p==1)question.setAnswer("B");
        else if(p==2)question.setAnswer("C");
        else if(p==3)question.setAnswer("D");
        nextPostion();
        showQuestion();
    }
    public void previousQuestion(View view){
        prePostion();
        showQuestion();
    }
    private void showQuestion(){
        Question question= (Question) questionList.get(postionOfQuestion);
        content.setText(Integer.toString(postionOfQuestion+1)+"."+question.getContent());
        optionA.setText("A."+question.getOptionA());
        optionB.setText("B."+question.getOptionB());
        optionC.setText("C."+question.getOptionC());
        optionD.setText("D."+question.getOptionD());
        int p=0;
        String ans=question.getAnswer();
        if(ans==null)question.setAnswer("A");
        else if("A".equals(ans))p=0;
        else if("B".equals(ans))p=1;
        else if("C".equals(ans))p=2;
        else if("D".equals(ans))p=3;
        spinner.setSelection(p);
    }
    //todo 添加提交事件
    public void uploadAnswers(View view) throws JSONException {
        if(finished==false){
            Toast.makeText(this,"你还有题目没回答",Toast.LENGTH_SHORT).show();
            return;
        }
        int inclassId,studentId,rightNum=0,totalNum;
        inclassId=clientContext.getCurrentInclass().getInclassId();
        studentId=clientContext.getStudentInfo().getStudentId();
        totalNum=questionList.size();
        JSONArray array=new JSONArray();
        for(int i=0;i<questionList.size();i++){
            Question q= (Question) questionList.get(i);
            JSONObject object=new JSONObject();
            object.put("question_id",q.getQuestionId());
            boolean t=false;
            if(q.isRight()) {
                rightNum++;
                t=true;
            }
            object.put("right",t);
            array.put(object);
        }

        //提交
        String url=clientContext.getUrl()+"student_upload_answer";
        FormBody.Builder builder=new FormBody.Builder();
        RequestBody requestBody=builder.add("inclass_id", String.valueOf(inclassId))
                .add("student_id",String.valueOf(studentId))
                .add("right_num",String.valueOf(rightNum))
                .add("total_num",String.valueOf(totalNum))
                .add("question_result_list",array.toString()).build();
        Log.i("json", "uploadAnswers: "+array.toString());

        Request.Builder builder1=new Request.Builder();
        Request request=builder1.get().url(url).post(requestBody).build();
        Call call=clientContext.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("return", "onResponse: "+response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AnswerQuestion.this,"提交成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
