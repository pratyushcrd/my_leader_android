package addicto.myleader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Pratyush on 22-03-2015.
 */
public class Candidate extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    com.melnykov.fab.FloatingActionButton fab;
    ListView lv;
    private CardArrayAdapter cardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Store.action_color)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(Store.status_color));
        }

        getSupportActionBar().setTitle(Store.current_state_nm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Store.action_color)));
        lv = (ListView) findViewById(R.id.cand_list);
        cardArrayAdapter = new CardArrayAdapter(this, R.layout.candidate);
        new getInfo().execute(Store.host + "cand.php?area=" + Store.current_state);

        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.attachToListView(lv);

        lv.setOnItemClickListener(this);



    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fab){

            Intent i = new Intent("addicto.myleader.Dis");
            startActivity(i);


        }
    }

    RevealLayout mRevealLayout;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



        if(mRevealLayout!=null)
        if(mRevealLayout.isContentShown())
            mRevealLayout.hide();
        mRevealLayout = null;
        mRevealLayout = (RevealLayout) view.findViewWithTag(position);

                    if (mRevealLayout.isContentShown()) {

                        mRevealLayout.hide((int) mRevealLayout.getWidth()/2, (int) mRevealLayout.getHeight()/2, 250);

                    } else {

                        mRevealLayout.show((int) mRevealLayout.getWidth()/2, (int) mRevealLayout.getHeight()/2, 250);

                    }

    }


    class getInfo extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Candidate.this);
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
            try {
                ja = new JSONArray(s);
                for (i = 0; i < ja.length(); ++i) {

                    jo = ja.getJSONObject(i);

                    String age = "Age : " + jo.getString("age") + "\n";
                    String edu = "Education : " + jo.getString("edu") + "\n";
                    String asset = "Assets : " + jo.getString("asset") + "\n";
                    String cases = "Cases : " + jo.getString("cases") + "\n";
                    String charges = "Charges : " + jo.getString("charges");
                    String details = age + edu + asset + cases + charges;
                    card = new Card(jo.getString("name"), Store.host + "photos/" + jo.getString("pic") + ".jpg", details);

                    /**card = new Card(jo.getString("text"),jo.getString("profile_image_url_https"),FindUrls.extractUrls(jo.toString()));
                     **/
                    cardArrayAdapter.add(card);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


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


            return web_response;
        }

    }

}
