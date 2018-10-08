package com.example.meng.studentclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class PresentPracticeFragment extends Fragment {
    private StudentClientContext clientContext;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_show_inclass,container,false);
        listView=(ListView)view.findViewById(R.id.show_inclass_lv);
        clientContext=StudentClientContext.getClientContext();
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,
                StudentClientContext.getClientContext().getInclassList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clientContext.getQuestionList().clear();
                InclassInfo ref=clientContext.getInclassAt(position);
                clientContext.setCurrentInclass(ref);
                Toast.makeText(getActivity(),ref.toString(),Toast.LENGTH_SHORT).show();
                getQuestion();
                Intent intent=new Intent(getActivity(),AnswerQuestion.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
    private void getQuestion(){
        String url=clientContext.getUrl()+"student_get_inclass_question";
        int inclassId=clientContext.getCurrentInclass().getInclassId();
        FormBody.Builder builder=new FormBody.Builder();
        RequestBody requestBody=builder.add("inclass_id",Integer.toString(inclassId)).build();
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
                final String res=response.body().string();
                Log.i("question", "onResponse: "+res);
                try {
                    JSONArray jsonArray=new JSONArray(res);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.optJSONObject(i);
                        int qid=object.getInt("question_id");
                        int tid=object.getInt("teacher_id");
                        int cid=object.getInt("course_id");
                        String qContent=object.getString("question_content");
                        JSONObject qContentObj=new JSONObject(qContent);
                        String question=qContentObj.getString("question");
                        String options=qContentObj.getString("options");
                        JSONObject qObj=new JSONObject(options);
                        String optionA=qObj.getString("A");
                        String optionB=qObj.getString("B");
                        String optionC=qObj.getString("C");
                        String optionD=qObj.getString("D");
                        String rightAnswer=qContentObj.getString("answer");
                        Question q=new Question();
                        q.setQuestionId(qid);
                        q.setTeacherId(tid);
                        q.setCourseId(cid);
                        q.setContent(question);
                        q.setOptionA(optionA);
                        q.setOptionB(optionB);
                        q.setOptionC(optionC);
                        q.setOptionD(optionD);
                        q.setRightAnswer(rightAnswer);
                        clientContext.addQuestion(q);
                        Log.i("question", "onResponse: "+q.toJson());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
