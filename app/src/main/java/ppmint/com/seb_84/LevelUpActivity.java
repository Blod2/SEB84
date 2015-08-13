package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LevelUpActivity extends Activity {

    TextView tvStPoints;
    EditText etStr;
    EditText etAgl;
    EditText etInt;
    EditText etEnd;
    Button btnStrAdd;
    Button btnStrSub;
    Button btnAglAdd;
    Button btnAglSub;
    Button btnIntAdd;
    Button btnIntSub;
    Button btnEndAdd;
    Button btnEndSub;
    Button btnConfirm;
    private int str;
    private int agl;
    private int intel;
    private int end;
    private int stPoints;
    Intent stIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_up_activity);
        tvStPoints = (TextView) findViewById(R.id.tvStPoints);
        etStr = (EditText) findViewById(R.id.etStr);
        etAgl = (EditText) findViewById(R.id.etAgl);
        etInt = (EditText) findViewById(R.id.etInt);
        etEnd = (EditText) findViewById(R.id.etEnd);
        btnStrAdd = (Button) findViewById(R.id.btnStrAdd);
        btnStrSub = (Button) findViewById(R.id.btnStrSub);
        btnAglAdd = (Button) findViewById(R.id.btnAglAdd);
        btnAglSub = (Button) findViewById(R.id.btnAglSub);
        btnIntAdd = (Button) findViewById(R.id.btnIntAdd);
        btnIntSub = (Button) findViewById(R.id.btnIntSub);
        btnEndAdd = (Button) findViewById(R.id.btnEndAdd);
        btnEndSub = (Button) findViewById(R.id.btnEndSub);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        stIntent = getIntent();
        str = stIntent.getIntExtra("str", 0);
        agl = stIntent.getIntExtra("agl", 0);
        intel = stIntent.getIntExtra("int", 0);
        end = stIntent.getIntExtra("end",0);
        stPoints = stIntent.getIntExtra("points",0);

        tvStPoints.setText(String.valueOf(stPoints));
        etStr.setText(String.valueOf(str));
        etAgl.setText(String.valueOf(agl));
        etInt.setText(String.valueOf(intel));
        etEnd.setText(String.valueOf(end));

        btnStrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stPoints > 0) {
                    str++;
                    stPoints--;
                    etStr.setText(String.valueOf(str));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnStrSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((str > 0) && (str>0)) {
                    str--;
                    stPoints++;
                    etStr.setText(String.valueOf(str));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnAglAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stPoints > 0) {
                    agl++;
                    stPoints--;
                    etAgl.setText(String.valueOf(agl));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnAglSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((agl > 0)&& (agl>0)){
                    agl--;
                    stPoints++;
                    etAgl.setText(String.valueOf(agl));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnIntAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stPoints > 0) {
                    intel++;
                    stPoints--;
                    etInt.setText(String.valueOf(intel));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnIntSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((intel > 0)&& (intel>0)){
                    intel--;
                    stPoints++;
                    etInt.setText(String.valueOf(intel));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnEndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stPoints > 0) {
                    end++;
                    stPoints--;
                    etEnd.setText(String.valueOf(end));
                    tvStPoints.setText(String.valueOf(stPoints));
                }
            }
        });
        btnEndSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((end > 0)&& (end>0)){
                    end--;
                    stPoints++;
                    etEnd.setText(String.valueOf(end));
                    tvStPoints.setText(String.valueOf(stPoints));

                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stIntent.putExtra("str",str);
                stIntent.putExtra("agl",agl);
                stIntent.putExtra("int",intel);
                stIntent.putExtra("end",end);
                stIntent.putExtra("points",stPoints);
                setResult(RESULT_OK, stIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_level_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

