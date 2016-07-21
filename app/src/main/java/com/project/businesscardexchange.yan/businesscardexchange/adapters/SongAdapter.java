package com.project.businesscardexchange.yan.businesscardexchange.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCard;

import java.util.Collections;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>  {
    SongViewHolder songViewHolder;
    List<BusinessCard> cardList= Collections.emptyList();
    Context context;

    Bitmap decodedBitmap;
    Bitmap decodedBitmap1;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public SongAdapter(List<BusinessCard> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }


    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int setDesign = 1;//This for my design
      //  int setDesign =2; //This for Backgroung Image design
        View view=null;
        if(setDesign ==1)
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bcard_row_new, parent, false);
        }
        else if(setDesign ==2)
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bcard_row_new_photo_bg, parent, false);

        }
        else
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bcard_row_new, parent, false);

        }
        return new SongViewHolder(context,view);
    }



    public void removeItem(int position){
        cardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,cardList.size());
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
       // holder.itemView.setSelected(position == position);
       /*
       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                Log.e("PositionTest2",""+holder.getPosition());
                return false;
            }
        });
        */

        BusinessCard song = cardList.get(position);
        holder.name.setText(song.getName());
        holder.companyName.setText(song.getCompanyName());
        holder.phone.setText("Comp: "+PhoneNumberUtils.formatNumber(song.getPhone()));
        holder.email.setText(song.getEmailAddress());
        holder.email.setPaintFlags(holder.email.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        holder.website.setText(song.getWebsiteUrl());
        holder.website.setPaintFlags(holder.website.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.directPhone.setText("Cell: "+PhoneNumberUtils.formatNumber(song.getDirectPhone()));
        holder.post.setText(song.getPost());
        holder.street.setText(song.getStreet());
        //holder.state.setText(song.getState());
        holder.city.setText(song.getCity()+", "+song.getState()+" "+song.getZipCode());
       // holder.zipCode.setText(song.getZipCode());
        if(song.getCountryName()!= null && !song.getCountryName().trim().equals("") )
        {
            holder.bcard_country_name.setText(song.getCountryName());
            holder.bcard_country_name.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.bcard_country_name.setVisibility(View.GONE);
        }


        //needed afterwards , just commented for testing
         String encodedImage = song.getPhoto();
         String encodedImageLogo = song.getPhotocompanylogo();

        try
        {
            // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            decodedBitmap = BitmapFactory.decodeFile(encodedImage);//BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedBitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            //byte[] decode = Base64.decode(encodedImageLogo, Base64.DEFAULT);
            decodedBitmap1 = BitmapFactory.decodeFile(encodedImageLogo);//BitmapFactory.decodeByteArray(decode, 0, decode.length);
            holder.imageView2.setImageBitmap(decodedBitmap1);
        }catch (Exception e){
            e.printStackTrace();
        }



        //  songViewHolder = holder;
        // new MyAsyncTask().execute(encodedImage);

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,companyName,phone,email,website,directPhone,post, street,city,state, zipCode,bcard_country_name;
        ImageView imageView;
        ImageView imageView2;
        private Context context;
        RelativeLayout address_section;
        private String url;


        public SongViewHolder(final Context context, View itemView) {
            super(itemView);
            this.context=context;
            name = (TextView) itemView.findViewById(R.id.bcard_name);
            companyName = (TextView) itemView.findViewById(R.id.bcard_comapny_name);
            phone = (TextView) itemView.findViewById(R.id.bcard_phone);
            email = (TextView) itemView.findViewById(R.id.bcard_email);
            website = (TextView) itemView.findViewById(R.id.bcard_website);
            directPhone = (TextView)itemView.findViewById(R.id.bcard_direct_phone);
            imageView = (ImageView)itemView.findViewById(R.id.image_test_for_business_cards);
            post=(TextView)itemView.findViewById(R.id.bcard_comapny_post);
            street = (TextView)itemView.findViewById(R.id.bcard_street);
            city = (TextView)itemView.findViewById(R.id.bcard_city);
            state = (TextView)itemView.findViewById(R.id.bcard_state);
            zipCode = (TextView)itemView.findViewById(R.id.bcard_zip_code);
            imageView2 = (ImageView)itemView.findViewById(R.id.bcard_logo);
            address_section= (RelativeLayout) itemView.findViewById(R.id.address_section);
            bcard_country_name = (TextView)itemView.findViewById(R.id.bcard_country_name);
            itemView.setOnClickListener(this);
            address_section.setOnClickListener(new View.OnClickListener() {
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
                    context.startActivity(intent);
                }
            });
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString().substring(5)));
                    context.startActivity(intent);
                }
            });

            //intent to dial direct line
            directPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + directPhone.getText().toString().substring(5)));
                    context.startActivity(intent);
                }
            });

            //intent to send email
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", email.getText().toString(), null));
                    context.startActivity(intent);
                }
            });


            //intent to go to the website
            url=website.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);


                }
            });

        }



        @Override
        public void onClick(View view)
        {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //intent to dial company line

        }


       /* @Override
        public boolean onLongClick(View v) {


            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imageView.getParent().requestDisallowInterceptTouchEvent(false);

                    return false;
                }
            });
            phone.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    phone.getParent().requestDisallowInterceptTouchEvent(false);

                    return false;
                }
            });
            address_section.setOnLongClickListener(null);
            //intent to dial direct line
            directPhone.setOnLongClickListener(null);
            //intent to send email
            email.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    email.getParent().requestDisallowInterceptTouchEvent(false);

                    return false;
                }
            });

            //intent to go to the website
            website.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    website.getParent().requestDisallowInterceptTouchEvent(false);

                    return false;
                }
            });

            return false;
        }*/
    }
    @Override
    public void onViewRecycled(SongViewHolder holder) {
       // holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... encodedImage) {

            return decodedBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            songViewHolder.imageView.setImageBitmap(bitmap);

        }
    }
}