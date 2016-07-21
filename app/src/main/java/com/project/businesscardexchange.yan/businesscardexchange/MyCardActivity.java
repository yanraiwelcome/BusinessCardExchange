package com.project.businesscardexchange.yan.businesscardexchange;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.project.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCardRealm;
import com.project.businesscardexchange.yan.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.RadarScanActivity;
import com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.fragment.CardFragment;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.CardInfo;
import com.project.businesscardexchange.yan.businesscardexchange.utils.DBHelper;
import com.project.businesscardexchange.yan.businesscardexchange.utils.ToastUtils;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

//import io.realm.Realm;

public class MyCardActivity extends AppCompatActivity {

    private KenBurnsView photoImageView;

    private ImageView logoImageView;
    // DotProgressBar dotProgressBar;

    //SharedPreferences prefs;
    String url;
    //  Realm myRealm;
    DBHelper myDbHelper;



    BusinessCard myOwn;
    String myOwnPhoto,myOwnLogo;
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("NFC_TEST","start");
        // set Ndef message to send by beam
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        assert nfcAdapter != null;
        try
        {
            Log.e("NFC_TEST","initialize");

            nfcAdapter.setNdefPushMessageCallback(
                    new NfcAdapter.CreateNdefMessageCallback() {
                        public NdefMessage createNdefMessage(NfcEvent event) {
                            Log.e("NFC_TEST","Created Message");

                            return createMessage();
                        }
                    }, this);
        }
        catch (Exception e)
        {
            e.fillInStackTrace();
            Log.e("NFC_TEST","ERROR");
        }

        //See if app got called by AndroidBeam intent.

    }
    SharedPreferences prefs;
    Gson gson;
    public static final String  MyPREFERENCES = "MyPrefs" ;
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

        String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+ File.separator+MyApplication.ZIP_DIRECTORY_NAME;
        // Log.d(tag,"inputPath:"+inputPath);
        File file = new File(inputPath+newCard.getTimestamp()+".zip");
        //  Log.d(tag,"filePath:"+file.getAbsolutePath());
        if (file.exists()) {
            Log.e("NFC_TEST","File found, trying to share");

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
            Log.e("NFC_TEST","Success");

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
                            NdefRecord.createApplicationRecord("com.project.businesscardexchange")
                    });
        }
        else
        {
            Log.e("NFC_TEST","Can not share");

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
                            NdefRecord.createApplicationRecord("com.project.businesscardexchange")
                    });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //  myRealm = MyApplication.getRealmInstance(getApplicationContext());
        myDbHelper = DBHelper.getInstance(getApplicationContext());
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

      /*  final BusinessCardRealm[] myDog = new BusinessCardRealm[1];
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                myDog[0] = realm.createObject(BusinessCardRealm.class);
                myDog[0].setName("Fido");
                myDog[0].setPhone("psdasd");
            }
        });

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BusinessCardRealm myPuppy = realm.where(BusinessCardRealm.class).equalTo("phone", "psdasd").findFirst();
                myPuppy.setPhone("newwwww");
            }
        });

        Toast.makeText(getApplicationContext(),":"+myDog[0].getPhone(),Toast.LENGTH_LONG).show();
*/

        try {
            // myOwn =   myRealm.where(BusinessCardRealm.class).equalTo("isOwn",true).findFirst();
            myOwn = myDbHelper.getSingleOwn();
            /*for(BusinessCardRealm c:results1)
            {
                if(c.isOwn() == true) {
                    Toast.makeText(getApplicationContext(), "Own Found: " + c.getName(), Toast.LENGTH_LONG).show();
                    myOwn =c;
                    break;
                }
            }*/

            //  Log.e("Result","Total:"+results1.size());
            //lists = select.all().from(BCard.class).orderBy("name ASC").execute();
        } catch (Exception e)
        {
            Log.e("Error","No any record");
            e.fillInStackTrace();
        }

        CardView myCard = (CardView) findViewById(R.id.card_view);
        myCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCardActivity.this);
                builder.setTitle("Choose Action:");
                builder.setItems(new CharSequence[]
                                {"Share", "Send to wifi"},//, "Send to NFC"
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // The 'which' argument contains the index position
                                // of the selected item
                                //   RealmResults<BusinessCardRealm> results = myRealm.where(BusinessCardRealm.class).findAll();
                                //   results.sort("name");
                                final BusinessCard b = myOwn;//bizCardLists.get(position);

                                switch (which) {
                                    case 0:
                                        String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir()) + File.separator + MyApplication.ZIP_DIRECTORY_NAME;
                                        // Log.d(tag,"inputPath:"+inputPath);
                                        File file = new File(inputPath + b.getTimestamp() + ".zip");
                                        //  Log.d(tag,"filePath:"+file.getAbsolutePath());
                                        if (file.exists()) {
                                            String zipName = b.getTimestamp();
                                            Intent shareIntent = new Intent();
                                            shareIntent.setAction(Intent.ACTION_SEND);
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(zipName)));
                                            shareIntent.setType("*/*");
                                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Can not share 1:" + b.getName(), Toast.LENGTH_SHORT).show();
                                        }

                                        break;
                               /*     case 1:
                                        // Toast.makeText(getApplicationContext(), "clicked 2:"+b.getName(), Toast.LENGTH_SHORT).show();
                                        //  Toast.makeText(CardsActivity.this, "share", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("tel:"));
//                                startActivity(intent);
                                        try {

                                            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(MyCardActivity.this);
                                            assert nfcAdapter != null;
                                            nfcAdapter.setNdefPushMessageCallback(
                                                    new NfcAdapter.CreateNdefMessageCallback() {
                                                        public NdefMessage createNdefMessage(NfcEvent event) {
                                                            return new NFCService(getApplicationContext(), 0, b).createMessage();
                                                        }
                                                    }, MyCardActivity.this);
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), "NFC ERROR ", Toast.LENGTH_LONG).show();
                                        }

                                        break;
                               */     case 1:
                                        //Toast.makeText(getApplicationContext(), "clicked 3:"+b.getName(), Toast.LENGTH_SHORT).show();
                                       /* Intent gotoWifi = new Intent(CardsActivity.this, com.project.businesscardexchange.ui.main.MainActivity.class);
                                        gotoWifi.putExtra("type","shareCard");
                                        //gotoWifi.putExtra("card",b);
                                        startActivity(gotoWifi);*/
                                        CardInfo info = CardFragment.getCardInfo(b);
                                        try {
                                            P2PFileInfo fileInfo = new P2PFileInfo();
                                            fileInfo.name = info.getFileName();
                                            fileInfo.type = P2PConstant.TYPE.APP;
                                            fileInfo.size = new File(info.getFilePath()).length();
                                            fileInfo.path = info.getFilePath();
                                            Cache.selectedList.add(fileInfo);
                                        } catch (Exception e) {
                                            Log.e("ErrorWifi", "Do cache:" + e.getLocalizedMessage());
                                        }
                                        if (Cache.selectedList.size() > 0)
                                            startActivity(new Intent(MyCardActivity.this,
                                                    RadarScanActivity.class).putExtra("name", Build.DEVICE));
                                        else
                                            ToastUtils.showTextToast(getApplicationContext(),
                                                    getString(R.string.file_not_found));
                                        break;
                                }
                            }
                        });
                builder.create().show();

                return false;
            }
        });
        //  dotProgressBar = (DotProgressBar)findViewById(R.id.dot_progress_bar_1);
        LinearLayout ll_address_content = (LinearLayout) findViewById(R.id.ll_address_content);
        TextView name = (TextView)findViewById(R.id.mycardactivity_name);
        TextView companyName = (TextView)findViewById(R.id.mycardactivity_comapny_name);
        final TextView phone = (TextView)findViewById(R.id.mycardactivity_phone);
        final TextView email = (TextView)findViewById(R.id.mycardactivity_email);
        final TextView webiste = (TextView)findViewById(R.id.mycardactivity_website);
        final TextView directPhone = (TextView)findViewById(R.id.mycardactivity_direct_line);
        final TextView post = (TextView)findViewById(R.id.mycardactivity_post);
        final TextView street = (TextView)findViewById(R.id.mycardactivity_street);
        final TextView city = (TextView)findViewById(R.id.mycardactivity_city);
        final TextView state = (TextView)findViewById(R.id.mycardactivity_state);
        final TextView zipCode = (TextView)findViewById(R.id.mycardactivity_zip_code);
        TextView country_name = (TextView)findViewById(R.id.mycardactivity_country_name);

        photoImageView = (KenBurnsView)findViewById(R.id.image_test);
        logoImageView = (ImageView) findViewById(R.id.image_company_logo);

        ShowTipsView showtips = new ShowTipsBuilder(this)

                .setTarget(photoImageView)
                .setTitle("YOUR BUSINESS CARD")
                .setDescription("See your business card after you have created")
                .setDelay(1000)
                .displayOneTime(2)
                .setBackgroundColor(R.color.colorAccent)
                .setBackgroundAlpha(95)
                .setCloseButtonColor(R.color.colorAccent)
                .setButtonText("GOT IT?")
                .build();

        showtips.setDisplayOneTimeID(2);


        showtips.show(this);

        // prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        if(myOwn != null)
        {
            name.setText(myOwn.getName());
            companyName.setText(myOwn.getCompanyName());
            // phone.setText("Company line: "+myOwn.getPhone());
            try {

                phone.setText("Comp: " + PhoneNumberUtils.formatNumber(myOwn.getPhone()));
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
            email.setText(myOwn.getEmailAddress());
            email.setPaintFlags(email.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            webiste.setText(myOwn.getWebsiteUrl());
            webiste.setPaintFlags(webiste.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            // directPhone.setText("Cell: "+myOwn.getDirectPhone());
            try {
                directPhone.setText("Cell: " + PhoneNumberUtils.formatNumber(myOwn.getDirectPhone()));
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
            post.setText(myOwn.getPost());
            street.setText(myOwn.getStreet());
            city.setText(myOwn.getCity()+", "+myOwn.getState()+" "+myOwn.getZipCode());
            if(myOwn.getCountryName()!= null && !myOwn.getCountryName().trim().equals("") )
            {
                country_name.setText(myOwn.getCountryName());
                country_name.setVisibility(View.VISIBLE);

            }
            else
            {
                country_name.setVisibility(View.GONE);
            }

            //state.setText(myOwn.getState());
            // zipCode.setText(myOwn.getZipCode());
            myOwnPhoto = myOwn.getPhoto();
            myOwnLogo = myOwn.getPhotocompanylogo();
        }

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PhoneNumberUtils.formatNumber(myOwn.getPhone()) ));
                startActivity(intent);
            }

        });


        //intent to dial company line
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PhoneNumberUtils.formatNumber(myOwn.getPhone()) ));
                startActivity(intent);
            }
        });


        //intent to dial direct line
        directPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +  PhoneNumberUtils.formatNumber(myOwn.getDirectPhone()) ));
                startActivity(intent);
            }
        });

        //intent to send email
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email.getText().toString(), null));
                startActivity(intent);
            }
        });

        //intent to go to the website
        url=webiste.getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        webiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        ll_address_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Geocoder coder = new Geocoder(context);
                    try {
                        ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("Your Address", 50);
                        //for(Address add : adresses){
                           // if (statement) {//Controls to ensure it is right address such as country etc.
                                double longitude = adresses.get(0).getLongitude();
                                double latitude = adresses.get(0).getLatitude();
                           // }
                       // }
                    } catch (IOException e) {
                        Log.e("address_Error","Error:"+e.getLocalizedMessage());
                        e.printStackTrace();

                    } catch (Exception e) {
                        Log.e("address_Error","Error:"+e.getLocalizedMessage());
                        e.printStackTrace();
                    }*/
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.co.in/maps?q="+street.getText()+","+city.getText()+","+state.getText()+" "+zipCode.getText()));
                startActivity(intent);
            }
        });

        //...
        //now starting async task to handle image in different thread

        // new MyAsyncTask().execute("");

        // Toast.makeText(this,imageEncoded,Toast.LENGTH_LONG).show();
        // new MyAsyncTaskForLogo().execute("");

        try {
            // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeFile(myOwnPhoto);//BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photoImageView.setImageBitmap(decodedBitmap);

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap1 = BitmapFactory.decodeFile(myOwnLogo);//BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            logoImageView.setImageBitmap(decodedBitmap1);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private class MyAsyncTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dotProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String imageEncoded = null;// prefs.getString("PHOTO", "Default value");
            if(myOwnPhoto == null)
            {
                return null;

            }
            else
            {
                imageEncoded =myOwnPhoto;
                byte[] decodedString = Base64.decode(imageEncoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                return decodedBitmap;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                Log.e("Url Image",":"+myOwn.getPhoto());
                if (bitmap != null) {
                    photoImageView.setImageBitmap(bitmap);
                    //   dotProgressBar.setVisibility(View.GONE);
                } else {

                }
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
                Log.e("Error Image",e.getLocalizedMessage());

            }
        }

    }
    private class MyAsyncTaskForLogo extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //  String imageEncoded = prefs.getString("PHOTO","Default value");
            String logoEncoded = null;//prefs.getString("photocompanylogo", "Default Value");
            if(myOwnLogo == null)
            {
                return null;
            }
            else
            {
                logoEncoded = myOwnLogo;
                byte[] decodedStringLogo = Base64.decode(logoEncoded,Base64.DEFAULT);

                // byte[] decodedString = Base64.decode(imageEncoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedStringLogo, 0, decodedStringLogo.length);
                return decodedBitmap;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap !=null) {
                logoImageView.setImageBitmap(bitmap);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MyCardActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
