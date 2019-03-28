package com.example.timecapsule;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timecapsule.data.CapsuleStructure;

import java.util.ArrayList;
import java.util.List;

public class ViewCapsules extends AppCompatActivity {

    List<CapsuleStructure> capsulelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_capsules);


        initlist();
        CapsuleAdapter ca=new CapsuleAdapter(ViewCapsules.this,R.layout.capsule_item,capsulelist);
        ListView lv=(ListView)findViewById(R.id.listview);
        lv.setAdapter(ca);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CapsuleStructure c=capsulelist.get(position);
                AlertDialog.Builder dialog=new AlertDialog.Builder(ViewCapsules.this);
                dialog.setTitle("时间胶囊详情");
                StringBuilder sb=new StringBuilder();
                sb.append("胶囊名称："+c.capsulename+"\n");

                sb.append("总参与人数："+c.totalNum+"\n");
                sb.append("最少解密人数："+c.leastNum+"\n");
                //sb.append("胶囊体积："+c.MB+"MB"+"\n");
                sb.append(c.date+"\n");
                sb.append("预定解封时间：2019年03月28日"+"\n");

                dialog.setMessage(sb.toString());
//                dialog.setNegativeButton("开启本胶囊", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


    }

    private void initlist() {
        //TODO
        CapsuleStructure c1=new CapsuleStructure();
        c1.capsulename="《时间胶囊》课题组成果展示";
        c1.date="封存时间：2019年03月28日";
        c1.totalNum=12;
        c1.leastNum=8;
        c1.MB=100000;


        CapsuleStructure c2=new CapsuleStructure();
        c2.capsulename="高二(10)班同学聚会";
        c2.date="封存时间：2019年03月25日";
        c2.totalNum=12;
        c2.leastNum=8;

        CapsuleStructure c3=new CapsuleStructure();
        c3.capsulename="给50年后的我们";
        c3.date="封存时间：2019年03月25日";
        c3.totalNum=12;
        c3.leastNum=8;

        capsulelist.add(c1);
        capsulelist.add(c2);
        capsulelist.add(c3);
    }
}

class CapsuleAdapter extends ArrayAdapter<CapsuleStructure>{

    private final int resourceID;

    public CapsuleAdapter(Context context, int resource, List<CapsuleStructure> objects) {
        super(context, resource, objects);
        resourceID=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CapsuleStructure c=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        ImageView cimage=(ImageView)view.findViewById(R.id.capsule_image);
        TextView ctext=(TextView)view.findViewById(R.id.capsule_name);

        int m_id=R.drawable.green;
        switch (position){
            case 0:
                m_id=R.drawable.green;
                break;
            case 1:
                m_id=R.drawable.yellow;
                break;
            case 2:
                m_id=R.drawable.purple;
                break;

        }

        cimage.setImageResource(m_id);

        ctext.setText(c.capsulename+"\n"+c.date);

        return view;


    }
}