package com.example.cp.qr_car_parking_app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.cp.qr_car_parking_app.Data.Booking_details;
import com.example.cp.qr_car_parking_app.Data.ImageLoadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

public class View_QR extends AppCompatActivity {

    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__qr);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String str = Booking_details.getBookingId();
        try {
            Bitmap bm = encodeAsBitmap(str);
            imageView.setImageBitmap(bm);
         } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imgPath=(ImageView)findViewById(R.id.imgBookPath);
        new ImageLoadTask(Booking_details.getImgPath(), imgPath);

        new ImageLoadTask(Booking_details.getImgPath(), imgPath).execute();

    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }

}
