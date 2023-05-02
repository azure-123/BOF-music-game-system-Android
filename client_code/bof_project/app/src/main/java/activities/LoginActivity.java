package activities;

import com.hanks.htextview.base.HTextView;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_project.R;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.suke.widget.SwitchButton;
import SQLite.DBOpenHelper;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends BaseActivity {

    String TAG = LoginActivity.class.getCanonicalName();
    public static LoginActivity loginActivity;
    private HashMap<String,String> stringHashMap;//传给服务端的数据在此
    private int ID;
    private String nickname;
    private String password;
    private String sex="";
    private String birthday="";
    private String role;
    private CheckBox chooseAdmin;
    private boolean isAdmin;
    private Timer timer;
    private HTextView loginTitle;
    private SwitchButton switchButton;
    private RelativeLayout loginLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Stetho.initializeWithDefaults(this);
        //chooseAdmin=findViewById(R.id.choose_admin);
        switchButton=findViewById(R.id.choose_admin);
        switchButton.setChecked(false);
        loginActivity=this;
        loginLayout=findViewById(R.id.login_layout);
        int []backgroundImage={R.drawable.login_background_1,R.drawable.login_background_2};
        Random r=new Random();
        loginLayout.setBackgroundResource(backgroundImage[r.nextInt(2)]);

        loginTitle=findViewById(R.id.title);
        loginTitle.setOnClickListener(new ClickListener());
        //loginTitle.setAnimationListener(new SimpleAnimationListener(this));
        register();
        login();


        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,3000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行
    }

    // (2) 使用handler处理接收到的消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                loginTitle.performClick();
            }
        }
    };
    private void SQLiteCreate()
    {
        DBOpenHelper dbOpenHelper=new DBOpenHelper(LoginActivity.this,"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        String sql="delete from user";
        db.execSQL(sql);
        //创建存放数据的ContentValues对象
        ContentValues values=new ContentValues();
        values.put("user_id",ID);
        values.put("user_nickname",nickname);
        values.put("user_password",password);
        values.put("user_sex",sex);
        values.put("user_birthday",birthday);
        values.put("role",role);
        Log.e(TAG,sex);


        db.insert("user",null,values);
        db.close();
    }

    private void register()
    {
        FancyButton ButtonRegister=findViewById(R.id.btn_register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login()
    {
        FancyButton ButtonLogin=findViewById(R.id.btn_login);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNickname=findViewById(R.id.et_data_nickname);
                EditText userPassword=findViewById(R.id.et_data_upass);
                if(switchButton.isChecked())
                {
                    isAdmin=true;
                    role="admin";
                }
                else
                {
                    isAdmin=false;
                    role="user";
                }
                nickname=userNickname.getText().toString();
                password=userPassword.getText().toString();
                stringHashMap=new HashMap<>();
                stringHashMap.put("user_nickname",nickname);
                stringHashMap.put("user_password",password);
                stringHashMap.put("role",role);

                new Thread(postRun).start();
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
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/LoginServlet";

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
                            String jsondata=jsonObject.optString("data");
                            JSONObject jsonObject1=new JSONObject(jsondata);
                            if(!isAdmin)
                            {
                                ID = jsonObject1.getInt("id");
                                sex = jsonObject1.getString("usersex");
                                birthday = jsonObject1.getString("userbirth");
                                SQLiteCreate();
                                Log.e(TAG, "用户ID:" + Integer.toString(ID));
                            }
                            else
                            {
                                ID=Integer.parseInt(nickname);
                                SQLiteCreate();
                            }
                            Intent intent=new Intent(LoginActivity.this,MainpageActivity.class);
                            startActivity(intent);
                            if(loginActivity!=null)
                                loginActivity.finish();
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        case 101:
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"用户名不存在或者密码错误！", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        default:
                            // 登录失败
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"数据上传失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(LoginActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
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
}