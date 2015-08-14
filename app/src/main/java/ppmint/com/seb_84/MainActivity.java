package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    public String LOG_TAG = "MainScreen";

    SharedPreferences sharedPref;
    TextView tvName;
    TextView tvLvl;
    TextView tvStr;
    TextView tvAgl;
    TextView tvInt;
    TextView tvEnd;
    TextView tvAttack;
    TextView tvCh;
    TextView tvAccur;
    TextView tvDef;
    TextView tvDd;
    TextView tvStPoints;
    TextView tvTrn;
    ProgressBar pbHP;
    ProgressBar pbExp;
    ProgressBar pbTrain;
    Button btnTrn;
    Button btnBtl;
    public Hero hero;
    Timer timer;
    TimerTask hpRegeneration;
    Handler mHandler;
    int timeTrain;
    Timer trnTimer;
    TimerTask trainTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLvl = (TextView) findViewById(R.id.tvLvl);
        tvStr = (TextView) findViewById(R.id.tvStr);
        tvAgl = (TextView) findViewById(R.id.tvAgl);
        tvInt = (TextView) findViewById(R.id.tvInt);
        tvEnd = (TextView) findViewById(R.id.tvEnd);
        tvAttack = (TextView) findViewById(R.id.tvAttack);
        tvCh = (TextView) findViewById(R.id.tvCh);
        tvAccur = (TextView) findViewById(R.id.tvAccur);
        tvDef = (TextView) findViewById(R.id.tvDef);
        tvDd = (TextView) findViewById(R.id.tvDd);
        tvStPoints = (TextView) findViewById(R.id.tvStPoints);
        pbHP = (ProgressBar) findViewById(R.id.pbHP);
        pbExp = (ProgressBar) findViewById(R.id.pbExp);
        btnTrn = (Button) findViewById(R.id.btnTrn);
        btnBtl = (Button) findViewById(R.id.btnBtl);
        pbTrain = (ProgressBar) findViewById(R.id.pbTrn);
        tvTrn = (TextView) findViewById(R.id.tvTrn);


        sharedPref = getSharedPreferences(getString(R.string.preference_file_key_name), Context.MODE_PRIVATE);
        if (sharedPref.contains(getString(R.string.preference_hero_name_key))){
            hero = new Hero(sharedPref,"sasiska",this);
            updateScreen();}
        else {
            Intent intent = getIntent();
            String nameHero = intent.getStringExtra("hero_name");
            hero = new Hero(sharedPref,nameHero,this);
            updateScreen();
        }
        btnTrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("lvl", hero.lvl);
                startActivityForResult(intent, 1);
                lvlUp();
                updateScreen();
            }
        });
        timer = new Timer(true);
        hpRegeneration = new TimerTask() {
            @Override
            public void run() {
                if(hero.hp >= hero.maxHP){
                    hero.hp = hero.maxHP;
                    timer.cancel();
                }
                else
                    hero.hp += hero.endurance;
                mHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(hpRegeneration,200,60000);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message message){
                switch(message.what){
                    case 1: hero.exp += hero.lvl;
                        pbTrain.setVisibility(View.GONE);
                        tvTrn.setVisibility(View.GONE);
                        btnTrn.setClickable(true);
                        break;
                    case 2: hero.exp += hero.lvl*2;
                        pbTrain.setVisibility(View.GONE);
                        tvTrn.setVisibility(View.GONE);
                        btnTrn.setClickable(true);
                        break;
                    case 3: hero.exp += hero.lvl*3;
                        pbTrain.setVisibility(View.GONE);
                        tvTrn.setVisibility(View.GONE);
                        btnTrn.setClickable(true);
                        break;
                    case 4: hero.exp += hero.lvl*4;
                        pbTrain.setVisibility(View.GONE);
                        tvTrn.setVisibility(View.GONE);
                        btnTrn.setClickable(true);
                        break;
                    default: break;
                }
                updateScreen();
            }
        };
        calculateRegenHP(hero.time);
    }

    public void calculateRegenHP(long time){
        //TODO: check counts
        if (hero.hp<hero.maxHP) hero.hp += (time/60000)*hero.endurance;
        Log.d(LOG_TAG, "Hp regened: " + (time / 60000) * hero.endurance);
        if (hero.hp>=hero.maxHP) hero.hp = hero.maxHP;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.lvlUp) {
            Intent intent = new Intent(this, LevelUpActivity.class);
            intent.putExtra("str", hero.strength);
            intent.putExtra("agl", hero.agility);
            intent.putExtra("int", hero.intellect);
            intent.putExtra("end", hero.endurance);
            intent.putExtra("points", hero.statPoints);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        hero.saveHero();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        else if (resultCode == 1){
            hero.strength = data.getIntExtra("str",0);
            hero.agility = data.getIntExtra("agl",0);
            hero.intellect = data.getIntExtra("int",0);
            hero.endurance = data.getIntExtra("end",0);
            hero.statPoints = data.getIntExtra("points",0);}
        else if (resultCode == 2){
            //todo time training
            pbTrain.setVisibility(View.VISIBLE);
            tvTrn.setVisibility(View.VISIBLE);
            btnTrn.setClickable(false);
            timeTrain = data.getIntExtra("timeTrain",0);
            final int mode = data.getIntExtra("modeTrain",0);
            Log.d(LOG_TAG, "TIME: " + timeTrain);
            if (timeTrain!=0) {
                pbTrain.setMax(timeTrain);
                trnTimer = new Timer(false);
                trainTask = new TimerTask() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (timeTrain != 0) {
                            pbTrain.setProgress(i);
                            Log.d(LOG_TAG, "Progress: " + i);
                            i++;
                            timeTrain--;
                            mHandler.sendEmptyMessage(0);
                        } else {
                            mHandler.sendEmptyMessage(mode);
                            trnTimer.cancel();
                        }
                    }
                };
                trnTimer.schedule(trainTask, 200, 60000);
            }
        }
        updateScreen();
    }

    public void updateScreen(){
        hero.refreshHero();
        tvName.setText(hero.name);
        tvLvl.setText("Level: " + String.valueOf(hero.lvl));
        tvStr.setText(String.valueOf(hero.strength));
        tvAgl.setText(String.valueOf(hero.agility));
        tvInt.setText(String.valueOf(hero.intellect));
        tvEnd.setText(String.valueOf(hero.endurance));
        tvAttack.setText(String.valueOf(hero.attack));
        tvCh.setText(String.valueOf(hero.ch));
        tvAccur.setText(String.valueOf(hero.accuracy));
        tvDef.setText(String.valueOf(hero.defence));
        tvDd.setText(String.valueOf(hero.dodge));
        pbHP.setMax(hero.maxHP);
        pbExp.setMax(hero.maxExp);
        pbHP.setProgress(hero.hp);
        pbExp.setProgress(hero.exp);
        if (hero.statPoints > 0) tvStPoints.setText("+" + String.valueOf(hero.statPoints));
        else tvStPoints.setText("");
    }

    public void lvlUp(){
        if (hero.exp >= hero.maxExp){
            hero.exp -= hero.maxExp;
            hero.lvl++;
            hero.statPoints += 3;
            hero.maxExp = hero.lvl * 15;
            Toast.makeText(getApplicationContext(),"Level UP!!!",Toast.LENGTH_SHORT).show();
        }
    }


}
