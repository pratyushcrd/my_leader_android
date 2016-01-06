package addicto.myleader;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pratyush on 22-03-2015.
 */
public class Dis extends ActionBarActivity implements View.OnClickListener {


    public static boolean isRunning = true;
    ListView lv;
    private CardArrayAdapter2 cardArrayAdapter;
    addicto.myleader.PaperButton bt1;
    addicto.myleader.FloatingEditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);


        getSupportActionBar().setTitle(Store.current_state_nm+" Discussions");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Store.action_color)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(Store.status_color));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Store.action_color)));
        lv = (ListView) findViewById(R.id.chat_list);
        cardArrayAdapter = new CardArrayAdapter2(this, R.layout.chat);

        bt1 = (addicto.myleader.PaperButton) findViewById(R.id.chat_bt1);
        bt1.setText("Send");
        bt1.setOnClickListener(this);
        e1 =(addicto.myleader.FloatingEditText) findViewById(R.id.chat_et1);
        new getInfo().execute(Store.host + Store.current_state + "/posts.json");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.chat_bt1){

            new PostData().execute(Store.host + Store.current_state+"/post.php");
        }
    }

    class getInfo extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Dis.this);
            dialog.setMessage("Downloading details..");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Card card;
            JSONArray ja;
            int i;
            JSONObject jo;
            cardArrayAdapter.clear();
            cardArrayAdapter = null;
            cardArrayAdapter = new CardArrayAdapter2(Dis.this, R.layout.chat);
            try {
                ja = new JSONArray(s);
                for (i = 0; i < ja.length(); ++i) {

                    jo = ja.getJSONObject(i);

                    card = new Card(jo.getString("username"), Store.host + jo.getString("avatar"), jo.getString("text"));

                    /**card = new Card(jo.getString("text"),jo.getString("profile_image_url_https"),FindUrls.extractUrls(jo.toString()));
                     **/
                    cardArrayAdapter.add(card);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            lv.setAdapter(null);
            lv = null;
            lv = (ListView)(findViewById(R.id.chat_list));
            lv.setAdapter(cardArrayAdapter);

            dialog.dismiss();


        }

        @Override
        protected String doInBackground(String... params) {


            String web_response = "";
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);

                HttpResponse httpResponse = httpClient.execute(httpGet);


                web_response = EntityUtils.toString(httpResponse.getEntity());
            } catch (MalformedURLException e) {
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }


            return web_response+']';
        }

    }



    private class PostData extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return postData(params[0]);

        }

        protected void onPostExecute(String result) {

            Log.e("phpres",result);
            if (result.equals("success")) {
                Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_LONG).show();
                new getInfo().execute(Store.host + Store.current_state+"/posts.json");
                e1.setText("");
            }else
                Toast.makeText(getApplicationContext(), "Not Sent!", Toast.LENGTH_LONG).show();

            pd.hide();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Dis.this);
            pd.setMessage("Working...");
            pd.show();
        }

        public String postData(String val) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Store.host + Store.current_state + "/post.php");

            try {
                // Add your data
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user",sp.getString(Store.user_name,"") ));
                nameValuePairs.add(new BasicNameValuePair("msg", e1.getText().toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpclient.execute(httppost);
                String response = EntityUtils.toString(httpResponse.getEntity());

                return response;

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return val;
        }

    }

}
