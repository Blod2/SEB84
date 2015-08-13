package ppmint.com.seb_84;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Таня on 12.08.2015.
 */
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
    public float ch;
    public float accuracy;
    public int defence;
    public float dodge;
    public int statPoints;
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
        attack = strength;
        ch = (float)agility / 10;
        accuracy = (float)agility / 20 + (float)intellect / 10;
        defence = endurance;
        dodge = (float)agility / 10 + (float)intellect / 20;
        maxHP = strength * 5 + endurance * 5;
        maxExp = lvl * 15;
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
        editor.commit();
    }

    public void refreshHero(){
        //refreshing only countable stats
        attack = strength;
        ch = (float)agility / 10;
        accuracy = (float)agility / 20 + (float)intellect / 10;
        defence = endurance;
        dodge = (float)agility / 10 + (float)intellect / 20;
        maxHP = strength * 5 + endurance * 5;
        maxExp = lvl * 15;
    }
}
