package com.example.joan.brainallydiary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AdaptadorSpnEmocions extends ArrayAdapter<String>{

    private Context context;
    private String[] emocions;
    private int[] img_emocions;

    public AdaptadorSpnEmocions(Context context,String[] emocions, int[] img) {
        super(context, R.layout.item_emocio_spn,emocions);
        this.context = context;
        this.emocions = emocions;
        this.img_emocions = img;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_emocio_spn,null);
        //TextView emocio = row.findViewById(R.id.item_emocio_spn_text);
        ImageView img_emocio = row.findViewById(R.id.item_emocio_spn);

        //emocio.setText(emocions[position]);
        //img_emocio.setImageResource(img_emocions[position]);
        Glide.with(context).load(img_emocions[position]).into(img_emocio);
        //img_emocio.setImageBitmap(decodeSampledBitmapFromResource(row.getResources(), img_emocions[position], 50, 50));


        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_emocio_spn,null);
        //TextView emocio = row.findViewById(R.id.item_emocio_spn_text);
        ImageView img_emocio = row.findViewById(R.id.item_emocio_spn);

        //emocio.setText(emocions[position]);
        //img_emocio.setImageResource(img_emocions[position]);
        Glide.with(context).load(img_emocions[position]).into(img_emocio);
        //img_emocio.setImageBitmap(decodeSampledBitmapFromResource(row.getResources(), img_emocions[position], 50, 50));

        return row;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
