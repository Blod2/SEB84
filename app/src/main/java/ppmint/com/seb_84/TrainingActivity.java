package ppmint.com.seb_84;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class TrainingActivity extends Activity {

    RadioGroup rgTrain;
    RadioButton rbTrain30;
    RadioButton rbTrain60;
    RadioButton rbTrain120;
    RadioButton rbTrain240;
    Button btnConfTrain;
    Intent trainIntent;
    int lvl;
    int timetrain=0;
    int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        rgTrain = (RadioGroup) findViewById(R.id.rgTrain);
        rbTrain30 = (RadioButton) findViewById(R.id.rbTrain30);
        rbTrain60 = (RadioButton) findViewById(R.id.rbTrain60);
        rbTrain120 = (RadioButton) findViewById(R.id.rbTrain120);
        rbTrain240 = (RadioButton) findViewById(R.id.rbTrain240);
        btnConfTrain = (Button) findViewById(R.id.btnConfTrain);

        trainIntent = getIntent();
        lvl = trainIntent.getIntExtra("lvl", 0);

        btnConfTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go main
                if (timetrain>0) {
                    trainIntent.putExtra("timeTrain",timetrain);
                    trainIntent.putExtra("modeTrain",mode);
                    setResult(2, trainIntent);
                    finish();
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbTrain30:
                if (checked)
                    timetrain = 30;
                    mode = 1;
                    break;
            case R.id.rbTrain60:
                if (checked)
                    timetrain = 60;
                    mode = 2;
                    break;
            case R.id.rbTrain120:
                if (checked)
                    timetrain = 120;
                    mode = 3;
                break;
            case R.id.rbTrain240:
                if (checked)
                    timetrain = 240;
                    mode = 4;
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training, menu);
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
