package com.example.meng.teacherclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PresentPracticeFragment extends Fragment {
    private TeacherClientContext clientContext;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_show_inclass,container,false);
        listView=(ListView)view.findViewById(R.id.show_inclass_lv);
        clientContext=TeacherClientContext.getClientContextInstance();
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,
                TeacherClientContext.getClientContextInstance().getInClassList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InclassInfo ref=clientContext.getInclassInfo(position);
                clientContext.setCurrentInclassInfo(ref);
                Intent intent=new Intent(getActivity(),ShowStudentScore.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
