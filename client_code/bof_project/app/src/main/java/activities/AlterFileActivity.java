package activities;

import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;
import com.example.bof_project.R;
import com.lucasurbas.listitemview.ListItemView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

import SQLite.DBOpenHelper;
import fragments.PersonalFragment;

public class AlterFileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private HashMap<String,String>stringHashMap;
    private int mark=-1;
    String TAG=AlterFileActivity.class.getCanonicalName();
    FragmentManager fragmentManager;
    private int birthdayYear;
    private int birthdayMonth;
    private int birthdayDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_file);

        ListItemView alterNickname=findViewById(R.id.alter_nickname);
        ListItemView alterSex=findViewById(R.id.alter_sex);
        ListItemView alterBirthday=findViewById(R.id.alter_birthday);
        fragmentManager = getSupportFragmentManager();
        PersonalFragment personalFragment;


        alterNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PanterDialog(AlterFileActivity.this)
                        .setHeaderBackground(R.drawable.pattern_bg_orange)
                        .withAnimation(Animation.SIDE)
                        .setDialogType(DialogType.INPUT)
                        .setTitle("修改昵称")
                        .isCancelable(false)
                        .input("请修改您的昵称", new
                                OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String text) {
                                        Toast.makeText(AlterFileActivity.this, text, Toast.LENGTH_LONG).show();
                                        //修改SQLite中的临时数据
                                        DBOpenHelper dbOpenHelper=new DBOpenHelper(AlterFileActivity.this,"user_info.db",null,1);
                                        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                                        Cursor cursor=db.rawQuery("select user_id from user",null);
                                        int tempId=-1;
                                        if (cursor != null && cursor.getCount() > 0) {

                                            while (cursor.moveToNext()) {//游标是否继续向下移动
                                                //通过列的索引获取对应的数据
                                                tempId=cursor.getInt(cursor.getColumnIndex("user_id"));
                                            }
                                            //关闭cursor
                                            cursor.close();
                                        }
                                        String sql="update user set user_nickname='"+text+"' where user_id="+tempId;
                                        Log.e(TAG,sql);
                                        db.execSQL(sql);
                                        db.close();
                                        //修改后端数据库中的永久数据
                                        mark=1;
                                        stringHashMap=new HashMap<>();
                                        stringHashMap.put("user_id",tempId+"");
                                        stringHashMap.put("user_nickname",text);
                                        new Thread(postRun).start();

                                    }
                                })
                        .show();
            }
        });

        alterSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PanterDialog(AlterFileActivity.this)
                        .setHeaderBackground(R.drawable.pattern_bg_blue)
                        .setDialogType(DialogType.SINGLECHOICE)
                        .setTitle("修改性别")
                        .withAnimation(Animation.SLIDE)
                        .isCancelable(false)
                        .items(R.array.sample_array, new OnSingleCallbackConfirmListener() {
                            @Override
                            public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                                Toast.makeText(AlterFileActivity.this, "position : " + String.valueOf(pos) +
                                                " value = " + text,
                                        Toast.LENGTH_LONG).show();
                                Toast.makeText(AlterFileActivity.this, text, Toast.LENGTH_LONG).show();
                                String sex;
                                if(text.equals("男"))
                                {
                                    sex="m";
                                }
                                else if(text.equals("女"))
                                {
                                    sex="f";
                                }
                                else
                                {
                                    sex="";
                                }
                                Log.e(TAG,text+" "+sex);
                                DBOpenHelper dbOpenHelper=new DBOpenHelper(AlterFileActivity.this,"user_info.db",null,1);
                                SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                                Cursor cursor=db.rawQuery("select user_id from user",null);
                                int tempId=-1;
                                if (cursor != null && cursor.getCount() > 0) {

                                    while (cursor.moveToNext()) {//游标是否继续向下移动
                                        //通过列的索引获取对应的数据
                                        tempId=cursor.getInt(cursor.getColumnIndex("user_id"));
                                    }
                                    //关闭cursor
                                    cursor.close();
                                }
                                String sql="update user set user_sex='"+sex+"'";
                                db.execSQL(sql);
                                db.close();
                                //修改后端数据库中的永久数据

                                mark=2;
                                stringHashMap=new HashMap<>();
                                stringHashMap.put("user_id",tempId+"");
                                stringHashMap.put("user_sex",sex);
                                new Thread(postRun).start();
                                //stringHashMap.clear();

                            }
                        })
                        .show();
            }
        });

        alterBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog ;
                Calendar calendar = Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = DatePickerDialog.newInstance(AlterFileActivity.this, year, month, day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("修改生日");
                fragmentManager =getSupportFragmentManager();
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(AlterFileActivity.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                datePickerDialog.show(fragmentManager, "DatePickerDialog");
            }
        });
    }

    Runnable postRun=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestPost(stringHashMap);
        }
    };

    private void requestPost(HashMap<String,String> paramsMap)
    {
        int code=20;
        try {
            String servlet="";
            if(mark==1)
            {
                servlet="AlterNicknameServlet";
            }
            else if(mark==2)
            {
                servlet="AlterSexServlet";
            }
            else
            {
                servlet="AlterBirthdayServlet";
            }
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/"+servlet;

            StringBuilder tempParams=new StringBuilder();
            int pos=0;
            for(String key:paramsMap.keySet()){
                if(pos>0)
                {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s",key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params=tempParams.toString();

            //新建URL对象
            URL url=new URL(baseUrl);
            //打开一个HttpURLConnection连接
            HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            //配置请求Content-Type
//            urlConn.setRequestProperty("Content-Type", "application/json");//post请求不能设置这个
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            PrintWriter dos = new PrintWriter(urlConn.getOutputStream());
            dos.write(params);
            dos.flush();
            dos.close();

            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        code = jsonObject.optInt("code");
                    }
                    switch (code){
                        case 1 :
                            //登录成功
                            Looper.prepare();
                            Toast.makeText(AlterFileActivity.this,"修改成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        case 101:
                            Looper.prepare();
                            Toast.makeText(AlterFileActivity.this,"用户名不存在或者密码错误！", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        default:
                            // 登录失败
                            Looper.prepare();
                            Toast.makeText(AlterFileActivity.this,"数据上传失败", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                    }
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            } else {
                Looper.prepare();
                Log.e(TAG, "Post方式请求失败");
                Toast.makeText(AlterFileActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            urlConn.disconnect();
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }

    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(++monthOfYear)+"-"+dayOfMonth;
        birthdayYear=year;
        birthdayMonth=monthOfYear;
        birthdayDay=dayOfMonth;
        Log.e(TAG,date);
        mark=3;
        stringHashMap=new HashMap<>();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(AlterFileActivity.this,"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select user_id from user",null);
        int tempId=-1;
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {//游标是否继续向下移动
                //通过列的索引获取对应的数据
                tempId=cursor.getInt(cursor.getColumnIndex("user_id"));
            }
            //关闭cursor
            cursor.close();
        }
        String sql="update user set user_birthday='"+date+"'";
        db.execSQL(sql);
        db.close();
        stringHashMap.put("user_id",tempId+"");
        stringHashMap.put("user_birthday",date);
        new Thread(postRun).start();
    }

}