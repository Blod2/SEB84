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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.currentTimeMillis;


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
    Timer regenTimer;
    Handler mHandler;
    Timer trnTimer;
    ImageView ivAvatar;


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
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);


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
                updateScreen();
            }
        });
        btnBtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pbHP.getProgress() > hero.maxHP/2){
                    Intent in = new Intent(getApplicationContext(), BTConnect.class);
                    startActivityForResult(in, 2);
                }
                else {
                    Toast.makeText(getApplicationContext(),"You HP < 50%. Іди броди...",Toast.LENGTH_LONG).show();
                }
            }
        });
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
        calculateTrainingProgress(hero.time);
    }

    public void calculateRegenHP(long time){
        if (hero.hp<hero.maxHP) hero.hp += (time/60000)*hero.endurance;
        Log.d(LOG_TAG, "Hp restored: " + (time / 60000) * hero.endurance);
        if (hero.hp>=hero.maxHP) hero.hp = hero.maxHP;
    }

    public void calculateTrainingProgress(long time){
        if (hero.timeTrain>0) {
            if (hero.timeTrain < (time / 60000)) {
                hero.timeTrain = 0;
                mHandler.sendEmptyMessage(hero.mode);
                addHPpotion();
            }
            if (hero.timeTrain > (time / 60000)) {
                hero.timeTrain -= time / 60000;
                trnTimer = new Timer(true);
                pbTrain.setVisibility(View.VISIBLE);
                tvTrn.setVisibility(View.VISIBLE);
                btnTrn.setClickable(false);
                trnTimer.purge();
                trnTimer.schedule(new TrainTask(), 200, 60000);

            }
        }
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
        if (id == R.id.armory) {
            hero.saveHero();
            startActivityForResult(new Intent(getApplicationContext(), ArmoryActivity.class), 5);
        }
        if (id==R.id.botfight) {
            if (pbHP.getProgress() > hero.maxHP/2){
                Intent in = new Intent(getApplicationContext(), BattleVSBotActivity.class);
                startActivityForResult(in, 3);
            }
            else {
                Toast.makeText(getApplicationContext(),"You HP < 50%. Іди броди...",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (regenTimer!=null) regenTimer.cancel();
        regenTimer = new Timer(true);
        regenTimer.schedule(new RegenTask(), 60000, 60000);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (trnTimer!=null)trnTimer.cancel();
        hero.saveHero();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        else if (requestCode == 1) {
            if (resultCode == 1) {
                hero.strength = data.getIntExtra("str", 0);
                hero.agility = data.getIntExtra("agl", 0);
                hero.intellect = data.getIntExtra("int", 0);
                hero.endurance = data.getIntExtra("end", 0);
                hero.statPoints = data.getIntExtra("points", 0);
            } else if (resultCode == 2) {
                pbTrain.setVisibility(View.VISIBLE);
                tvTrn.setVisibility(View.VISIBLE);
                btnTrn.setClickable(false);
                hero.timeTrain = data.getIntExtra("timeTrain", 0);
                hero.mode = data.getIntExtra("modeTrain", 0);
                Log.d(LOG_TAG, "TIME: " + hero.timeTrain);
                if (hero.timeTrain != 0) {
                    trnTimer = new Timer(true);
                    trnTimer.purge();
                    trnTimer.schedule(new TrainTask(), 200, 60000);
                }
            }
        }
        if (requestCode==2){
            String device = data.getStringExtra("device_address");
            Intent btlint = new Intent(getApplicationContext(),BattleActivity.class);
            btlint.putExtra("device_address",device);
            if (device.equals("")) return;
            startActivityForResult(btlint,3);
            Toast.makeText(getApplicationContext(),device,Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 3){
            if (resultCode == 3){
            hero.hp = data.getIntExtra("myHP",10);
            addHPpotion();
            if (hero.hp <= 0){ Toast.makeText(getApplicationContext(),getString(R.string.toast_loose),Toast.LENGTH_SHORT).show();
                hero.exp+=hero.lvl;}
            else{ Toast.makeText(getApplicationContext(),getString(R.string.toast_win),Toast.LENGTH_SHORT).show();
            hero.exp+=hero.lvl*3;}}
            if (resultCode == 4) {
                Toast.makeText(getApplicationContext(),getString(R.string.toast_discon),Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 5){
            if (resultCode==RESULT_OK) {
                hero.avatar = data.getIntExtra("resId", R.drawable.witch);
                hero.hp = data.getIntExtra("heroHP",hero.hp);
                hero.hpPotions = data.getIntExtra("potionHP",0);
            }
        }
        updateScreen();
    }

    public void addHPpotion(){
        int rollNum = roll(100);
        if (rollNum>=0&&rollNum<=10){
            hero.hpPotions++;
            Toast.makeText(getApplicationContext(),getString(R.string.toast_hppotion_found),Toast.LENGTH_SHORT).show();
        }
    }

    private int roll(int diapason){
        Random rand = new Random(currentTimeMillis());
        return rand.nextInt(diapason);
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
        ivAvatar.setImageResource(hero.avatar);
        lvlUp();
        if (hero.statPoints > 0) tvStPoints.setText("+" + String.valueOf(hero.statPoints));
        else tvStPoints.setText("");
    }

    public void lvlUp(){
        if (hero.lvl == Constants.MAXIMAL_LEVEL){
            hero.lvl = Constants.MAXIMAL_LEVEL;
            Toast.makeText(getApplicationContext(),"You have reached maximal level! You rulezz!",Toast.LENGTH_SHORT).show();
        }
        else
            if (hero.exp >= hero.maxExp){
            hero.exp -= hero.maxExp;
            hero.lvl++;
            hero.statPoints += 3;
            hero.maxExp = hero.lastExp(hero.lvl);
            Toast.makeText(getApplicationContext(),"Level UP!!!",Toast.LENGTH_SHORT).show();

            }
    }

    int getMaxTime(){
        int maxTime = 0;
        switch(hero.mode){
            case 1:maxTime = 30; break;
            case 2:maxTime = 60; break;
            case 3:maxTime = 120; break;
            case 4:maxTime = 240; break;
        }
        pbTrain.setMax(maxTime);
      return maxTime;
    }

    public class TrainTask extends TimerTask{
        int i = getMaxTime() - hero.timeTrain;

        @Override
        public void run() {
            if (hero.timeTrain != 0) {
                pbTrain.setProgress(i);
                Log.d(LOG_TAG, "Progress: " + i);
                i++;
                hero.timeTrain--;
                mHandler.sendEmptyMessage(0);
            }
            else {
                mHandler.sendEmptyMessage(hero.mode);
                if (trnTimer!=null){
                    trnTimer.purge();
                    trnTimer.cancel();
                    addHPpotion();
                }
            }
        }
    }

    public class RegenTask extends TimerTask{
        @Override
        public void run() {
            if(hero.hp >= hero.maxHP){
                hero.hp = hero.maxHP;
                regenTimer.cancel();
            }
            else
                hero.hp += hero.endurance;
            mHandler.sendEmptyMessage(0);
        }
    }

}
