package addicto.myleader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    EditText e1,e2;
    Button b1;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        b1 = (Button) findViewById(R.id.main_bt1);
        e1 = (EditText) findViewById(R.id.main_et1);
        b1.setOnClickListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spe = sp.edit();

        if(sp.getBoolean(Store.user_isLogged,false)){

            Store.user_current = sp.getString(Store.user_name,"");
            Intent i =new Intent("addicto.myleader.State");
            startActivity(i);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.main_bt1){

            spe = sp.edit();
            spe.putString(Store.user_name,e1.getText().toString());
            spe.putBoolean(Store.user_isLogged,true);
            spe.commit();
            Store.user_current = e1.getText().toString();
            Intent i =new Intent("addicto.myleader.State");
            startActivity(i);
            finish();


        }

    }
}
