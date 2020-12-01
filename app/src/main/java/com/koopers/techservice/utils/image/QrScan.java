package com.koopers.techservice.utils.image;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.activities.Detail_Activity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.OutputStream;
import java.util.Objects;

public class QrScan {
    private static MultiFormatWriter multiFormatWriter;
    private static BarcodeEncoder barcodeEncoder;
    private Context ctx;

    public QrScan(Context context){
        multiFormatWriter = new MultiFormatWriter();
        barcodeEncoder = new BarcodeEncoder();
        ctx = context;
    }

    public void CustomDialog(Equipo equipo, Boolean Show_Info){
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_click);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.TOP);
        dialog.getWindow().getAttributes().y = 250;

        ((TextView) dialog.findViewById(R.id.Title)).setText(String.valueOf(equipo.getModelo()));
        ((ImageView) dialog.findViewById(R.id.QrCode)).setImageBitmap(StringToQr(equipo.getId() + ""));

        dialog.findViewById(R.id.QrCode).setOnClickListener(v -> {
            Intent intent = new Intent(ctx, Detail_Activity.class);
            intent.putExtra("equipo", equipo);
            ctx.startActivity(intent);
        });

        dialog.findViewById(R.id.share).setOnClickListener(v -> {
            if(Show_Info)
                ShareImage(StringToQr(equipo.getId() + ""));
        });

        dialog.findViewById(R.id.info).setOnClickListener(v -> {
            Intent intent = new Intent(ctx, Detail_Activity.class);
            intent.putExtra("equipo", equipo);
            ctx.startActivity(intent);
        });

        if(!Show_Info)
            dialog.findViewById(R.id.info).setVisibility(View.GONE);

        dialog.show();
    }

    private void ShareImage(Bitmap Image){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = ctx.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outstream;
        try {
            assert uri != null;
            outstream = ctx.getContentResolver().openOutputStream(uri);
            Image.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            assert outstream != null;
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        ctx.startActivity(Intent.createChooser(share, "Share Image"));
    }

    public Bitmap StringToQr(String ToConv){
        try {
            return barcodeEncoder.createBitmap(multiFormatWriter.encode(ToConv,
                    BarcodeFormat.QR_CODE, 350, 350));
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
