package com.project.businesscardexchange.yan.businesscardexchange;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.project.businesscardexchange.yan.businesscardexchange.fragments.ProfileActivity;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCardRealm;
import com.project.businesscardexchange.yan.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.yan.businesscardexchange.ui.WifiFilehelper;
import com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.ReceiveActivity;
import com.project.businesscardexchange.yan.businesscardexchange.utils.DBHelper;
import com.project.businesscardexchange.yan.businesscardexchange.utils.DeviceUtils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //for navigation drawer

    String encodedImageString;
    KenBurnsView imageView;
    ExpandableMenuOverlay menuOverlay;

    public static final String IS_KEY_SET = "isKeySet";
    public static final String PW_LOGIN = "passwordLogin";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NAME = "nameKey";
    public static final String COMPANY_NAME = "companyNameKey";
    public static final String EMAIL_ADDRESS = "emailKey";
    public static final String PHONE = "phoneKey";
    public static final String WEBSITE_URL = "websiteUrlKey";
    public static final String PHONE_DIRECT_LINE = "directLineKey";
    public static final String TIMESTAMP = "timestamp";
    public static final String PHOTO_COMPANY_LOGO = "photocompanylogo";
    public static final String PHOTO = "PHOTO";
    public static final String POST="postKey";
    public static final String STREET = "streetKey";
    public static final String CITY = "cityKey";
    public static final String STATE = "stateKey";
    public static final String ZIPCODE = "zipCodeKey";
    public static final String COUNTRY_NAME = "country_name";

    SharedPreferences prefs;
    Gson gson;
 //   Realm myRealm;
DBHelper myDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //myRealm = MyApplication.getRealmInstance(getApplicationContext());
        myDbHelper = DBHelper.getInstance(getApplicationContext());
        //setuping navigation drawer background

      //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        ShimmerTextView shimmerTextView = (ShimmerTextView)findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        shimmer.setDuration(4000);

        //Below line is code to save DB of sqlite when app is opened to sd card;
        DeviceUtils.exportDB();



        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {

                switch (action) {
                    case MID:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:
                        startActivity(new Intent(MainActivity.this, MyCardActivity.class));
                        break;
                    case RIGHT:
                        startActivity(new Intent(MainActivity.this, CardsActivity.class));
                        break;
                }
            }
        });

        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(menuOverlay)
                .setTitle("Menu Button")
                .setDescription("Click here to open your menus")
                .setDelay(1000)
                .displayOneTime(1)
                .setBackgroundColor(R.color.colorAccent)
                .setBackgroundAlpha(80)
                .setCloseButtonColor(R.color.colorAccent)
                .setButtonText("CLICK HERE IF YOU GOT IT!")
                .build();
       showtips.setDisplayOneTimeID(1);
        showtips.show(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        imageView = (KenBurnsView)hView.findViewById(R.id.image_navigation_drawer);


        //.........

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        //Check if pwd is set or not?
        if(prefs.getBoolean(IS_KEY_SET,false))
        {
            //Show login Dialog
            showLoginDialog();
        }
        else
        {
            //show set password Dialog
            showSetPwDialog();
        }
        //setting up navigation drawer background

        if(prefs.getString(NAME,null)!= null){
        TextView textView = (TextView)hView.findViewById(R.id.text_navigation_drawer);
        textView.setText("Hi, "+ prefs.getString(NAME,"you") + " !!");
          //  new MyAsyncTask().execute(prefs.getString("PHOTO","Not available"));
    }
    gson = new Gson();

        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
//           finish();
//            return;
//        }
        //set the navigation header profile with name and email address from the shared preference
        //trying we can delelte this

        //This is receiving part of NFC ; this will called when NFC beam clicked from sender; It is defined in Manifest file
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.e("BYTE_CHECK", "extractPayload; STARTED" );

            extractPayload(getIntent());
            Log.e("BYTE_CHECK", "extractPayload; ENDED");

        }


    }

    private void showSetPwDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.set_pw_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userPw = (EditText) promptsView.findViewById(R.id.txt_password);
        final EditText confirmPw = (EditText) promptsView.findViewById(R.id.txt_confirm_pw);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Create Password", null) //Set to null. We override the onclick
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                                MainActivity.this.finish();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                  //      d.dismiss();
                        if(userPw.getText().toString().trim() == "")
                        {
                            Toast.makeText(getApplicationContext(),"Password Field Required ",Toast.LENGTH_SHORT).show();
                        }
                        else if(confirmPw.getText().toString().trim() == "")
                        {
                            Toast.makeText(getApplicationContext(),"Confirm Password Field Required ",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            if(!userPw.getText().toString().trim().equals(confirmPw.getText().toString().trim()))
                            {
                                Toast.makeText(getApplicationContext(),"Passwords Does not match ",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(PW_LOGIN, userPw.getText().toString().trim());
                                editor.putBoolean(IS_KEY_SET, true);
                                editor.commit();
                                alertDialog.dismiss();
                            }
                        }

                    }
                });
            }
        });

        // show it
        alertDialog.show();

    }

    private void showLoginDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.login_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userPw = (EditText) promptsView.findViewById(R.id.txt_password);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Login", null) //Set to null. We override the onclick
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                                MainActivity.this.finish();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        //      d.dismiss();
                        if(userPw.getText().toString().trim() == "")
                        {
                            Toast.makeText(getApplicationContext(),"Password Field Required ",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                           // Log.e("PW_CHECK",prefs.getString(PW_LOGIN,""));
                            if(!userPw.getText().toString().trim().equals(prefs.getString(PW_LOGIN,"")))
                            {
                                Toast.makeText(getApplicationContext(),"Passwords Does not match ",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Successfully Logged In.",Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }

                    }
                });
            }
        });

        // show it
        alertDialog.show();

    }

    NfcAdapter nfcAdapter;
    @Override
    protected void onResume() {
        super.onResume();
       // set Ndef message to send by beam
       // NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        assert nfcAdapter != null;
        try
        {
            nfcAdapter.setNdefPushMessageCallback(
                    new NfcAdapter.CreateNdefMessageCallback() {
                        public NdefMessage createNdefMessage(NfcEvent event) {
                            return createMessage();
                        }
                    }, this);
        }
        catch (Exception e)
        {
            e.fillInStackTrace();
            Log.e("Error","Error occured");
        }

        //See if app got called by AndroidBeam intent.

    }



//    @Override
//    public void onNewIntent(Intent intent) {
//        // onResume gets called after this to handle the intent
//        setIntent(intent);
//    }


    private NdefMessage createMessage() {

        String mimeType = "application/com.project.businesscardexchange.yan.businesscardexchange";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

        //GENERATE PAYLOAD
        BusinessCardRealm newCard = new BusinessCardRealm();
        newCard.setCompanyName(prefs.getString(COMPANY_NAME, "NA"));
        newCard.setName(prefs.getString(NAME, "NA"));
        newCard.setEmailAddress(prefs.getString(EMAIL_ADDRESS, "NA"));
        newCard.setPhone(prefs.getString(PHONE, "NA"));
        newCard.setWebsiteUrl(prefs.getString(WEBSITE_URL, "NA"));
        newCard.setStreet(prefs.getString(STREET,"NA"));
        newCard.setState(prefs.getString(STATE,"NA"));
        newCard.setPost(prefs.getString(POST,"NA"));
        newCard.setDirectPhone(prefs.getString(PHONE_DIRECT_LINE,"NA"));
        newCard.setPhoto(prefs.getString(PHOTO,"NA"));
        newCard.setPhotocompanylogo(prefs.getString(PHOTO_COMPANY_LOGO,"NA"));
        newCard.setCity(prefs.getString(CITY,"NA"));
        newCard.setZipCode(prefs.getString(ZIPCODE,"NA"));
        newCard.setTimestamp(prefs.getString(TIMESTAMP,"NA"));
        newCard.setCountryName(prefs.getString(COUNTRY_NAME,"NA"));
        newCard.setOwn(false);

        String toJson = gson.toJson(newCard);
        byte[] payLoad = toJson.getBytes();
        // TextView text = (TextView) findViewById(R.id.text);
        // byte[] payLoad = text.getText().toString().getBytes();

        String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+ File.separator+MyApplication.ZIP_DIRECTORY_NAME;
        // Log.d(tag,"inputPath:"+inputPath);
        File file = new File(inputPath+newCard.getTimestamp()+".zip");
        //  Log.d(tag,"filePath:"+file.getAbsolutePath());
        if (file.exists()) {
           // String zipName = newCard.getTimestamp();
            // byte[] payLoad =  Uri.fromFile(new File(zipName)).get;
            FileInputStream fileInputStream = null;
           // File fileZip = new File(zipName);
            // byte[] bFile = new byte[(int) fileZip.length()];
            byte[] bFile = new byte[(int) file.length()];
            try {
                //convert file into array of bytes
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] payLoad2 = bFile;

            //GENERATE NFC MESSAGE
            return new NdefMessage(
                    new NdefRecord[]{
                            new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                    mimeBytes,
                                    null,
                                    payLoad),
                            new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                    mimeBytes,
                                    null,
                                    payLoad2),
                            NdefRecord.createApplicationRecord("com.project.businesscardexchange.yan.businesscardexchange")
                    });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Can not share MainActivity:"+newCard.getName(), Toast.LENGTH_SHORT).show();
            byte[] payLoad2 = null;
            //GENERATE NFC MESSAGE
            return new NdefMessage(
                    new NdefRecord[]{
                            new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                    mimeBytes,
                                    null,
                                    payLoad),
                            new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                    mimeBytes,
                                    null,
                                    payLoad2),
                            NdefRecord.createApplicationRecord("com.project.businesscardexchange.yan.businesscardexchange")
                    });
        }

    }

    //wifi this or nfc
    //NFC transfer

    private void extractPayload(Intent beamIntent) {
        Parcelable[] messages = beamIntent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) messages[0];
        //NdefRecord reescord = message.getRecords()[0];
       // String payload = new String(record.getPayload());

        //First record contains JSON object
        NdefRecord record1 = message.getRecords()[0];
        //
        NdefRecord record2 = message.getRecords()[1];
        putInsideDatabase2(new String(record1.getPayload()),record2.getPayload());

         Toast.makeText(this,"Successfully Transferred!!",Toast.LENGTH_LONG).show();

    }


    public void putInsideDatabase2(String payload,byte[] payload1){



        try {
            JSONObject jsonObject  = new JSONObject(payload);
//            private String name;
//            private String companyName;
//            private String websiteUrl;
//            private String phone;
//            private String emailAddress;


//            public BCard(String name, String companyName, String phone, String email, String website, String directPhone,
// String street, String photo, String post, String city, String state, String zipCode){
            BusinessCard bCard = new BusinessCard();
            bCard.setName(jsonObject.getString("name"));
            bCard.setCompanyName(jsonObject.getString("companyName"));
            bCard.setPhone(jsonObject.getString("phone"));
            bCard.setEmailAddress(jsonObject.getString("emailAddress"));
            bCard.setWebsiteUrl(jsonObject.getString("websiteUrl"));
            bCard.setDirectPhone(jsonObject.getString("directPhone"));
            bCard.setStreet(jsonObject.getString("street"));
            bCard.setPhoto(jsonObject.getString("photo"));
            bCard.setPost(jsonObject.getString("post"));
            bCard.setCity(jsonObject.getString("city"));
            bCard.setState(jsonObject.getString("state"));
            bCard.setZipCode(jsonObject.getString("zipCode"));
            bCard.setPhotocompanylogo(jsonObject.getString("photocompanylogo"));
            bCard.setTimestamp(jsonObject.getString("timestamp"));
            try {
                bCard.setCountryName(jsonObject.getString("country_name"));
                //
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
            bCard.setIsOwn(0);


            /*String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+ File.separator+MyApplication.ZIP_DIRECTORY_NAME;
            // Log.d(tag,"inputPath:"+inputPath);
            String outFnm = inputPath+bCard.getTimestamp()+".zip";
            try {
                FileOutputStream fileOuputStream = new FileOutputStream(outFnm);
                fileOuputStream.write(payload1);
                fileOuputStream.close();

            } catch ( IOException iox ){
                iox.printStackTrace();
            }
*/

            //Step1: Make folder if not exist
            String outputPathWrite = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+ File.separator+MyApplication.IMAGE_DIRECTORY_NAME+File.separator+bCard.getTimestamp();
            File dir = new File(outputPathWrite);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //Step2: Loop through zip byte array and put files to folder
            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(payload1));
            ZipEntry entry = null;
            try {
                while ((entry = zipStream.getNextEntry()) != null) {

                    String entryName = entry.getName();
                    Log.e("BYTE_CHECK", "entryName;" + entryName);
                    FileOutputStream out = new FileOutputStream(outputPathWrite+File.separator+entryName);//openFileOutput(entryName, Context.MODE_PRIVATE);
                    if(entryName.equals("DATA.txt"))
                    {
                    }
                    else if(entryName.equals("LOGO.jpg"))
                    {
                        bCard.setPhotocompanylogo(outputPathWrite+File.separator+entryName);
                    }
                    else if(entryName.equals("PHOTO.jpg"))
                    {
                        bCard.setPhoto(outputPathWrite+File.separator+entryName);
                    }
                    byte[] byteBuff = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipStream.read(byteBuff)) != -1) {
                        out.write(byteBuff, 0, bytesRead);
                    }
                    out.close();
                    zipStream.closeEntry();
                }
                zipStream.close();
            } catch (Exception e) {
                Log.e("BYTE_CHECK", "Error2;" + e.getLocalizedMessage());
            }
            //Step3: Make zip of file and Write that zip to Zip folder
            WifiFilehelper zipManager = new WifiFilehelper();
            String[] s = new String[3];
            // Type the path of the files in here
            s[0] = bCard.getPhoto();
            Log.e("BYTE_CHECK", "F1;" + s[0]);

            File txt_name = WifiFilehelper.getOutputMediaFile(7,bCard.getTimestamp(),"");
            zipManager.writeToFile(getApplicationContext(),bCard.toString(),txt_name);
            //zipManager.writeToFile(getActivity(),copyOfBCard.toString(),txt_name);
            s[1] = txt_name.getAbsolutePath();
            File dirS1 = new File(txt_name.getAbsolutePath());
            if (!dirS1.exists()) {
                //dir2.mkdirs();
                Log.e("BYTE_CHECK", "F1Error;" + "Not Found");
            }
            else {
                Log.e("BYTE_CHECK", "F1 Found");

            }
            Log.e("BYTE_CHECK", "F2;" + s[1]);
            s[2] = bCard.getPhotocompanylogo();
            Log.e("BYTE_CHECK", "F3;" + s[2]);
            File dirS2 = new File( bCard.getPhotocompanylogo());
            if (!dirS2.exists()) {
                //dir2.mkdirs();
                Log.e("BYTE_CHECK", "F3Error;" + "Not Found");
            }
            else {
                Log.e("BYTE_CHECK", "F3 Found");

            }
            //  Log.e("ZipTest","s0:"+imageEncoded);
            //  Log.e("ZipTest","s1:"+logoEncoded);
            //  Log.e("ZipTest","s2:"+txt_name.getAbsolutePath());
            // calling the zip function
            String outputPathWrite2 = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+File.separator+MyApplication.ZIP_DIRECTORY_NAME;
            File dir2 = new File(outputPathWrite2);
            if (!dir2.exists()) {
                dir2.mkdirs();
            }
            zipManager.zip(s, outputPathWrite2+bCard.getTimestamp()+".zip");
            Log.e("BYTE_CHECK", "PATH;" + outputPathWrite2+bCard.getTimestamp()+".zip");


            //  myRealm.beginTransaction();
            // BusinessCardRealm copyOfBCard = myRealm.copyToRealm(bCard);
            //myRealm.commitTransaction();
            //Step4: Finally Insert into database
            myDbHelper.insertCard(bCard);
            //Step5: Resend own card
          /*  try
            {
                //sending Part of NFC
                nfcAdapter.setNdefPushMessageCallback(
                        new NfcAdapter.CreateNdefMessageCallback() {
                            public NdefMessage createNdefMessage(NfcEvent event) {
                                return createMessage();
                            }
                        }, this);

            }
            catch (Exception e)
            {
                e.fillInStackTrace();
                Log.e("Error","Error occured");
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

            Cache.selectedList.clear();
            startActivity(new Intent(MainActivity.this, ReceiveActivity.class)
                    .putExtra("name", Build.DEVICE));//nameEdit.getText().toString()));
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
        } else if (id == R.id.nav_create_business_card) {

            startActivity(new Intent(MainActivity.this, ProfileActivity.class));


        } else if (id == R.id.nav_my_card) {
         startActivity(new Intent(this,MyCardActivity.class));
        } else if (id == R.id.nav_business_cards) {

            startActivity(new Intent(this,CardsActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... string) {






            byte[] decodedString = Base64.decode(string[0], Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedBitmap;

        }



        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);

            imageView.setImageBitmap(bm);

        }


    }

}
