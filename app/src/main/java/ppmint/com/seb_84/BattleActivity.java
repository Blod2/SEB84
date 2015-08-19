package ppmint.com.seb_84;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BattleActivity extends Activity {

    final String BATTLE_LOG = "Battle";
    private BattleManager mBattleManager = null;
    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter mBluetoothAdapter = null;

    Hero hero;
    SharedPreferences sharedPref;
    private int enemyAttack = 0;
    private int enemyDefence = 0;
    private int myTarget = 0;
    private int myBlock = 0;
    private int enemyTarget = 0;
    private int enemyBlock = 0;
    private boolean battleFlag = true;

    TextView tvMyHero;
    TextView tvEnemyHero;
    ImageView ivMyHero;
    ImageView ivEnemyHero;
    ProgressBar pbMyHero;
    ProgressBar pbEnemyHero;
    RadioButton rbAHead;
    RadioButton rbAChest;
    RadioButton rbALowerBack;
    RadioButton rbAFoot;
    RadioButton rbDHead;
    RadioButton rbDChest;
    RadioButton rbDLowerBack;
    RadioButton rbDFoot;
    Button btnToAttack;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        tvMyHero = (TextView) findViewById(R.id.tvMyHero);
        tvEnemyHero = (TextView) findViewById(R.id.tvEnemyHero);
        ivMyHero = (ImageView) findViewById(R.id.imgMyHero);
        ivEnemyHero = (ImageView) findViewById(R.id.imgEnemyHero);
        pbMyHero = (ProgressBar) findViewById(R.id.pbMyHero);
        pbEnemyHero = (ProgressBar) findViewById(R.id.pbEnemyHero);
        rbAHead = (RadioButton) findViewById(R.id.rbAHead);
        rbAChest = (RadioButton) findViewById(R.id.rbAChest);
        rbALowerBack = (RadioButton) findViewById(R.id.rbALow);
        rbAFoot = (RadioButton) findViewById(R.id.rbAFoot);
        rbDHead = (RadioButton) findViewById(R.id.rbDHead);
        rbDChest = (RadioButton) findViewById(R.id.rbDChest);
        rbDLowerBack = (RadioButton) findViewById(R.id.rbDLow);
        rbDFoot = (RadioButton) findViewById(R.id.rbDFoot);
        btnToAttack = (Button) findViewById(R.id.btnToAttack);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key_name), Context.MODE_PRIVATE);
        hero = new Hero(sharedPref,"unnamed",this);
        tvMyHero.setText(hero.name);
        pbMyHero.setMax(hero.maxHP);
        pbMyHero.setProgress(hero.hp);
        setupChat();
        intent = getIntent();


    }

    public void onDestroy() {
        super.onDestroy();
        if (mBattleManager != null) {
            Log.d(BATTLE_LOG,"Stopping chat service");
            mBattleManager.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBattleManager != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBattleManager.getState() == BattleManager.STATE_NONE) {
                // Start the Bluetooth chat services
                Log.d(BATTLE_LOG,"starting battle service");
                mBattleManager.start();
                String deviceadr = intent.getStringExtra("device_address");
                if (!deviceadr.equals("server")) {
                    connectDevice(intent);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battle, menu);
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
    private void setupChat() {
        Log.d(BATTLE_LOG, "setupChat()");
        // Initialize the send button with a listener that for click events
        btnToAttack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                if (battleFlag) {
                    sendMsg(attackPackage());
                    btnToAttack.setClickable(false);
                    battleFlag = false;
                }
                else{
                    int enemyDamage = enemyAttack;
                    int myDamage = hero.attack;
                    if (myBlock==enemyTarget) enemyDamage = 0;
                    hero.hp -= enemyDamage;
                    if (myTarget==enemyBlock) myDamage = 0;
                    pbEnemyHero.setProgress(pbEnemyHero.getProgress()-myDamage);
                    sendMsg(defencePackage(pbEnemyHero.getProgress() - myDamage, hero.hp));
                    battleFlag = true;
                }
            }
        });
        mBattleManager = new BattleManager(getApplicationContext(), mHandler);
        mOutStringBuffer =  new StringBuffer("");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbAHead:
                if (checked)
                    myTarget = 1;
                break;
            case R.id.rbAChest:
                if (checked)
                    myTarget = 2;
                break;
            case R.id.rbALow:
                if (checked)
                    myTarget = 3;
                break;
            case R.id.rbAFoot:
                if (checked)
                    myTarget = 4;
                break;
            case R.id.rbDHead:
                if (checked)
                    myBlock = 1;
                break;
            case R.id.rbDChest:
                if (checked)
                    myBlock = 2;
                break;
            case R.id.rbDLow:
                if (checked)
                    myBlock = 3;
                break;
            case R.id.rbDFoot:
                if (checked)
                    myBlock = 4;
                break;
        }
    }

    private void sendMsg(String message) {
        // Check that we're actually connected before trying anything
        if (mBattleManager.getState() != BattleManager.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBattleManager.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //editText.setText(mOutStringBuffer);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                   // editText.setText(writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //tvEnemyHero.setText(readMessage);
                    parseMessage(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    String deviceadr = intent.getStringExtra("device_address");
                    if (!deviceadr.equals("server")){
                        sendMsg(constructFirstMessage());
                    }
                    break;
            }
        }
    };


    private void connectDevice(Intent data) {
        // Get the device MAC address
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String address = data.getExtras().getString("device_address");
        if (address.equals(null)) return;
        Log.d(BATTLE_LOG,"Connecting to device:"+address);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBattleManager.connect(device);
    }

    private String constructFirstMessage(){
        return "F:"+hero.name+":"+hero.maxHP+":"+hero.hp+":"+hero.attack+":"+hero.defence;
    }

    boolean firstMes = true;
    private void parseMessage(String mess){
        //TODO: parsing messages
        Pattern xx = Pattern.compile("[AFD]:([^*$]*)");
        Matcher m = xx.matcher(mess);
        if (m.matches()){
            TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
            splitter.setString(mess);
            String head = splitter.next();
            if (head.equals("F")){
            tvEnemyHero.setText(splitter.next());
            pbEnemyHero.setMax(Integer.valueOf(splitter.next()));
            pbEnemyHero.setProgress(Integer.valueOf(splitter.next()));
            enemyAttack = Integer.valueOf(splitter.next());
            enemyDefence = Integer.valueOf(splitter.next());
                if (firstMes){
                    String message = constructFirstMessage();
                    if (message != null) sendMsg(message);
                    firstMes = false;
                }
            }
            if (head.equals("A")){
                enemyTarget = Integer.valueOf(splitter.next());
                enemyBlock = Integer.valueOf(splitter.next());
                battleFlag = false;
            }
            if (head.equals("D")){
                pbEnemyHero.setProgress(Integer.valueOf(splitter.next()));
                hero.hp = Integer.valueOf(splitter.next());
                pbMyHero.setProgress(hero.hp);
                btnToAttack.setClickable(true);
            }
        }
    }

    private String attackPackage(){
        return "A:" + myTarget+ ":" + myBlock;
    }

    private String defencePackage(int enHP, int myHP){
        return "D:"+enHP+":"+myHP;
    }

}
