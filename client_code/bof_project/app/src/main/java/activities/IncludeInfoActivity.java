package activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bof_project.R;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import Bean.IncludeItem;
import Bean.MusicItem;


public class IncludeInfoActivity extends AppCompatActivity {

    String TAG = IncludeInfoActivity.class.getCanonicalName();
    private HashMap<String,String>stringHashMap;
    public Handler mhandler;
    private ArrayList<IncludeItem> listData;
    String gameName;
    private final int WC=ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP=ViewGroup.LayoutParams.FILL_PARENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_include_info);

        Intent intent=getIntent();
        gameName=intent.getStringExtra("gameName");
        listData=new ArrayList<>();
        stringHashMap=new HashMap<>();
        stringHashMap.put("game_name",gameName);
        new Thread(postRun).start();
        mhandler=new mHandler();

//        TableLayout tableLayout=(TableLayout)findViewById(R.id.TableLayout01);
//        tableLayout.setStretchAllColumns(true);
//        for(int row=0;row<6;row++){
//            TableRow tableRow=new TableRow(IncludeInfoActivity.this);
//            tableRow.setBackgroundColor(Color.rgb(222, 220, 210));
//            for (int col = 0; col < 5; col++) {
//                TextView tv=new TextView(IncludeInfoActivity.this);
//                tv.setBackgroundResource(R.drawable.background_include);
//                tv.setText("hello");
//                tableRow.addView(tv);
//            }
//            tableLayout.addView(tableRow,new LayoutParams(FP, WC));
//        }
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
            String baseUrl = "http://121.4.253.217:8080/database_server/servlet/GetIncludeServlet";
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
                                String level1=item.getString("level1");
                                String level2=item.getString("level2");
                                String level3=item.getString("level3");
                                String level4=item.getString("level4");
                                int flag=item.getInt("includeFlag");
                                IncludeItem data=new IncludeItem(musicName,level1,level2,level3,level4,flag);
                                //MusicItem data=new MusicItem(bofName,musicName,musicCreator);
                                //MusicItem data=new MusicItem(bofName,"NAME","CREATOR");
                                listData.add(data);
                            }
                            Message msg = Message.obtain();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            msg.setData(bundle);//mes利用Bundle传递数据
                            mhandler.sendMessage(msg);
                            break;
                        default:
                            Looper.prepare();
                            Toast.makeText(IncludeInfoActivity.this,"数据库错误", Toast.LENGTH_LONG).show();
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
                Toast.makeText(IncludeInfoActivity.this,"Post方式请求失败", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
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

    //
    class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            TableLayout tableLayout=(TableLayout)findViewById(R.id.TableLayout01);
            tableLayout.setStretchAllColumns(true);
            for(int row=0;row<listData.size();row++){
                TableRow tableRow=new TableRow(IncludeInfoActivity.this);
                tableRow.setBackgroundColor(Color.rgb(222, 220, 210));
//                for (int col = 0; col < 5; col++) {
                TextView tv1=new TextView(IncludeInfoActivity.this);
                tv1.setBackgroundResource(R.drawable.background_include);
                tv1.setText(listData.get(row).getMusicName());
                tv1.setTextSize(10);
                tableRow.addView(tv1);

                TextView tv2=new TextView(IncludeInfoActivity.this);
                tv2.setBackgroundResource(R.drawable.background_include);
                tv2.setText(listData.get(row).getLevel1());
                tv2.setTextSize(10);
                tableRow.addView(tv2);

                TextView tv3=new TextView(IncludeInfoActivity.this);
                tv3.setBackgroundResource(R.drawable.background_include);
                tv3.setText(listData.get(row).getLevel2());
                tv3.setTextSize(10);
                tableRow.addView(tv3);

                TextView tv4=new TextView(IncludeInfoActivity.this);
                tv4.setBackgroundResource(R.drawable.background_include);
                tv4.setText(listData.get(row).getLevel3());
                tv4.setTextSize(10);
                tableRow.addView(tv4);

                TextView tv5=new TextView(IncludeInfoActivity.this);
                tv5.setBackgroundResource(R.drawable.background_include);
                tv5.setText(listData.get(row).getLevel4());
                tv5.setTextSize(10);
                tableRow.addView(tv5);

//                TextView tv6=new TextView(IncludeInfoActivity.this);
//                tv6.setBackgroundResource(R.drawable.background_include);
//                tv6.setText(listData.get(row).getFlag());
//                tableRow.addView(tv6);
                //}
                tableLayout.addView(tableRow,new LayoutParams(FP, WC));
            }
        }
    }




}