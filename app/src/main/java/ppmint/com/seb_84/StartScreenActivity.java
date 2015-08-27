package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class StartScreenActivity extends Activity {

    Thread loadThread;
    private static final String LOG_TAG = "StartScreen";
    ProgressBar pbStartScreen;
    TextView tvLogo;
    TextView tvEnter;
    EditText etHeroName;
    Button btnSubm;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        pbStartScreen = (ProgressBar)findViewById(R.id.pbStartScreen);
        pbStartScreen.setMax(30);
        tvLogo = (TextView) findViewById(R.id.textView);
        tvEnter = (TextView)findViewById(R.id.textView2);
        etHeroName = (EditText)findViewById(R.id.edHero_name);
        btnSubm = (Button)findViewById(R.id.button);
        btnSubm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etHeroName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),getString(R.string.toast_noname),Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent("android.intent.action.HERO");
                    intent.putExtra("hero_name", etHeroName.getText().toString());

                    startActivity(intent);
                    finish();
                }
            }
        });
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key_name), Context.MODE_PRIVATE);


        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1;i<=30;i++){
                    try{
                        TimeUnit.MILLISECONDS.sleep(100);
                        pbStartScreen.setProgress(i);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Error while charging: at sleep point");
                    }
                }
                if (sharedPref.contains(getString(R.string.preference_hero_name_key)))  {
                mHandler.sendEmptyMessage(1);}
                else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
        loadThread.start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent("android.intent.action.HERO");
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    pbStartScreen.setVisibility(View.GONE);
                    tvLogo.setVisibility(View.GONE);
                    tvEnter.setVisibility(View.VISIBLE);
                    btnSubm.setVisibility(View.VISIBLE);
                    etHeroName.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


}
