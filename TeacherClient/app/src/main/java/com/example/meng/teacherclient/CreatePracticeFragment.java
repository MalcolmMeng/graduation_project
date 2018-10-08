package com.example.meng.teacherclient;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePracticeFragment extends Fragment {
    //create practice fragment
    private EditText weekEt;
    private EditText timesEt;
    private EditText contentEt;
    private EditText optionAEt;
    private EditText optionBEt;
    private EditText optionCEt;
    private EditText optionDEt;
    private EditText rightAnswerEt;
    private Button nextQuestionBt;
    private Button finishBt;
    private InclassInfo inclassInfo;
    private ArrayList questionList;
    private TeacherClientContext clientContext;
    private TextView showCourseNameTv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final String TAG="debug";
       View layout=inflater.inflate(R.layout.fragment_create_practice,container,false);
        Log.i(TAG, "onCreateView: "+"successful");
        bindView(layout);
       return layout;
    }
    private void bindView(View v){

        questionList=new ArrayList<Question>();
        clientContext=TeacherClientContext.getClientContextInstance();
        showCourseNameTv=(TextView)v.findViewById(R.id.show_course_name_tv);
        weekEt=(EditText)v.findViewById(R.id.week_et);
        timesEt=(EditText)v.findViewById(R.id.times_et);
        contentEt=(EditText)v.findViewById(R.id.question_content_et);
        optionAEt=(EditText)v.findViewById(R.id.optionA_et);
        optionBEt=(EditText)v.findViewById(R.id.optionB_et);
        optionCEt=(EditText)v.findViewById(R.id.optionC_et);
        optionDEt=(EditText)v.findViewById(R.id.optionD_et);
        rightAnswerEt=(EditText)v.findViewById(R.id.right_answer_et);
        nextQuestionBt=(Button)v.findViewById(R.id.next_question_bt);
        finishBt=(Button)v.findViewById(R.id.finish_bt);
        showCourseNameTv.setText(clientContext.getCurrentCourse().getCourseName());
        nextQuestionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInclass();
                final String TAG="debug";
                String content=contentEt.getText().toString();
                String optionA=optionAEt.getText().toString();
                String optionB=optionBEt.getText().toString();
                String optionC=optionCEt.getText().toString();
                String optionD=optionDEt.getText().toString();
                String rightAnswer=rightAnswerEt.getText().toString();
                if(content==""||optionA==""||optionB==""
                        ||optionC==""||optionD==""||rightAnswer=="")return;
                Log.i(TAG, "editNextQuestion: "+content);
                Question question=new Question();
                question.setContent(content);
                question.setOptionA(optionA);
                question.setOptionB(optionB);
                question.setOptionC(optionC);
                question.setOptionD(optionD);
                question.setRightAnswer(rightAnswer);
                questionList.add(question);
                try {
                    Toast.makeText(getActivity(),question.toJson(),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.i("create_practice", "onClick: "+question.toJson());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contentEt.setText("");
                optionAEt.setText("");
                optionBEt.setText("");
                optionCEt.setText("");
                optionDEt.setText("");
                rightAnswerEt.setText("");
            }
        });
        finishBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadQuestions();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                questionList.clear();
                Dialog dialog=new Dialog(getActivity());
                dialog.setTitle("题目已经全部上传");
                dialog.show();
            }
        });
    }
    private void uploadQuestions() throws JSONException {
        String url=clientContext.getUrl()+"teacher_upload_question";
        int cId=clientContext.getCurrentCourse().getCourseId();
        int inId=clientContext.getCurrentInclassInfo().getInclassId();
        int tId=clientContext.getTeacherInfo().getTeacherId();
        for(int i=0;i<questionList.size();i++){
            FormBody.Builder builder=new FormBody.Builder();
            RequestBody body=builder.add("teacher_id",String.valueOf(tId))
                    .add("course_id",String.valueOf(cId))
                    .add("inclass_id",String.valueOf(inId))
                    .add("question_content",((Question)questionList.get(i)).toJson())
                    .build();

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

                }
            });
        }
    }
    private void createInclass(){
        if(inclassInfo!=null)return;
        final String name=clientContext.getCurrentCourse().getCourseName()+weekEt.getText().toString()+timesEt.getText().toString();
        String id;
        final int cid,tid;
        cid=clientContext.getCurrentCourse().getCourseId();
        tid=clientContext.getTeacherInfo().getTeacherId();
        FormBody.Builder builder=new FormBody.Builder();
        RequestBody body=builder.add("course_id",String.valueOf(cid))
                .add("teacher_id",String.valueOf(tid))
                .add("inclass_name",name).build();

        Request.Builder requestBuilder=new Request.Builder();
        Request request=requestBuilder.get().url(clientContext.getUrl()+"teacher_create_inclass")
                .post(body).build();
        Call call=clientContext.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String inId=response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),inId,Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                inclassInfo=new InclassInfo(Integer.parseInt(inId),cid,name,false,true);
                clientContext.setCurrentInclassInfo(inclassInfo);
            }
        });

    }
}
