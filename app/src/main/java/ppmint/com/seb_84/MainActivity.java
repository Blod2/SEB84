package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
    ProgressBar pbHP;
    ProgressBar pbExp;
    Button btnTrn;
    Button btnBtl;
    Hero hero;

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
                hero.exp += 10;
                lvlUp();
                updateScreen();
            }
        });
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
            return true;
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

    public void updateScreen(){
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
