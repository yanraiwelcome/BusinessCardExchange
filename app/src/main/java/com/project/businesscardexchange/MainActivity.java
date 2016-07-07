package com.project.businesscardexchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.project.businesscardexchange.fragments.ProfileActivity;
import com.project.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.models.BusinessCardRealm;
import com.project.businesscardexchange.utils.DBHelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //for navigation drawer

    String encodedImageString;
    KenBurnsView imageView;
    ExpandableMenuOverlay menuOverlay;

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

    NfcAdapter mNfcAdapter;
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

        //setting up navigation drawer background

        if(prefs.getString(NAME,null)!= null){
        TextView textView = (TextView)hView.findViewById(R.id.text_navigation_drawer);
        textView.setText("Hi, "+ prefs.getString(NAME,"you") + " !!");
          //  new MyAsyncTask().execute(prefs.getString("PHOTO","Not available"));
    }
    gson = new Gson();

        // Check for available NFC Adapter
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
//           finish();
//            return;
//        }
        //set the navigation header profile with name and email address from the shared preference
        //trying we can delelte this

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            extractPayload(getIntent());
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
       // set Ndef message to send by beam
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
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
        String mimeType = "application/com.project.businesscardexchange";
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

        //GENERATE NFC MESSAGE
        return new NdefMessage(
                new NdefRecord[]{
                        new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                                mimeBytes,
                                null,
                                payLoad),
                        NdefRecord.createApplicationRecord("com.project.businesscardexchange")
                });
    }

    private void extractPayload(Intent beamIntent) {
        Parcelable[] messages = beamIntent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) messages[0];
        NdefRecord record = message.getRecords()[0];
        String payload = new String(record.getPayload());

       putInsideDatabase(payload);

        Toast.makeText(this,"Successfully Transferred!!",Toast.LENGTH_LONG).show();

    }

    public void putInsideDatabase(String payload){
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
            try {
                bCard.setCountryName(jsonObject.getString("country_name"));

            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
            bCard.setIsOwn(0);

          //  myRealm.beginTransaction();
           // BusinessCardRealm copyOfBCard = myRealm.copyToRealm(bCard);
            //myRealm.commitTransaction();
            myDbHelper.insertCard(bCard);

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
