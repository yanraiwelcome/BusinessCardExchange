package com.project.businesscardexchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.project.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.utils.DBHelper;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

//import io.realm.Realm;

public class MyCardActivity extends AppCompatActivity {

    private KenBurnsView photoImageView;

    private ImageView logoImageView;
   // DotProgressBar dotProgressBar;

    //SharedPreferences prefs;
    String url;
  //  Realm myRealm;
    DBHelper myDbHelper;

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String NAME = "nameKey";
    private static final String COMPANY_NAME = "companyNameKey";
    private static final String EMAIL_ADDRESS = "emailKey";
    private static final String PHONE = "phoneKey";
    private static final String WEBSITE_URL = "websiteUrlKey";
    public static final String PHONE_DIRECT_LINE = "directLineKey";
    public static final String POST="postKey";
    public static final String STREET = "streetKey";
    public static final String CITY = "cityKey";
    public static final String STATE = "stateKey";
    public static final String ZIPCODE = "zipcodeKey";

    BusinessCard myOwn;
    String myOwnPhoto,myOwnLogo;

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

      //  dotProgressBar = (DotProgressBar)findViewById(R.id.dot_progress_bar_1);

        TextView name = (TextView)findViewById(R.id.mycardactivity_name);
        TextView companyName = (TextView)findViewById(R.id.mycardactivity_comapny_name);
        final TextView phone = (TextView)findViewById(R.id.mycardactivity_phone);
        final TextView email = (TextView)findViewById(R.id.mycardactivity_email);
        final TextView webiste = (TextView)findViewById(R.id.mycardactivity_website);
        final TextView directPhone = (TextView)findViewById(R.id.mycardactivity_direct_line);
        TextView post = (TextView)findViewById(R.id.mycardactivity_post);
        TextView street = (TextView)findViewById(R.id.mycardactivity_street);
        TextView city = (TextView)findViewById(R.id.mycardactivity_city);
        TextView state = (TextView)findViewById(R.id.mycardactivity_state);
        TextView zipCode = (TextView)findViewById(R.id.mycardactivity_zip_code);

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
            phone.setText("Company line: "+myOwn.getPhone());
            email.setText(myOwn.getEmailAddress());
            email.setPaintFlags(email.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            webiste.setText(myOwn.getWebsiteUrl());
            webiste.setPaintFlags(webiste.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            directPhone.setText("Cell: "+myOwn.getDirectPhone());
            post.setText(myOwn.getPost());
            street.setText(myOwn.getStreet());
            city.setText(myOwn.getCity()+","+myOwn.getState()+""+myOwn.getZipCode());
            //state.setText(myOwn.getState());
           // zipCode.setText(myOwn.getZipCode());
            myOwnPhoto = myOwn.getPhoto();
            myOwnLogo = myOwn.getPhotocompanylogo();
        }


        //intent to dial company line
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString()));
                startActivity(intent);
            }
        });

        //intent to dial direct line
        directPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + directPhone.getText().toString()));
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
