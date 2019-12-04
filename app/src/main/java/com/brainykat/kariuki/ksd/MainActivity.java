package com.brainykat.kariuki.ksd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.GlobalDef;
import com.basewin.log.LogUtil;
import com.basewin.models.PrintLine;
import com.basewin.models.TextPrintLine;
import com.basewin.services.ServiceManager;
import com.basewin.utils.StringUtil;
import com.brainykat.kariuki.ksd.Retrofits.APIClient;
import com.brainykat.kariuki.ksd.Retrofits.ApiService;
import com.brainykat.kariuki.ksd.Retrofits.Pojos.Meal;
import com.brainykat.kariuki.ksd.Retrofits.Pojos.Student;
import com.pos.sdk.cardreader.PosCardReaderInfo;
import com.pos.sdk.cardreader.PosCardReaderManager;
import com.pos.sdk.cardreader.PosMifareCardReader;
import com.pos.sdk.utils.PosByteArray;
import com.pos.sdk.utils.PosUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView Mess,CardNo, Name, Admis,Grade;
    Spinner MealType;
    Button Place;
    ApiService apiService;
    String studentId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ServiceManager.getInstence().init(this);
        LogUtil.openLog();
        apiService = APIClient.getClient().create(ApiService.class);
        Mess = findViewById(R.id.message);
        Name = findViewById(R.id.name);
        Admis = findViewById(R.id.regnum);
        Grade = findViewById(R.id.grade);
        MealType = findViewById(R.id.spinner_MealType);
        Place = findViewById(R.id.order);
        Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PrintOrder(1);
                ReadCard();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void PrintOrder(Integer Id){
        try {
            TextPrintLine spacePrintLine = new TextPrintLine();
            spacePrintLine.setContent("\n");
            spacePrintLine.setSize(TextPrintLine.FONT_LARGE);

            //first step clean print cache
            ServiceManager.getInstence().getPrinter().cleanCache();
            //set print grayscale
            ServiceManager.getInstence().getPrinter().setPrintGray(2000);
            ServiceManager.getInstence().getPrinter().setPrintTypesettingType(GlobalDef.PRINTERLAYOUT_TYPESETTING);
            //second step set print data
            //add text data
            TextPrintLine header = new TextPrintLine();
            header.setType(PrintLine.TEXT);
            header.setSize(TextPrintLine.FONT_LARGE);
            header.setPosition(PrintLine.CENTER);
            header.setBold(true);
            header.setContent("Basiletu School");

            ServiceManager.getInstence().getPrinter().addPrintLine(header);
            TextPrintLine header2 = new TextPrintLine();
            header2.setType(PrintLine.TEXT);
            header.setSize(TextPrintLine.FONT_NORMAL);
            header2.setPosition(PrintLine.CENTER);
            header2.setBold(true);
            header2.setItalic(true);
            header2.setContent("brilliantly awesome");
            ServiceManager.getInstence().getPrinter().addPrintLine(header2);
            TextPrintLine headerLine = new TextPrintLine();
            headerLine.setContent("_____________________________");
            ServiceManager.getInstence().getPrinter().addPrintLine(headerLine);
            TextPrintLine textPrintLine = new TextPrintLine();
            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setSize(TextPrintLine.FONT_LARGE);
            textPrintLine.setPosition(PrintLine.CENTER);
            textPrintLine.setContent("Order placing Receipt : " + Id);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            ServiceManager.getInstence().getPrinter().addPrintLine(headerLine);
//add text data
            TextPrintLine textPrintLine2 = new TextPrintLine();
            textPrintLine2.setType(PrintLine.TEXT);
            textPrintLine2.setSize(TextPrintLine.FONT_NORMAL);
            textPrintLine2.setPosition(PrintLine.LEFT);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss ");
            String currentDateandTime = sdf.format(new Date());
            textPrintLine2.setContent("Date : " + currentDateandTime);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine2);

            TextPrintLine textPrintLine32 = new TextPrintLine();
            textPrintLine32.setType(PrintLine.TEXT);
            textPrintLine32.setSize(TextPrintLine.FONT_LARGE);
            textPrintLine32.setPosition(PrintLine.LEFT);

            int g = MealType.getSelectedItemPosition();
            if(g == 0 ){
                textPrintLine32.setContent("Meal type :\t\t Breakfast" );
            }
            else if(g == 1 ){
                textPrintLine32.setContent("Meal type :\t\t Lunch" );
            }
            else if(g == 2 ){
                textPrintLine32.setContent("Meal type :\t\t Supper" );
            }else{
                    textPrintLine32.setContent("Meal type :\t\t Other" );
            }
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine32);

            TextPrintLine textPrintLine6 = new TextPrintLine();
            textPrintLine6.setType(PrintLine.TEXT);
            textPrintLine6.setSize(TextPrintLine.FONT_NORMAL);
            textPrintLine6.setPosition(PrintLine.LEFT);
            textPrintLine6.setContent("Student " + Name.getText().toString().trim());
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine6);;
            ServiceManager.getInstence().getPrinter().addPrintLine(headerLine);
//add four blank lines
            TextPrintLine spacePrintLinee = new TextPrintLine();
            spacePrintLinee.setContent("\n\n");
            spacePrintLinee.setSize(TextPrintLine.FONT_LARGE);
            ServiceManager.getInstence().getPrinter().addPrintLine(spacePrintLinee);


            ServiceManager.getInstence().getPrinter().beginPrint(new OnPrinterListener() {
                @Override
                public void onError(int i, String s) {
                    LogUtil.i(getClass(),"print error!" + s);
                }
                @Override
                public void onFinish() {
                    LogUtil.i(getClass(),"print finish!");
                }
                @Override
                public void onStart() {
                    LogUtil.i(getClass(),"print start!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  PosMifareCardReader InitializeCardReader(){
        //Initialize
        PosMifareCardReader mifareCardReader=null;
        mifareCardReader = PosCardReaderManager.getDefault(this).getMifareCardReader();
        //Open
        int ret0 = mifareCardReader.open(PosMifareCardReader.CARD_TYPE_MIFARE_CLASSIC);
//        if(ret == 0){
//            snackThem("Opened");
//        }else{
//            snackThemRed("Failed ");
//        }
        int ret2 = mifareCardReader.detect();
        if(ret2 == 0){

            Log.e("Thuku","Detected");
        }else{
            Log.e("Thuku","Not Detected ");
        }
        //PosCardReaderInfo info=mifareCardReader.getCardReaderInfo();
        //Log.e("Thuku",info.toString());
        //PosCardReaderInfo:mCategory= 16, mCardType= 2, mCardChannel= 0, mSerialNum= 065d2135, mAttribute= 41020400420108
        //PosCardReaderInfo:mCategory= 16, mCardType= 2, mCardChannel= 0, mSerialNum= 66fe3a35, mAttribute= 41020400420108
        int ret = mifareCardReader.auth('A', 0, PosUtils.hexStringToBytes("FFFFFFFFFFFF"), null);
        if(ret == 0){
            Log.e("Thuku","Authorized");
                    //readSasa(mifareCardReader);
        }else{
            Log.e("Thuku","Failed Auth");
        }
        //readSasa(mifareCardReader);
        return mifareCardReader;
    }
    public  void ReadCard(){
        PosMifareCardReader mifareCardReader = InitializeCardReader();
        PosCardReaderInfo info = mifareCardReader.getCardReaderInfo();
        Log.e("Thuku",info.toString());
        if(info == null){
            snackThemRed("Blank Card");
        }
        else {
            String n = PosUtils.bytesToHexString( info.mSerialNum);

            if(TextUtils.isEmpty(n)){
                snackThemRed("Invalid Card");
            }else{
                Log.e("Thuku",n);
                Mess.setText(n);
                Call<Student> call = apiService.GetStudent(n);
                call.enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        Student res = response.body();
                        if(res != null) {
                            Name.setText("Name : " + res.getName().getFullName());
                            Admis.setText("Reg : " +res.getAdmissionNumber());
                            Grade.setText("Grade : " +res.getGrade());
                            studentId = res.getId();
                        }else{
                            snackThemRed("Invalid student card");
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        snackThemRed("Error occurred");
                        Log.e("Thuku",t.getMessage());
                    }
                });
            }
        }
    }
    public void placeOrder(){
        Meal meal = new Meal();
        meal.setMealType(MealType.getSelectedItemPosition());
        meal.setPOSId(Long.valueOf( getPhoneImei()));
        Call<com.brainykat.kariuki.ksd.Retrofits.Pojos.Response> call = apiService.PlaceOrder(studentId,meal);
        call.enqueue(new Callback<com.brainykat.kariuki.ksd.Retrofits.Pojos.Response>() {
            @Override
            public void onResponse(Call<com.brainykat.kariuki.ksd.Retrofits.Pojos.Response> call, Response<com.brainykat.kariuki.ksd.Retrofits.Pojos.Response> response) {
                com.brainykat.kariuki.ksd.Retrofits.Pojos.Response res = response.body();
                if(res != null){
                    if(res.getStatus()){
                        PrintOrder(1);
                        Name.setText("");Admis.setText("");Grade.setText("");Mess.setText("Tap card to get details");
                        snackThem("Order placed");

                    }else{
                        snackThemRed("Operation failed");
                    }
                }else{
                    snackThemRed("Invalid Response");
                }

            }

            @Override
            public void onFailure(Call<com.brainykat.kariuki.ksd.Retrofits.Pojos.Response> call, Throwable t) {
                snackThemRed("Error occurred");
            }
        });
    }
    public  void Write(){
        PosMifareCardReader mifareCardReader = InitializeCardReader();
        String str="Kariuki Margaret";
        byte[] array = str.getBytes();
        Log.e("Thuku", PosUtils.bytesToHexString(array));
        Log.e("Thuku", "byte length = " + String.valueOf(array.length) );
        int ret = mifareCardReader.write(6,PosUtils.hexStringToBytes(PosUtils.bytesToHexString(array)));
        if(ret == 0){
            snackThem("Written");
        }else {
            snackThemRed("write fail " + String.valueOf(ret)); //62527
        }
    }
    public void readSasa(PosMifareCardReader mifareCardReader){
        byte[] data;
        PosByteArray rspBuf = new PosByteArray();
        PosCardReaderInfo info=mifareCardReader.getCardReaderInfo();
        int secCount =info.mCategory;
        int bCount = 0;
        int bIndex = 0;
        String[] infoData = new String[secCount];
        int n = mifareCardReader.read(6,rspBuf);
        Log.e("Thuku", rspBuf.toString());
        String yy = PosUtils.bytesToHexString(rspBuf.buffer);
        Log.e("Thuku", yy);
        String xx = PosUtils.bytesToAscii(rspBuf.buffer);
        Log.e("Thuku", xx);
        /*for(int j = 0; j < secCount; j++){
            // 6.1) authenticate the sector
                //Log.i(TAG, "Authentication Success " + String.valueOf(j));
                // 6.2) In each sector - get the block count
                //bCount = mfc.getBlockCountInSector(j);
                bIndex = 0;
                final StringBuilder ms = new StringBuilder();
                for(int i = 0; i < 4; i++){
                    //bIndex = mfc.sectorToBlock(j);
                    int y = mifareCardReader.read(i,rspBuf); //.readBlock(bIndex);
                    //ms.append(rspBuf.toString());
                    ms.append(bytesToHex(rspBuf.buffer));
                    Log.e("Thuku", rspBuf.toString());
                    bIndex++;
                }
                Log.e("Thuku", HEXToString(ms.toString()));
            infoData[j] =HEXToString(ms.toString());

        }*/
        //return infoData;
    }
    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String HEXToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
    private void snackThem(String msg) {
        View v = findViewById(R.id.cordinator);
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.GREEN);
        snackbar.show();
    }
    private void snackThemRed(String msg) {
        View v = findViewById(R.id.cordinator);
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }
    private String getPhoneImei() {
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        return mngr.getDeviceId();
    }
}
