package activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bof_project.R;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

import Bean.MusicItem;
import SQLite.DBOpenHelper;
import adapter.SearchAdapter;

public class ShowLikeActivity extends AppCompatActivity {

    String TAG=ShowLikeActivity.class.getCanonicalName();
    private HashMap<String,String> stringHashMap;
    public Handler mhandler;
    private SearchAdapter likeAdapter;
    private ListView listView;
    private List<MusicItem> likeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_like);

        likeList=new ArrayList<>();
        listView=findViewById(R.id.like_list);
        mhandler=new mHandler();
        likeAdapter=new SearchAdapter(ShowLikeActivity.this,likeList);
        stringHashMap=new HashMap<>();
        int userId = userQuery();

        stringHashMap.put("user_id",""+userId);
        new Thread(postRun).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String [] bofNameArray={
                        "BOFXVII","BOFXVI","BOFXV","G2R2018","BOFU2017","BOFU2016","BOFU2015",
                        "G2R2014","BOF2013","BOF2012"};
                int musicId=likeList.get(position).getMusicId();
                int bofPosition=musicId%100;
                String bofName=bofNameArray[21-musicId/100];
                Log.e(TAG,""+musicId);
                Intent intent=new Intent(ShowLikeActivity.this, PlayBofActivity.class);
                intent.putExtra("bofName",bofName);
                intent.putExtra("bofPosition",bofPosition-1);
                Log.e(TAG,""+bofPosition);
                startActivity(intent);
            }
        });
    }
    private int userQuery()
    {
        DBOpenHelper dbOpenHelper=new DBOpenHelper(ShowLikeActivity.this,"user_info.db",null,1);
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select user_id from user",null);
        int tempId=-1;
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {//游标是否继续向下移动
                //通过列的索引获取对应的数据
                tempId=cursor.getInt(cursor.getColumnIndex("user_id"));
                Log.e(TAG,tempId+"");
            }
            //关闭cursor
            cursor.close();
        }

        return tempId;
    }
    Runnable postRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestPost(stringHashMap);
        }
    };

    private void requestPost(HashMap<String, String> paramsMap) {
        int code = 20;
        try {
            String baseUrl = "http://121.4.253.217:8080/database_server/servlet/GetLikeServlet";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos >0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            Log.e(TAG,"params--post-->>"+params);
            // 请求的参数转换为byte数组
//            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
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
            // 判断请求是否成功
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
                            String jsondata = jsonObject.optString("data");
                            JSONArray jsonObject2 = new JSONArray(jsondata);
                            List<String> list2 = new ArrayList<String>();
                            for (int i=0; i<jsonObject2.length(); i++) {
                                list2.add( jsonObject2.getString(i) );
                            }
                            Log.e(TAG,list2.toString());
                            for (int i = 0; i <jsonObject2.length() ; i++) {
                                JSONObject item = jsonObject2.getJSONObject(i);
                                String musicName=item.getString("musicName");
                                String musicCreator=item.getString("musicCreator");
                                int musicId=item.getInt("musicId");
                                likeList.add(new MusicItem(musicId,musicName,musicCreator));
                                //gameList.add(gameName);
                            }
                            Message msg = Message.obtain();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            msg.setData(bundle);//mes利用Bundle传递数据
                            mhandler.sendMessage(msg);
                            break;
                        default:
                            Looper.prepare();
                            Toast.makeText(ShowLikeActivity.this,"数据库错误", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ShowLikeActivity.this,"Post方式请求失败", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
    class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // set elements to adapter
            listView.setAdapter(likeAdapter);
            likeAdapter.notifyDataSetChanged();
        }
    }
}