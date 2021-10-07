package com.example.wash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class ChooseActivity extends AppCompatActivity {
    private Fragment Wash_fragment;
    private Button btn_reserve,btn_dantuo,btn_biaozhun;
    private TextView txt_number,txt_status,txt_money,txt_time;
    private Toolbar toolbar;
    private ConstraintLayout biaozhun,dantuo;
    private int flag1=-1,flag2=-1;
    private String[] sArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        biaozhun=findViewById(R.id.constraintLayout2);
        dantuo=findViewById(R.id.constraintLayout4);
        toolbar=findViewById(R.id.toolbar);
        txt_number=(TextView) findViewById(R.id.choose_number);
        txt_status=(TextView)findViewById(R.id.choose_status);
        btn_reserve=findViewById(R.id.choose_btn_reserve);
        btn_dantuo=findViewById(R.id.btn_dantuo);
        btn_biaozhun=findViewById(R.id.btn_biaozhun);
        Intent intent=getIntent();
        String result=intent.getStringExtra("data");
        sArray=result.split(",");
        if (sArray[0].equals("忙碌")){
            txt_status.setTextColor(this.getResources().getColor(R.color.red));
            txt_number.setTextColor(this.getResources().getColor(R.color.red));
            btn_reserve.setBackgroundColor(this.getResources().getColor(R.color.red));
            btn_reserve.setText("正在忙碌中");
        }
        txt_number.setText("#"+sArray[1]);
        txt_status.setText("#"+sArray[0]);
        //Toast.makeText(this,result,Toast.LENGTH_LONG).show();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("data","");
                setResult(0,intent);
                finish();
            }
        });
    }

    public void btn_reserve(View view) {
        if (sArray[0].equals("忙碌")){
            Toast.makeText(this,"当前洗衣机正在工作中",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent();
        if(flag1==-1&&flag2==-1){
            Toast.makeText(this,"请先选择洗衣模式",Toast.LENGTH_LONG).show();
            return;
        }
        else if(flag1==1){
            txt_money=findViewById(R.id.biaozhun_money);
            txt_time=findViewById(R.id.biaozhun_time);
        }
        else if(flag2==1){
            txt_money=findViewById(R.id.dantuo_money);
            txt_time=findViewById(R.id.dantuo_time);
        }

        intent.putExtra("data",txt_number.getText()+","+txt_time.getText()+","+txt_money.getText());
        this.setResult(0,intent);
        this.finish();
    }

    public void btn_biaozhun(View view) {
        flag1=1;flag2=-1;
        dantuo.setBackgroundColor(Color.WHITE);
        biaozhun.setBackgroundColor(Color.rgb(220,220,220));
    }
    public void btn_dantuo(View view) {
        flag2=1;
        flag1=-1;
        dantuo.setBackgroundColor(Color.rgb(220,220,220));
        biaozhun.setBackgroundColor(Color.WHITE);
    }
}