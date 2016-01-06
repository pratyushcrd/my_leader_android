package addicto.myleader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by Pratyush on 21-03-2015.
 */
public class State extends ActionBarActivity implements AdapterView.OnItemClickListener {

    String host_cont = Store.host+"cont.php";
    String[] cont;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Store.action_color)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(Store.status_color));
        }
        lv = (ListView) findViewById(R.id.state_list);
        lv.setOnItemClickListener(this);
           new CheckPass().execute(host_cont);
        Toast.makeText(this,"Welcome "+Store.user_current,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Store.current_state = position;
        Intent i = new Intent("addicto.myleader.Candidate");
        startActivity(i);

        Store.current_state_nm = cont[position];

        //finish();
    }

    class CheckPass extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                int i;
                JSONArray ja = new JSONArray(s);
                cont = new String[ja.length()];
                JSONObject jo;
                for (i = 0; i < ja.length(); ++i) {
                    jo = ja.getJSONObject(i);
                    cont[i] = jo.getString("name");
                }

                ArrayAdapter<String> aa = new ArrayAdapter<String>(State.this,R.layout.textview,cont);
                lv.setAdapter(aa);

            }catch (JSONException e) {}
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(State.this);
            pd.setMessage("Fetching Info...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "false";
            try {


                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                response = EntityUtils.toString(httpResponse.getEntity());
            } catch (MalformedURLException e) {
                return "false";
            } catch (ClientProtocolException e) {
                return "false";
            } catch (IOException e) {
                return "false";
            }

            return response;
        }
    }


}
