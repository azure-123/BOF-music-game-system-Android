package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.bof_project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class GameInfoActivity extends AppCompatActivity {
    public String gameName;
    private Bitmap bitmap1;
    String TAG = GameInfoActivity.class.getCanonicalName();
    private HashMap<String,String> stringHashMap;//传给服务端的数据在此
    public Handler mhandler;
    public Handler mhandler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        Intent intent=getIntent();
        gameName=intent.getStringExtra("gameName");
        TextView gameNameTextView=findViewById(R.id.game_name);
        gameNameTextView.setText("游戏名："+gameName);
        getGamePic();
        getGameInfo();
        FancyButton showMusic=findViewById(R.id.show_music);
        showMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameInfoActivity.this, IncludeInfoActivity.class);
                intent.putExtra("gameName",gameName);
                startActivity(intent);
            }
        });
    }

    private void getGamePic()
    {
        mhandler1=new mHandler1();
        new Thread(postRun1).start();
    }

    private void getGameInfo()
    {
        stringHashMap=new HashMap<>();
        stringHashMap.put("game_name",gameName);
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
    Runnable postRun1=new Runnable() {
        @Override
        public void run() {
            String path="http://121.4.253.217:8080/database_server/img/game_pic/"+gameName+".png";
            bitmap1=getPicture(path);
        }
    };

    public Bitmap getPicture(String path){
        Bitmap bm=null;
        BufferedInputStream inputStream=null;
        try{
            URL url=new URL(path);
            URLConnection connection  =url.openConnection();
            connection.connect();
            inputStream =new BufferedInputStream(connection.getInputStream());
            bm= BitmapFactory.decodeStream(inputStream);

            //新建一个Message对象，存储需要发送的消息
            Message message=Message.obtain();
            message.what=1;
            Bundle bundle=new Bundle();
            message.setData(bundle);
            //然后将消息发送出去
            mhandler1.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  bm;
    }


    private void requestPost(HashMap<String,String> paramsMap)
    {
        int code=20;
        try {
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/GetGameInfoServlet";
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
//                            String firstName=jsonObject1.getString("first_name");
//                            String secondName=jsonObject1.getString("second_name");
//                            String thirdName=jsonObject1.getString("third_name");
//                            String firstCreator=jsonObject1.getString("first_creator");
//                            String secondCreator=jsonObject1.getString("second_creator");
//                            String thirdCreator=jsonObject1.getString("third_creator");
                           String gameCname=jsonObject1.getString("gamecname");
                           String gameTime=jsonObject1.getString("gametime");
                           String gameCompany=jsonObject1.getString("gamecompany");
                           String gamePlatform=jsonObject1.getString("gameplatform");
                           String gameIntro=jsonObject1.getString("gameintro");
                            //无法直接通过子线程对组件内容进行修改，需要handler传值
                            Message msg = Message.obtain();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
//                            bundle.putString("first_name",firstName);
//                            bundle.putString("second_name",secondName);
//                            bundle.putString("third_name",thirdName);
//                            bundle.putString("first_creator",firstCreator);
//                            bundle.putString("second_creator",secondCreator);
//                            bundle.putString("third_creator",thirdCreator);
                            bundle.putString("game_cname",gameCname);
                            bundle.putString("game_time",gameTime);
                            bundle.putString("game_company",gameCompany);
                            bundle.putString("game_platform",gamePlatform);
                            bundle.putString("game_intro",gameIntro);
                            msg.setData(bundle);//mes利用Bundle传递数据
                            mhandler.sendMessage(msg);
                            //Toast弹出
                            Looper.prepare();
                            Toast.makeText(GameInfoActivity.this,"内容获取成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        default:
                            // 插入失败
                            Looper.prepare();
                            Toast.makeText(GameInfoActivity.this,"内容获取失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(GameInfoActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
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

    class mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            String gameCname=msg.getData().getString("game_cname");
            String gameTime=msg.getData().getString("game_time");
            String gameCompany=msg.getData().getString("game_company");
            String gamePlatform=msg.getData().getString("game_platform");
            String gameIntro=msg.getData().getString("game_intro");

            TextView gameCnameText=findViewById(R.id.game_cname);
            TextView gameTimeText=findViewById(R.id.game_time);
            TextView gameCompanyText=findViewById(R.id.game_company);
            TextView gamePlatformText=findViewById(R.id.game_platform);
            TextView gameIntroText=findViewById(R.id.game_intro);

            gameCnameText.setText("别名："+gameCname);
            gameTimeText.setText("发行日期："+gameTime);
            gameCompanyText.setText("发行公司："+gameCompany);
            gamePlatformText.setText("支持平台："+gamePlatform);
            gameIntroText.setText("游戏简介："+gameIntro);
        }
    }
    class mHandler1 extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    ImageView pic1=(ImageView) findViewById(R.id.game_pic);
                    pic1.setImageBitmap(bitmap1);
                    break;
                default:
                    break;
            }
        }
    }
}