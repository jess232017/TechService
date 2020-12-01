package com.koopers.techservice.ui.adapters;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;

import java.io.OutputStream;
import java.util.List;

public class MyHistorial extends RecyclerView.Adapter<MyHistorial.ViewHolder> {
    private static MultiFormatWriter multiFormatWriter;
    private static BarcodeEncoder barcodeEncoder;
    private List<Equipo> mValues;
    private Context ctx;

    public MyHistorial(Context ctx, List<Equipo> equipoList) {
        multiFormatWriter = new MultiFormatWriter();
        barcodeEncoder = new BarcodeEncoder();
        mValues = equipoList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.titleTextView.setText(holder.mItem.getModelo());
        holder.contentTextView.setText(holder.mItem.getObservacion());
        holder.idButton.setOnClickListener(v -> CustomDialog(holder.mItem));
        holder.idButton.setText(String.valueOf(holder.mItem.getId()));
    }

    private void CustomDialog(Equipo equipo){
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_click);

        ((TextView) dialog.findViewById(R.id.Title)).setText(String.valueOf(equipo.getModelo()));
        ((ImageView) dialog.findViewById(R.id.QrCode)).setImageBitmap(StringToQr(equipo.getId() + ""));

        dialog.findViewById(R.id.share).setOnClickListener(v ->
                ShareImage(StringToQr(equipo.getId() + "")));

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

    private Bitmap StringToQr(String ToConv){
        try {
            return barcodeEncoder.createBitmap(multiFormatWriter.encode(ToConv,
                    BarcodeFormat.QR_CODE, 350, 350));
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setData(List<Equipo> equipos){
        this.mValues = equipos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTextView;
        private TextView titleTextView;
        private Button idButton;
        private Equipo mItem;

        private ViewHolder(View itemView) {
            super(itemView);
            idButton = itemView.findViewById(R.id.idButton);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}
