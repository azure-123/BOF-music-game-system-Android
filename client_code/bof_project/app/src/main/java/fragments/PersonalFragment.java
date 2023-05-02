package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bof_project.R;
import com.lucasurbas.listitemview.ListItemView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

import SQLite.DBOpenHelper;
import activities.AlterFileActivity;
import activities.LoginActivity;
import activities.MainpageActivity;
import activities.ManageUserActivity;
import activities.ShowLikeActivity;
import adapter.SearchAdapter;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static PersonalFragment personalFragment;
    private boolean isCreated=false;
    private Button management;

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        isCreated=true;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated){
            return;
        }else {
            //进行刷新操作
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        personalFragment=this;
        userManagement();
        showPersonal();
        alterFile();
        showLike();
        exitLogin();
    }
    private boolean isAdmin()
    {
        DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity(),"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select role from user",null);
        String tempRole="";
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {//游标是否继续向下移动
                //通过列的索引获取对应的数据
                tempRole=cursor.getString(cursor.getColumnIndex("role"));
            }
            //关闭cursor
            cursor.close();
        }
        db.close();
        if(tempRole.equals("admin"))
            return true;
        else
            return false;
    }

    private void userManagement()
    {
        management=getView().findViewById(R.id.management);
        if(isAdmin()==false)
        {
            management.setVisibility(View.INVISIBLE);
        }
        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ManageUserActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showPersonal()
    {
        DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity(),"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select user_id,user_nickname,user_sex,user_birthday from user",null);
        int tempId=-1;
        String tempNickname="";
        String tempSex="";
        String tempBirthday="";
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {//游标是否继续向下移动
                //通过列的索引获取对应的数据
                tempId=cursor.getInt(cursor.getColumnIndex("user_id"));
                tempNickname=cursor.getString(cursor.getColumnIndex("user_nickname"));
                tempSex=cursor.getString(cursor.getColumnIndex("user_sex"));
                tempBirthday=cursor.getString(cursor.getColumnIndex("user_birthday"));
            }
            //关闭cursor
            cursor.close();
        }
        db.close();
        if(tempSex.equals("f"))
        {
            tempSex="女";
        }
        else if(tempSex.equals("m"))
        {
            tempSex="男";
        }
        else
        {
            tempSex="未知";
        }
        if(tempBirthday.equals(""))
        {
            tempBirthday="未知";
        }
        TextView userNickname= getView().findViewById(R.id.user_nickname);
        TextView userSex=getView().findViewById(R.id.user_sex);
        TextView userBirthday=getView().findViewById(R.id.user_birthday);

        userNickname.setText("昵称："+tempNickname);
        userSex.setText("性别："+tempSex);
        userBirthday.setText("生日："+tempBirthday);
    }

    private void alterFile()
    {
        ListItemView file=Objects.requireNonNull(getView()).findViewById(R.id.file);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AlterFileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showLike()
    {
        ListItemView like= Objects.requireNonNull(getView()).findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShowLikeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exitLogin()
    {
        //退出登录按钮
        ListItemView exit=Objects.requireNonNull(getView()).findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity(),"user_info.db",null,1);
                SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                String sql="delete from user";
                db.execSQL(sql);
                db.close();

                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                MainpageActivity.mainpageActivity.finish();
            }
        });
    }
}