package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ArmoryActivity extends Activity {
    Hero hero;
    SharedPreferences sharedPref;
    Button btnSave;
    ImageView ivAvatar,ivHpPotion;
    ProgressBar pbHerohp;
    private Integer[] avIds = {R.drawable.thief,R.drawable.witch,R.drawable.def};
    Gallery gallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armory);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key_name), Context.MODE_PRIVATE);
        hero = new Hero(sharedPref,"sasiska",getApplicationContext());
        btnSave = (Button)findViewById(R.id.btnSave);
        pbHerohp = (ProgressBar)findViewById(R.id.pbHerohp);
        pbHerohp.setMax(hero.maxHP);
        pbHerohp.setProgress(hero.hp);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivHpPotion = (ImageView)findViewById(R.id.ivHpPotion);
        ivAvatar.setImageResource(hero.avatar);
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hero.avatar = avIds[position];
                ivAvatar.setImageResource(avIds[position]);
                gallery.setVisibility(View.GONE);
            }
        });
        if (hero.hpPotions>0) ivHpPotion.setVisibility(View.VISIBLE);
    }

    public void onChangeAvatarClick(View view){
        gallery.setVisibility(View.VISIBLE);
    }

    public void onExitClick(View view){
        Intent intent = new Intent();
        intent.putExtra("resId",hero.avatar);
        intent.putExtra("heroHP",hero.hp);
        intent.putExtra("potionHP",hero.hpPotions);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void useHpPotion(View view){
        if (hero.hpPotions>0){
            if (hero.hp<hero.maxHP) {
                hero.hp += 50;
                if (hero.hp > hero.maxHP) hero.hp = hero.maxHP;
                hero.hpPotions--;
                pbHerohp.setProgress(hero.hp);
                if (hero.hpPotions==0) ivHpPotion.setVisibility(View.GONE);
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {

        private int mGalleryItemBackground;
        private Context mContext;
        private final Integer[] mImage = {R.drawable.thief,R.drawable.witch,R.drawable.def};

        public ImageAdapter(Context сontext) {
            mContext = сontext;
        }

        @Override
        public int getCount() {
            return mImage.length;
        }

        @Override
        public Object getItem(int position) {
            return mImage[position];
        }

        @Override
        public long getItemId(int position) {
            return mImage[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view = new ImageView(mContext);
            view.setImageResource(mImage[position]);
            view.setPadding(20, 20, 20, 20);
            view.setLayoutParams(new Gallery.LayoutParams(250, 250));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setBackgroundResource(mGalleryItemBackground);

            return view;
        }
    }

}
