package activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bof_project.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class BofInfoActivity extends AppCompatActivity {

    String TAG = BofInfoActivity.class.getCanonicalName();
    private HashMap<String,String> stringHashMap;//传给服务端的数据在此

    public Handler mhandler;
    public String bofName;
    public int bofPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bof_info);

        Intent intent=this.getIntent();
        bofName=intent.getStringExtra("bof_name");
        bofPosition=intent.getIntExtra("bof_position",0);
        int [] bofImageArray={R.drawable.bof2021,R.drawable.bof2020,R.drawable.bof2019,R.drawable.bof2018,R.drawable.bof2017,
                R.drawable.bof2016,R.drawable.bof2015,R.drawable.bof2014,R.drawable.bof2013,R.drawable.bof2012};
        ImageView bofImage=findViewById(R.id.bof_image);
        bofImage.setImageResource(bofImageArray[bofPosition]);


        Log.e(TAG, "onCreate: "+bofName);
        getBofInfo();

        FancyButton rankMusic=findViewById(R.id.btn_rank_music);
        rankMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BofInfoActivity.this,MusicListActivity.class);
                intent.putExtra("bofName",bofName);
                startActivity(intent);
            }
        });
    }

    private void getBofInfo()
    {
//        bofName="BOFXVI";
        stringHashMap=new HashMap<>();
        stringHashMap.put("bof_name",bofName);

        mhandler=new mHandler();
        new Thread(postRun).start();

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
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/GetBofInfoServlet";
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
                            //获取成功
                            String jsondata=jsonObject.optString("data");
                            JSONObject jsonObject1=new JSONObject(jsondata);
                            String firstName=jsonObject1.getString("first_name");
                            String secondName=jsonObject1.getString("second_name");
                            String thirdName=jsonObject1.getString("third_name");
                            String firstCreator=jsonObject1.getString("first_creator");
                            String secondCreator=jsonObject1.getString("second_creator");
                            String thirdCreator=jsonObject1.getString("third_creator");
                            //无法直接通过子线程对组件内容进行修改，需要handler传值
                            Message msg = Message.obtain();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("first_name",firstName);
                            bundle.putString("second_name",secondName);
                            bundle.putString("third_name",thirdName);
                            bundle.putString("first_creator",firstCreator);
                            bundle.putString("second_creator",secondCreator);
                            bundle.putString("third_creator",thirdCreator);
                            msg.setData(bundle);//mes利用Bundle传递数据
                            mhandler.sendMessage(msg);
                            //Toast弹出
                            Looper.prepare();
                            Toast.makeText(BofInfoActivity.this,"内容获取成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        default:
                            // 插入失败
                            Looper.prepare();
                            Toast.makeText(BofInfoActivity.this,"内容获取失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BofInfoActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
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

    @SuppressLint("HandlerLeak")
    class mHandler extends Handler{
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            String firstName=msg.getData().getString("first_name");
            String secondName=msg.getData().getString("second_name");
            String thirdName=msg.getData().getString("third_name");
            String firstCreator=msg.getData().getString("first_creator");
            String secondCreator=msg.getData().getString("second_creator");
            String thirdCreator=msg.getData().getString("third_creator");
            TextView firstIdText=findViewById(R.id.first_id);
            TextView secondIdText=findViewById(R.id.second_id);
            TextView thirdIdText=findViewById(R.id.third_id);

            firstIdText.setText(firstName+"-"+firstCreator);
            secondIdText.setText(secondName+"-"+secondCreator);
            thirdIdText.setText(thirdName+"-"+thirdCreator);

        }
    }
}