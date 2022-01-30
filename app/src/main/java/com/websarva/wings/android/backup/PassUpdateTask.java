package com.websarva.wings.android.backup;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


class PassUpdateTask extends AsyncTask<String, Void, String> {

    private Listener listener;

    // 非同期処理
    @Override
    protected String doInBackground(String... params) {

        // 使用するサーバーのURLに合わせる
        String urlSt = "http://3.83.166.248/pass_update.php";

        HttpURLConnection httpConn = null;
        String result = null;
        String user = "user=" + params[0] + "&pass=" +params[1];

        try{
            // URL設定
            URL url = new URL(urlSt);

            // HttpURLConnection
            httpConn = (HttpURLConnection) url.openConnection();

            // request POST
            httpConn.setRequestMethod("POST");

            // no Redirects
            httpConn.setInstanceFollowRedirects(false);

            // データを書き込む
            httpConn.setDoOutput(true);

            // 時間制限
            httpConn.setReadTimeout(10000);
            httpConn.setConnectTimeout(20000);

            // 接続
            httpConn.connect();

            try(// POSTデータ送信処理
                OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream(),StandardCharsets.UTF_8);) {
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
            if (status == HttpURLConnection.HTTP_OK) {
                // レスポンスを受け取る処理等
                result="HTTP_OK";
            }
            else{
                result="status="+String.valueOf(status);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
}
