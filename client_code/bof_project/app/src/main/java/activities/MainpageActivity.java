package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_project.R;

import fragments.ContestFragment;
import fragments.GameFragment;
import fragments.PersonalFragment;
import fragments.SearchFragment;

public class MainpageActivity extends AppCompatActivity {
    private RelativeLayout main_body;
    private TextView bottom_bar_text_1;
    private ImageView bottom_bar_image_1;
    private TextView bottom_bar_text_2;
    private ImageView bottom_bar_image_2;
    private TextView bottom_bar_text_3;
    private ImageView bottom_bar_image_3;
    private TextView bottom_bar_text_4;
    private ImageView bottom_bar_image_4;

    private LinearLayout main_body_bar;
    private RelativeLayout bottom_bar_1_btn;
    private RelativeLayout bottom_bar_2_btn;
    private RelativeLayout bottom_bar_3_btn;
    private RelativeLayout bottom_bar_4_btn;

    private ContestFragment contestFragment;
    private GameFragment musicFragment;
    private SearchFragment searchFragment;
    private PersonalFragment personalFragment;

    public static MainpageActivity mainpageActivity;

    private int TAG=1;
    private long exitTime=0;

    //SharedPreferences sp = this.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
//        if(GuideActivity.guideActivity!=null)
//            GuideActivity.guideActivity.finish();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,new ContestFragment()).commit();
        initView();

        contestFragment=new ContestFragment();
        musicFragment=new GameFragment();
        searchFragment=new SearchFragment();
        personalFragment=new PersonalFragment();

        mainpageActivity=this;
    }


    private View.OnClickListener MyListener=new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if(TAG==0)
            {
                TAG=1;
                switch (v.getId())
                {
                    case R.id.bottom_bar_1_btn:
                        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,contestFragment).commitAllowingStateLoss();
                        setSelectStatus(0);
                        break;

                    case R.id.bottom_bar_2_btn:
                        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,musicFragment).commitAllowingStateLoss();
                        setSelectStatus(1);
                        break;

                    case R.id.bottom_bar_3_btn:
                        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,searchFragment).commitAllowingStateLoss();
                        setSelectStatus(2);
                        break;

                    case R.id.bottom_bar_4_btn:
                        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,personalFragment).commitAllowingStateLoss();
                        setSelectStatus(3);
                        break;
                }
            }
            else if(TAG==1)
            {
                switch (v.getId())
                {
                    case R.id.bottom_bar_1_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,contestFragment).commitAllowingStateLoss();
                        setSelectStatus(0);
                        break;

                    case R.id.bottom_bar_2_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,musicFragment).commitAllowingStateLoss();
                        setSelectStatus(1);
                        break;

                    case R.id.bottom_bar_3_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,searchFragment).commitAllowingStateLoss();
                        setSelectStatus(2);
                        break;

                    case R.id.bottom_bar_4_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,personalFragment).commitAllowingStateLoss();
                        setSelectStatus(3);
                        break;
                }
            }
        }
    };

    private void initView()
    {
        main_body=findViewById(R.id.main_body);
        bottom_bar_text_1=findViewById(R.id.bottom_bar_text_1);
        bottom_bar_text_2=findViewById(R.id.bottom_bar_text_2);
        bottom_bar_text_3=findViewById(R.id.bottom_bar_text_3);
        bottom_bar_text_4=findViewById(R.id.bottom_bar_text_4);
        bottom_bar_image_1=findViewById(R.id.bottom_bar_image_1);
        bottom_bar_image_2=findViewById(R.id.bottom_bar_image_2);
        bottom_bar_image_3=findViewById(R.id.bottom_bar_image_3);
        bottom_bar_image_4=findViewById(R.id.bottom_bar_image_4);

        main_body_bar=findViewById(R.id.main_body_bar);
        bottom_bar_1_btn=findViewById(R.id.bottom_bar_1_btn);
        bottom_bar_2_btn=findViewById(R.id.bottom_bar_2_btn);
        bottom_bar_3_btn=findViewById(R.id.bottom_bar_3_btn);
        bottom_bar_4_btn=findViewById(R.id.bottom_bar_4_btn);

        bottom_bar_1_btn.setOnClickListener(MyListener);
        bottom_bar_2_btn.setOnClickListener(MyListener);
        bottom_bar_3_btn.setOnClickListener(MyListener);
        bottom_bar_4_btn.setOnClickListener(MyListener);

        bottom_bar_image_1.setImageResource(R.drawable.contest_icon);
        bottom_bar_text_1.setTextColor(Color.parseColor("#0097f7"));

        setSelectStatus(0);
    }

    public void setSelectStatus(int index)
    {
        switch (index)
        {
            case 0:
                bottom_bar_image_1.setImageResource(R.drawable.contest_icon);
                bottom_bar_text_1.setTextColor(Color.parseColor("#0097f7"));

                bottom_bar_image_2.setImageResource(R.drawable.music_icon);
                bottom_bar_text_2.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_3.setImageResource(R.drawable.search_icon);
                bottom_bar_text_3.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_4.setImageResource(R.drawable.personal_icon);
                bottom_bar_text_4.setTextColor(Color.parseColor("#000000"));

                break;

            case 1:
                bottom_bar_image_1.setImageResource(R.drawable.contest_icon);
                bottom_bar_text_1.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_2.setImageResource(R.drawable.music_icon);
                bottom_bar_text_2.setTextColor(Color.parseColor("#0097f7"));

                bottom_bar_image_3.setImageResource(R.drawable.search_icon);
                bottom_bar_text_3.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_4.setImageResource(R.drawable.personal_icon);
                bottom_bar_text_4.setTextColor(Color.parseColor("#000000"));
                break;

            case 2:
                bottom_bar_image_1.setImageResource(R.drawable.contest_icon);
                bottom_bar_text_1.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_2.setImageResource(R.drawable.music_icon);
                bottom_bar_text_2.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_3.setImageResource(R.drawable.search_icon);
                bottom_bar_text_3.setTextColor(Color.parseColor("#0097f7"));

                bottom_bar_image_4.setImageResource(R.drawable.personal_icon);
                bottom_bar_text_4.setTextColor(Color.parseColor("#000000"));
                break;

            case 3:
                bottom_bar_image_1.setImageResource(R.drawable.contest_icon);
                bottom_bar_text_1.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_2.setImageResource(R.drawable.music_icon);
                bottom_bar_text_2.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_3.setImageResource(R.drawable.search_icon);
                bottom_bar_text_3.setTextColor(Color.parseColor("#000000"));

                bottom_bar_image_4.setImageResource(R.drawable.personal_icon);
                bottom_bar_text_4.setTextColor(Color.parseColor("#0097f7"));
                break;
        }
    }
    //1.重写onBackPressed方法组织super即可实现禁止返回上一层页面
    public void onBackPressed(){
        //super.onBackPressed();
    }

    @Override
    //两次返回，返回到home界面（System.exit决定是否退出当前界面，重新加载程序）
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                //退出系统，不保存之前页面
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}