package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bof_project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

import mehdi.sakout.fancybuttons.FancyButton;

public class RegisterActivity extends AppCompatActivity {

    String TAG = RegisterActivity.class.getCanonicalName();
    private HashMap<String,String> stringHashMap;//传给服务端的数据在此
    public static RegisterActivity registerActivity;
    private RelativeLayout registerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        registerLayout=findViewById(R.id.register_layout);
        int []backgroundImage={R.drawable.login_background_1,R.drawable.login_background_2};
        Random r=new Random();
        registerLayout.setBackgroundResource(backgroundImage[r.nextInt(2)]);

        registerActivity=this;
        getInfo();
    }


    private void getInfo()
    {
        FancyButton register=findViewById(R.id.btn_register_confirm);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNickname=findViewById(R.id.et_data_uname);
                EditText userPassword=findViewById(R.id.et_data_upass);
                EditText userConfirm=findViewById(R.id.et_data_uconf);
                if(userPassword.getText().toString().equals("") || userNickname.getText().toString().equals("") || userNickname.getText().toString().equals(""))
                {
                    Toast.makeText(RegisterActivity.this,"信息输入不完整", Toast.LENGTH_LONG).show();
                }
                if(!userPassword.getText().toString().equals(userConfirm.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"密码输入不一致", Toast.LENGTH_LONG).show();
                }
                stringHashMap=new HashMap<>();
                stringHashMap.put("user_nickname",userNickname.getText().toString());
                stringHashMap.put("user_password",userPassword.getText().toString());

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
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/RegisterServlet";
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
                            //插入成功
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"数据上传成功", Toast.LENGTH_LONG).show();
                            if(registerActivity!=null)
                                registerActivity.finish();
                            Looper.loop();
                            break;
                        case 101:
                            //昵称已存在
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"该昵称已存在", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        default:
                            // 插入失败
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"数据上传失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(RegisterActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
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