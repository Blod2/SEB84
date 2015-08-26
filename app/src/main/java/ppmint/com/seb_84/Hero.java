package ppmint.com.seb_84;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static java.lang.System.currentTimeMillis;


public class Hero {

    public String name;
    public int lvl;
    public int hp;
    public int maxHP;
    public int exp;
    public int maxExp;
    public int strength;
    public int agility;
    public int intellect;
    public int endurance;
    public int attack;
    public int ch;
    public int accuracy;
    public int defence;
    public int dodge;
    public int statPoints;
    public long time;
    public int timeTrain;
    public int mode;

    SharedPreferences sharedPref;
    Context context;

    public Hero(SharedPreferences sp,String hname,Context ccc){
        sharedPref = sp;
        context = ccc;
        name = sharedPref.getString(context.getString(R.string.preference_hero_name_key), hname);
        lvl = sharedPref.getInt(context.getString(R.string.preference_hero_lvl_key), 1);
        hp = sharedPref.getInt(context.getString(R.string.preference_hero_HP_key), 10);
        exp = sharedPref.getInt(context.getString(R.string.preference_hero_Exp_key), 0);
        strength = sharedPref.getInt(context.getString(R.string.preference_hero_str_key), 1);
        agility = sharedPref.getInt(context.getString(R.string.preference_hero_Agl_key), 1);
        intellect = sharedPref.getInt(context.getString(R.string.preference_hero_Int_key), 1);
        endurance = sharedPref.getInt(context.getString(R.string.preference_hero_End_key),1);
        statPoints = sharedPref.getInt(context.getString(R.string.preference_hero_stPt_key),3);
        timeTrain = sharedPref.getInt(context.getString(R.string.preference_hero_train_time),0);
        time = currentTimeMillis() - sharedPref.getLong(context.getString(R.string.preference_exit_time),currentTimeMillis());
        mode = sharedPref.getInt(context.getString(R.string.preference_hero_train_mode),0);
        refreshHero();
    }

    public void saveHero(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_hero_name_key),name);
        editor.putInt(context.getString(R.string.preference_hero_lvl_key), lvl);
        editor.putInt(context.getString(R.string.preference_hero_str_key), strength);
        editor.putInt(context.getString(R.string.preference_hero_Agl_key), agility);
        editor.putInt(context.getString(R.string.preference_hero_Int_key), intellect);
        editor.putInt(context.getString(R.string.preference_hero_End_key), endurance);
        editor.putInt(context.getString(R.string.preference_hero_HP_key), hp);
        editor.putInt(context.getString(R.string.preference_hero_stPt_key),statPoints);
        editor.putInt(context.getString(R.string.preference_hero_Exp_key), exp);
        editor.putLong(context.getString(R.string.preference_exit_time), currentTimeMillis());
        editor.putInt(context.getString(R.string.preference_hero_train_time), timeTrain);
        editor.putInt(context.getString(R.string.preference_hero_train_mode), mode);
        editor.commit();
    }

    public void refreshHero(){
        //refreshing only countable stats
        attack = strength;
        defence = endurance;
        if (ch >= 50) ch = 50;
        else
            ch = Math.round(agility / 4);
        if (accuracy >= 50) accuracy = 50;
        else
            accuracy = Math.round(agility / 6 + intellect / 3);
        if (dodge >= 50) dodge = 50;
        else
            dodge = Math.round(agility / 3 + intellect / 6);
        maxHP = strength * 5 + endurance * 5;
        maxExp = lastExp(lvl);
    }

    public int lastExp(int x){
        if(x == 1) return 10;
        return lastExp(x-1) + x * 10;
    }
}
