package com.project.businesscardexchange.fragments;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.project.businesscardexchange.MainActivity;
import com.project.businesscardexchange.MyApplication;
import com.project.businesscardexchange.R;
import com.project.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.ui.WifiFilehelper;
import com.project.businesscardexchange.utils.DBHelper;
import com.project.businesscardexchange.utils.ImageHelper;
import com.project.businesscardexchange.utils.MarshMallowPermission;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Robus on 2/4/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    String myuploadType;
    String filePath,filePathLogo;
    Uri fileUri,fileUriLogo;
    String timeStamp;

    Bitmap myBitmap;
    DotProgressBar progressBar;

    EditText websiteEdittext;
    EditText emailEdittext;
    EditText phoneEdittext;
    EditText comapanyNameEdittext;
    EditText nameEdittext;
    EditText mPhoneDirectLine;
    EditText mPost;
    EditText mStreet;
    EditText mCity;
    EditText mState;
    EditText mZipCode;

    MorphingButton btnMorph, btnMorphLogo;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String NAME = "nameKey";
    public static final String COMPANY_NAME = "companyNameKey";
    public static final String EMAIL_ADDRESS = "emailKey";
    public static final String PHONE = "phoneKey";
    public static final String WEBSITE_URL = "websiteUrlKey";
    public static final String PHONE_DIRECT_LINE = "directLineKey";
    public static final String POST = "postKey";
    public static final String STREET = "streetKey";
    public static final String CITY = "cityKey";
    public static final String STATE = "stateKey";
    public static final String ZIPCODE = "zipcodeKey";
    public static final String PHOTO_COMPANY_LOGO = "photocompanylogo";
    public static final String TIMESTAMP = "timestamp";


    SharedPreferences sharedpreferences;


    final int CONTEXT_MENU_FROM_CAMERA_IMAGE = 1;
    final int CONTEXT_MENU_FROM_GALLERY_IMAGE = 2;
    final int CONTEXT_MENU_FROM_CAMERA_IMAGE_LOGO = 3;
    final int CONTEXT_MENU_FROM_GALLERY_IMAGE_LOGO = 4;
    final int CONTEXT_MENU_CANCEL = 7;
    private static final int CHOOSE_IMAGE_GALLERY_REQUEST_CODE = 400;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    DBHelper myDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        //  getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //   getDialog().getWindow().setTitle("CREATE BUSINESS CARD");
        // getDialog().setTitle("CREATE BUSINESS CARD");

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        marshMallowPermission = new MarshMallowPermission(getActivity());
        timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      //  myRealm = Realm.getInstance(getActivity());
        myDbHelper = DBHelper.getInstance(getActivity());
        // get the file url
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable("file_uri");
            filePath = savedInstanceState.getString("file_path");
            fileUriLogo = savedInstanceState.getParcelable("file_uri_logo");
            filePathLogo = savedInstanceState.getString("file_path_logo");
            myuploadType = savedInstanceState.getString("display_type");
            myPhototype = savedInstanceState.getString("photo_type");
            timeStamp = savedInstanceState.getString("time_stamp");

        }

        btnMorph = (MorphingButton) view.findViewById(R.id.upload_photo_button);
        btnMorphLogo = (MorphingButton) view.findViewById(R.id.upload_company_logo);
        btnMorphLogo.setOnClickListener(this);
        btnMorph.setOnClickListener(this);

        ShowTipsView showtips = new ShowTipsBuilder(getActivity())

                .setTarget(btnMorph)
                .setTitle("Company Photo")
                .setDescription("This button is used to upload company photo")
                .setDelay(1000)
                .displayOneTime(2)
                .setBackgroundColor(R.color.colorPrimaryDark)
                .setBackgroundAlpha(50)
                .setCloseButtonColor(R.color.buttonColor)

                .build();

        showtips.setDisplayOneTimeID(2);
        showtips.show(getActivity());


        progressBar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);

        nameEdittext = (EditText) view.findViewById(R.id.editText);
        comapanyNameEdittext = (EditText) view.findViewById(R.id.editText2);
        phoneEdittext = (EditText) view.findViewById(R.id.editText3);
        //for phone no formatting
        emailEdittext = (EditText) view.findViewById(R.id.editText4);
        websiteEdittext = (EditText) view.findViewById(R.id.editText5);
        mPhoneDirectLine = (EditText) view.findViewById(R.id.editText6);
        mPost = (EditText) view.findViewById(R.id.editText_post);

        setHypen(phoneEdittext);
        setHypen(mPhoneDirectLine);


        mStreet = (EditText) view.findViewById(R.id.editText_street);
        mCity = (EditText) view.findViewById(R.id.editText_city);
        mState = (EditText) view.findViewById(R.id.editText_state);
        mZipCode = (EditText) view.findViewById(R.id.editText_zip_code);


        //.........
        Button createButton = (Button) view.findViewById(R.id.create_profile);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageEncoded = sharedpreferences.getString("PHOTO", "x");
                String logoEncoded = sharedpreferences.getString("photocompanylogo", "x");


                if (!logoEncoded.equals("x") && !imageEncoded.equals("x") && !nameEdittext.getText().toString().equals("") && !comapanyNameEdittext.getText().toString().equals("") && !phoneEdittext.getText().toString().equals("") && !emailEdittext.getText().toString().equals("") && !websiteEdittext.getText().toString().equals("") && !mPhoneDirectLine.getText().toString().equals("")) {
                    BusinessCard bCard = new BusinessCard();
                    bCard.setName(nameEdittext.getText().toString());
                    bCard.setCompanyName(comapanyNameEdittext.getText().toString());
                    bCard.setPhone(phoneEdittext.getText().toString());
                    bCard.setEmailAddress(emailEdittext.getText().toString());
                    bCard.setWebsiteUrl(websiteEdittext.getText().toString());
                    bCard.setDirectPhone(mPhoneDirectLine.getText().toString());
                    bCard.setStreet(mStreet.getText().toString());
                    bCard.setPhoto(imageEncoded);
                    bCard.setPost(mPost.getText().toString());
                    bCard.setCity(mCity.getText().toString());
                    bCard.setState(mState.getText().toString());
                    bCard.setZipCode(mZipCode.getText().toString());
                    bCard.setPhotocompanylogo(logoEncoded);
                    bCard.setIsOwn(1);
                    bCard.setTimestamp(timeStamp);
                    //Start : shared preference for NFC use
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(NAME, nameEdittext.getText().toString());
                    editor.putString(COMPANY_NAME, comapanyNameEdittext.getText().toString());
                    editor.putString(EMAIL_ADDRESS, emailEdittext.getText().toString());
                    editor.putString(PHONE,phoneEdittext.getText().toString());
                    editor.putString(WEBSITE_URL,websiteEdittext.getText().toString());
                    editor.putString(PHONE_DIRECT_LINE,mPhoneDirectLine.getText().toString());
                    editor.putString(POST,mPost.getText().toString());
                    editor.putString(STREET, mStreet.getText().toString());
                    editor.putString(CITY, mCity.getText().toString());
                    editor.putString(STATE, mState.getText().toString());
                    editor.putString(ZIPCODE, mZipCode.getText().toString());
                    editor.putString(TIMESTAMP, timeStamp);
                    editor.commit();
                    //End


                 //   myRealm.beginTransaction();
                 //   BusinessCardRealm copyOfBCard = myRealm.copyToRealm(bCard);

                    myDbHelper.insertCard(bCard);
                    JSONObject myCardObject = new JSONObject();
                    try {
                        myCardObject.put("name",bCard.getName());
                        myCardObject.put("companyName",bCard.getCompanyName());
                        myCardObject.put("websiteUrl",bCard.getWebsiteUrl());
                        myCardObject.put("phone",bCard.getPhone());
                        myCardObject.put("emailAddress",bCard.getEmailAddress());
                        myCardObject.put("directPhone",bCard.getDirectPhone());
                        myCardObject.put("photo",bCard.getPhoto());
                        myCardObject.put("post",bCard.getPost());
                        myCardObject.put("street",bCard.getStreet());
                        myCardObject.put("city",bCard.getCity());

                        myCardObject.put("state",bCard.getState());
                        myCardObject.put("zipCode",bCard.getZipCode());
                        myCardObject.put("photocompanylogo",bCard.getPhotocompanylogo());
                        myCardObject.put("timestamp",bCard.getTimestamp());
                        myCardObject.put("isOwn",0); //false because it is to be transfered to other



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                  //  myRealm.commitTransaction();

                    String[] s = new String[3];
                    // Type the path of the files in here
                    s[0] = imageEncoded;

                    File txt_name = getOutputMediaFile(7);//inputPath + "/textfile.txt"; // /sdcard/ZipDemo/textfile.txt
                   String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+File.separator+MyApplication.ZIP_DIRECTORY_NAME;
                 //  Log.e("ZipTest","inputPath:"+inputPath);
                    // first parameter is d files second parameter is zip
                    // file name
                    WifiFilehelper zipManager = new WifiFilehelper();
                    zipManager.writeToFile(getActivity(),myCardObject.toString(),txt_name);
                    //zipManager.writeToFile(getActivity(),copyOfBCard.toString(),txt_name);
                    s[1] = txt_name.getAbsolutePath();
                    s[2] = logoEncoded;
                  //  Log.e("ZipTest","s0:"+imageEncoded);
                  //  Log.e("ZipTest","s1:"+logoEncoded);
                  //  Log.e("ZipTest","s2:"+txt_name.getAbsolutePath());
                    // calling the zip function
                    File dir = new File(inputPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    zipManager.zip(s, inputPath +timeStamp+".zip");


                    Toast.makeText(getActivity(), "Successfully Created Business Card", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));

                } else {
                    Toast.makeText(getActivity(), "Please fill out all the fields", Toast.LENGTH_LONG).show();
                }
                //  ProfileFragmentListener profileFragmentListener = (ProfileFragmentListener) getActivity();
                // profileFragmentListener.onCreateButtonClicked(new BusinessCard(comapanyNameEdittext.getText().toString(),emailEdittext.getText().toString(),nameEdittext.getText().toString(),phoneEdittext.getText().toString(),websiteEdittext.getText().toString()));

            }
        });
        return view;
    }

    private void setHypen(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private boolean deletingHyphen;
            private int hyphenStart;
            private boolean deletingBackward;

            @Override
            public void afterTextChanged(Editable text) {
                if (isFormatting)
                    return;

                isFormatting = true;

                // If deleting hyphen, also delete character before or after it
                if (deletingHyphen && hyphenStart > 0) {
                    if (deletingBackward) {
                        if (hyphenStart - 1 < text.length()) {
                            text.delete(hyphenStart - 1, hyphenStart);
                        }
                    } else if (hyphenStart < text.length()) {
                        text.delete(hyphenStart, hyphenStart + 1);
                    }
                }
                if (text.length() == 3 || text.length() == 7) {
                    text.append('-');
                }

                isFormatting = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isFormatting)
                    return;

                // Make sure user is deleting one char, without a selection
                final int selStart = Selection.getSelectionStart(s);
                final int selEnd = Selection.getSelectionEnd(s);
                if (s.length() > 1 // Can delete another character
                        && count == 1 // Deleting only one character
                        && after == 0 // Deleting
                        && s.charAt(start) == '-' // a hyphen
                        && selStart == selEnd) { // no selection
                    deletingHyphen = true;
                    hyphenStart = start;
                    // Check if the user is deleting forward or backward
                    if (selStart == start + 1) {
                        deletingBackward = true;
                    } else {
                        deletingBackward = false;
                    }
                } else {
                    deletingHyphen = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ProfileFragmentListener profileFragmentListener = (ProfileFragmentListener) getActivity();
        // profileFragmentListener.onCreateButtonClicked(new BusinessCard(comapanyNameEdittext.getText().toString(),emailEdittext.getText().toString(),nameEdittext.getText().toString(),phoneEdittext.getText().toString(),websiteEdittext.getText().toString()));


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.upload_photo_button || v.getId() == R.id.upload_company_logo) {
            //Context menu
            if(v.getId() == R.id.upload_photo_button)
            {
                menu.setHeaderTitle("Add photo");
            }
            else
            {
                menu.setHeaderTitle("Add Logo");
            }
            menu.add(Menu.NONE, CONTEXT_MENU_FROM_CAMERA_IMAGE, Menu.NONE, "From Camera");
            menu.add(Menu.NONE, CONTEXT_MENU_FROM_GALLERY_IMAGE, Menu.NONE, "From Gallery");
            menu.add(Menu.NONE, CONTEXT_MENU_CANCEL, Menu.NONE, "Cancel");
        }
//        else if (v.getId() == R.id.btnRecordVideo)
//        {
//            //Context menu
//            menu.setHeaderTitle("Get Video");
//            menu.add(Menu.NONE, CONTEXT_MENU_FROM_CAMERA_VIDEO, Menu.NONE, "Record Video Now");
//            menu.add(Menu.NONE, CONTEXT_MENU_FROM_GALLERY_VIDEO, Menu.NONE, "From Gallery");
//            menu.add(Menu.NONE, CONTEXT_MENU_CANCEL, Menu.NONE, "Cancel");
//
//        }
//        else if (v.getId() == R.id.sound_recorder)
//        {
//            //Context menu
//            menu.setHeaderTitle("Get Audio");
//            menu.add(Menu.NONE, CONTEXT_MENU_FROM_SOUND_RECORDER, Menu.NONE, "Record Audio Now");
//            menu.add(Menu.NONE, CONTEXT_MENU_FROM_GALLERY_AUDIO, Menu.NONE, "From Gallery");
//            menu.add(Menu.NONE, CONTEXT_MENU_CANCEL, Menu.NONE, "Cancel");
//
//        }

    }

    MarshMallowPermission marshMallowPermission;
    /**
     * Launching camera app to capture image
     */


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        myuploadType = "camera";
        Log.e("camera_debug", "1folder:" + MyApplication.getCardLocation().getPath()+File.separator+timeStamp);
        if(!new File(MyApplication.getCardLocation().getPath()+File.separator+timeStamp).exists())
        {
            new File(MyApplication.getCardLocation().getPath()+File.separator+timeStamp).mkdir();
        }
        if(myPhototype.equals("image_own"))
        {

            fileUri = getOutputMediaFileUri(1);//1=image
            Log.e("camera_debug", "1fileUri:" + fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
           // Toast.makeText(getActivity(),"Debug1:"+fileUri,Toast.LENGTH_SHORT).show();

        }
        else {
            fileUriLogo = getOutputMediaFileUri(1);//1=image
            Log.e("camera_debug", "1fileUri:" + fileUriLogo);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriLogo);
           // Toast.makeText(getActivity(),"Debug1:"+fileUriLogo,Toast.LENGTH_SHORT).show();


        }

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
        outState.putParcelable("file_uri_logo", fileUriLogo);
        outState.putString("file_path", filePath);
        outState.putString("file_path_logo", filePathLogo);
        outState.putString("display_type", myuploadType);
        outState.putString("photo_type", myPhototype);
        outState.putString("time_stamp",timeStamp);
    }

    public void getImageFromGallery() {

        myuploadType = "gallery";
        //   Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent i = Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose an image");
        startActivityForResult(i, CHOOSE_IMAGE_GALLERY_REQUEST_CODE);

    }
    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video/audio
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video/audio
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = MyApplication.getCardLocation();

        //OR
  /*
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsolutePath()+File.separator+Config.IMAGE_DIRECTORY_NAME);
*/
//

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d("test", "Oops! Failed create "  + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;

        if(type == 7)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + File.separator + "DATA" + ".txt");
        }
       else {
            if (myPhototype.equals("image_own")) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + timeStamp + File.separator + "PHOTO" + ".jpg");
            } else {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + timeStamp + File.separator + "LOGO" + ".jpg");
            }
        }

        return mediaFile;
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_FROM_CAMERA_IMAGE: {
                Log.e("camera_debug","Clicked");

                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                    Log.e("camera_debug","No Permission1");
                }
                else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                        Log.e("camera_debug", "No Permission2");
                    } else {
                        Log.e("camera_debug", "OpenCamera");
                        captureImage();
                    }
                }


            }
            break;
            case CONTEXT_MENU_FROM_GALLERY_IMAGE: {


                // Edit Action
/*
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                }
                else
*/
                //   {
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    getImageFromGallery();
                }
                // }


            }
            break;
//            case CONTEXT_MENU_FROM_CAMERA_VIDEO:
//            {
//                // record video
//                recordVideo();
//
//            }
//            break;
//            case CONTEXT_MENU_FROM_GALLERY_VIDEO:
//            {
//                // Edit Action
//                getVideoFromGallery();
//
//            }
//            break;
//            case CONTEXT_MENU_FROM_SOUND_RECORDER:
//            {
//                // capture audio
//                recordSound();
//
//            }
//            break;
//
//            case CONTEXT_MENU_FROM_GALLERY_AUDIO:
//            {
//                // Edit Action
//                getAudioFromGallery();
//
//            }
//            break;

            case CONTEXT_MENU_CANCEL: {

            }
            break;
        }

        return super.onContextItemSelected(item);
    }


    String myPhototype;
    @Override
    public void onClick(View v) {
        Log.e("CLICKED_ID",""+v.getId());
        if (v.getId() == R.id.upload_photo_button || v.getId() == R.id.upload_company_logo) {
            if (v.getId() == R.id.upload_photo_button) {
                myPhototype = "image_own";
            }
            else
            {
                myPhototype = "image_logo";
            }
                registerForContextMenu(v);
                getActivity().openContextMenu(v);
            Log.e("CLICKED_ID_OK",""+v.getId());

           /* EasyImage.openGallery(this, 1);
            EasyImage.configuration(getActivity())
                    .setImagesFolderName("My app images") //images folder name, default is "EasyImage"
                    .saveInAppExternalFilesDir();*/

        }

       /* if (v.getId() == R.id.upload_company_logo) {
            //Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
           *//* EasyImage.openGallery(this, 3);
            EasyImage.configuration(getActivity())
                    .setImagesFolderName("My app images") //images folder name, default is "EasyImage"
                    .saveInAppExternalFilesDir();*//*
            myPhototype = "image_logo";
            registerForContextMenu(v);
            getActivity().openContextMenu(v);

        }*/

    }

    private void performCameraSet() {
        //  btnUploadPresciption.setVisibility(View.GONE);

        if(myPhototype.equals("image_own")) {
            if (filePath == null) {
                filePath = getRealPathFromURINew(fileUri);//fileUri.getPath();
                //  compressImage(filePath);
                try {
                    ImageHelper.compressImage(filePath, getActivity());
                    //Start; Addedto fix rotation of 90deg issue on samsund devices
                }
                catch (Exception e)
                {
                    e.fillInStackTrace();
                }
            }
        }
        else
        {
            if (filePathLogo == null) {
                filePathLogo = getRealPathFromURINew(fileUriLogo);//fileUri.getPath();
                //  compressImage(filePath);
                try {
                    ImageHelper.compressImage(filePathLogo, getActivity());
                    //Start; Addedto fix rotation of 90deg issue on samsund devices
                }
                catch (Exception e)
                {
                    e.fillInStackTrace();
                }
            }
        }
        Log.e("camera_debug", "2filePath:" + filePath);
        previewMedia("image");


//                Log.d("testingcamera", "testingcamera");
//                Log.d("testingcamera123", tempPath);
    }

    public void previewMedia(String type) {
        if(myPhototype.equals("image_own")) {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("PHOTO", filePath);
            editor.commit();
            myBitmap = BitmapFactory.decodeFile(filePath);
            ImageView myImage = (ImageView) getActivity().findViewById(R.id.photo1);
            myImage.setImageBitmap(myBitmap);

            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(500)
                    .cornerRadius(200) // 56 dp
                    .width(200) // 56 dp
                    .height(200) // 56 dp
                    .color(R.color.green) // normal state color
                    .colorPressed(R.color.green_teal) // pressed state color
                    .icon(R.drawable.ic_done); // icon

            btnMorph.morph(circle);
        }
        else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(PHOTO_COMPANY_LOGO, filePathLogo);
            editor.commit();
            myBitmap = BitmapFactory.decodeFile(filePathLogo);
            ImageView myImage = (ImageView) getActivity().findViewById(R.id.photo2);
            myImage.setImageBitmap(myBitmap);

            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(500)
                    .cornerRadius(200) // 56 dp
                    .width(200) // 56 dp
                    .height(200) // 56 dp
                    .color(R.color.green) // normal state color
                    .colorPressed(R.color.green_teal) // pressed state color
                    .icon(R.drawable.ic_done); // icon

            btnMorphLogo.morph(circle);
            // Toast.makeText(getActivity(),imageFile.toString(),Toast.LENGTH_LONG).show();
         /*   if (imageFile.exists()) {

                myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                new MyLogoAsyncTask().execute(myBitmap);
            }*/
        }
    }

    private String getRealPathFromURINew(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("camera_debug", "CALLBACK");

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            Log.e("camera_debug", "CAMERA_CAPTURE_IMAGE_REQUEST_CODE");
            if (resultCode == getActivity().RESULT_OK) {
                Log.e("camera_debug", "RESULT_OK");
                // successfully captured the image
                // launching upload activity
                //  String tempPath = launchUploadActivity("image");


                performCameraSet();


            } else if (resultCode == getActivity().RESULT_CANCELED) {

                Log.e("camera_debug", "RESULT_CANCELED");
                // user cancelled Image capture
                //    Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();

            } else {
                Log.e("camera_debug", "else");
                // failed to capture image
                //  Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CHOOSE_IMAGE_GALLERY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Log.e("test_file", ":" + "Gallery Request");
                // successfully captured the image
                // launching upload activity
                //launchUploadActivity("image");
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, "image", getActivity());

                OutputStream out;

                File file = getOutputMediaFile(2);
                Log.e("test_file", "getParent:" + file.getParent());
                Log.e("test_file", "getAbsolutePath:" + file.getAbsolutePath());
                WifiFilehelper.copyFile(tempPath,file.getParent(),file.getAbsolutePath());

                //btnUploadPresciption.setVisibility(View.GONE);
                if(myPhototype.equals("image_own")) {

                    filePath = file.getAbsolutePath();//tempPath;
                    Log.e("test_file", "tempPath:" + filePath);
                    try {
                        ImageHelper.compressImage(filePath, getActivity());
                    }
                    catch (Exception e)
                    {
                        Log.e("test_file", "Error Compress:" + e.getLocalizedMessage());
                        e.fillInStackTrace();

                    }
                }
                else {
                    filePathLogo = file.getAbsolutePath();//tempPath;
                    Log.e("test_file", "tempPath:" + filePathLogo);
                    try {
                        ImageHelper.compressImage(filePathLogo, getActivity());
                    }
                    catch (Exception e)
                    {
                        Log.e("test_file", "Error Compress:" + e.getLocalizedMessage());
                        e.fillInStackTrace();

                    }
                }
                //  Log.e("test_file"                ,"//                Toast.makeText(getApplicationContext(), filePath, Toast.LENGTHSHORT).show();
                // compressImage(filePath);

                previewMedia("image");

//                Intent i = new Intent(Customer_now_prescription_activity.this, UploadPrescriptionActivity.class);
//                i.putExtra("image_or_video", "image");
//                i.putExtra("filePath", tempPath);
//                i.putExtra("upload_type", "prescription");
//                i.putExtra("order_now", true);
//                startActivityForResult(i, BACK_FROM_UPLOAD_PRESCRIPTION);


            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Log.e("test_file", ":" + "RESULT_CANCELED");
                // user cancelled Image capture
                //  Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT) .show()


            }
            else {
                /*EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                    @Override
                    public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                        //Some error handling
                    }


                    @Override
                    public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                        //Handle the image
                        // onPhotoReturned(imageFile);
                        if (type == 1) {
                            MorphingButton.Params circle = MorphingButton.Params.create()
                                    .duration(500)
                                    .cornerRadius(200) // 56 dp
                                    .width(200) // 56 dp
                                    .height(200) // 56 dp
                                    .color(R.color.green) // normal state color
                                    .colorPressed(R.color.green_teal) // pressed state color
                                    .icon(R.drawable.ic_done); // icon

                            btnMorph.morph(circle);
                            // Toast.makeText(getActivity(),imageFile.toString(),Toast.LENGTH_LONG).show();
                            if (imageFile.exists()) {

                                myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                                new MyAsyncTask().execute(myBitmap);
                            }
                        }
                        if (type == 3) {

                            //Toast.makeText(getActivity(),"hello again",Toast.LENGTH_LONG).show();
                            MorphingButton.Params circle = MorphingButton.Params.create()
                                    .duration(500)
                                    .cornerRadius(200) // 56 dp
                                    .width(200) // 56 dp
                                    .height(200) // 56 dp
                                    .color(R.color.green) // normal state color
                                    .colorPressed(R.color.green_teal) // pressed state color
                                    .icon(R.drawable.ic_done); // icon

                            btnMorphLogo.morph(circle);
                            // Toast.makeText(getActivity(),imageFile.toString(),Toast.LENGTH_LONG).show();
                            if (imageFile.exists()) {

                                myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                                new MyLogoAsyncTask().execute(myBitmap);
                            }
                        }
                    }


                });*/
            }
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    @SuppressLint("NewApi")
    public static String getPath(final Uri uri, final String type_img, Context context) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    private class MyAsyncTask extends AsyncTask<Bitmap, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Bitmap... myBitmap) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap[0].compress(Bitmap.CompressFormat.JPEG, 8, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("PHOTO", encodedImage);
                editor.commit();

                return encodedImage;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                ImageView myImage = (ImageView) getActivity().findViewById(R.id.photo1);

                myImage.setImageBitmap(myBitmap);
                progressBar.setVisibility(View.GONE);
            }
        }

        private class MyLogoAsyncTask extends AsyncTask<Bitmap, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Bitmap... myBitmap) {


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap[0].compress(Bitmap.CompressFormat.JPEG, 8, baos); //bm is the bitmap object

                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(PHOTO_COMPANY_LOGO, encodedImage);
                editor.commit();

                return encodedImage;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                ImageView myImage = (ImageView) getActivity().findViewById(R.id.photo2);

                myImage.setImageBitmap(myBitmap);

            }


        }


}
