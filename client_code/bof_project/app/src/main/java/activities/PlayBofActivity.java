package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.dl7.player.media.IjkPlayerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.util.HashMap;

import SQLite.DBOpenHelper;
import mehdi.sakout.fancybuttons.FancyButton;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.example.bof_project.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayBofActivity extends AppCompatActivity {
    String TAG = PlayBofActivity.class.getCanonicalName();
    IjkPlayerView mPlayerView;
    IjkMediaPlayer mMediaPlayer;
    Uri mUri;
    private int musicId;
    private int userId;
    private HashMap<String,String> stringHashMap;
    private HashMap<String,String> stringHashMap1;
    private boolean likeFlag=false;
    public Handler mhandler;
    private FancyButton likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bof);

        Intent intent=getIntent();
        String bofName=intent.getStringExtra("bofName");
        int position=intent.getIntExtra("bofPosition",0);
        //TextView nameText=findViewById(R.id.bof_name);
        //nameText.setText(bofName);

        Security.setProperty("networkaddress.cache.ttl", String.valueOf(0));
        //mMediaPlayer.setDisplay();

        //mPlayerView 播放view
        mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);
        //网址↓
        mUri = Uri.parse("http://121.4.253.217:8080/database_server/video/"+bofName+"/"+String.valueOf(position+1)+".mp4");
        //mUri = Uri.parse("http://121.4.253.217:8080/database_server/video/BOFXVI/1.mp3");

        mPlayerView.init()
                .setVideoPath(mUri)
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_BD)
                .enableDanmaku()
                .start();

        Log.e("like bof",""+position);
        String [] bofNameArray={
                "BOFXVII","BOFXVI","BOFXV","G2R2018","BOFU2017","BOFU2016","BOFU2015",
                "G2R2014","BOF2013","BOF2012"};
        for(int i=0;i<bofNameArray.length;i++)
        {
            if(bofName.equals(bofNameArray[i]))
            {
                musicId=(21-i)*100+position+1;
                break;
            }
        }
        userId=userQuery();
        stringHashMap1=new HashMap<>();
        stringHashMap1.put("user_id",""+userId);
        stringHashMap1.put("music_id",""+musicId);
        mhandler=new mHandler();
        new Thread(postRun1).start();


        likeButton=findViewById(R.id.bof_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringHashMap=new HashMap<>();

                stringHashMap.put("user_id",""+userId);
                stringHashMap.put("music_id",""+musicId);
                stringHashMap.put("like_flag",""+likeFlag);
                Log.e("user like",""+musicId);
                likeFlag=!likeFlag;
                if(likeFlag)
                {
                    likeButton.setText("取消收藏");
                }
                else
                {
                    likeButton.setText("收藏");
                }

                new Thread(postRun).start();
            }
        });

    }
    private int userQuery()
    {
        DBOpenHelper dbOpenHelper=new DBOpenHelper(PlayBofActivity.this,"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
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
        db.close();
        return tempId;
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
            // TODO Auto-generated method stub
            requestPost1(stringHashMap1);
        }
    };

    private void requestPost1(HashMap<String,String> paramsMap)
    {
        int code=20;
        try {
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/JudgeLikeServlet";
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
                        case 2 :
                        case 3:
                            likeFlag= code != 2;//收藏记录不存在
                            Message msg = Message.obtain();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("like_flag",likeFlag+"");
                            msg.setData(bundle);
                            mhandler.sendMessage(msg);
                            Log.e(TAG,"LIKE FLAG:"+likeFlag);
                            break;
                        default:
                            // 插入失败
                            Looper.prepare();
                            Toast.makeText(PlayBofActivity.this,"数据上传失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(PlayBofActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            urlConn.disconnect();
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }

    private void requestPost(HashMap<String,String> paramsMap)
    {
        int code=20;
        try {
            String baseUrl="http://121.4.253.217:8080/database_server/servlet/InsertLikeServlet";
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
                            Toast.makeText(PlayBofActivity.this,"数据上传成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            break;
                        default:
                            // 插入失败
                            Looper.prepare();
                            Toast.makeText(PlayBofActivity.this,"数据上传失败", Toast.LENGTH_LONG).show();
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
                Toast.makeText(PlayBofActivity.this,"Post方式请求失败，没接上", Toast.LENGTH_LONG).show();
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
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            String likeString = msg.getData().getString("like_flag");
            Log.e(TAG, "handleMessage: "+likeString);
            if (likeString.equals("true")) {
                likeButton.setText("取消收藏");
            } else if (likeString.equals("false")) {
                likeButton.setText("收藏");
            }
        }
    }
}