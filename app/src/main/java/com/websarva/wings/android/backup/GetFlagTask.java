package com.websarva.wings.android.backup;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class GetFlagTask extends AsyncTask<String, Void, String> {

    private Listener listener;

    // 非同期処理
    protected String doInBackground(String... params) {

        // 使用するサーバーのURLに合わせる
        String urlSt = "http://3.83.166.248/get_flag.php";

        HttpURLConnection httpConn = null;
        String result = null;
        String user = "user=" + params[0];

        try{
            // URL設定
            URL url = new URL(urlSt);

            // HttpURLConnection
            httpConn = (HttpURLConnection) url.openConnection();

            // request POST
            httpConn.setRequestMethod("POST");

            // no Redirects
            httpConn.setInstanceFollowRedirects(false);

            // URL接続からデータを読み取る場合はtrue
            httpConn.setDoInput(true);

            // データを書き込む
            httpConn.setDoOutput(true);

            // 時間制限
            httpConn.setReadTimeout(10000);
            httpConn.setConnectTimeout(20000);

            // 接続
            httpConn.connect();

            try(// POSTデータ送信処理
                OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream(),StandardCharsets.UTF_8)) {
                out.write(user);
                out.flush();
                out.close();
                Log.d("debug","flush");
            } catch (IOException e) {
                // POST送信エラー
                e.printStackTrace();
                result = "POST送信エラー";
            }

            final int status = httpConn.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream in;
        try {
            in = httpConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuffer buffer = new StringBuffer();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                buffer.append(line);
                Log.d("CHECK", buffer.toString());
            }
            String jsonText = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsonText);
            result = jsonObject.getString("result");
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return result;
    }

    // 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(String result);
    }
    public String readInputStream(InputStream in)throws IOException{
        StringBuffer sb = new StringBuffer();
        String st;

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while((st = br.readLine()) != null)
        {
            sb.append(st);
        }
        try
        {
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
