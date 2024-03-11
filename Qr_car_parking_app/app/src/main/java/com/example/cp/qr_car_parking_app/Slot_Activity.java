package com.example.cp.qr_car_parking_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;
import com.example.cp.qr_car_parking_app.Data.ImageLoadTask;
import com.example.cp.qr_car_parking_app.Data.Slot_info;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Slot_Activity extends AppCompatActivity {

    Dialog dg;
    int resp;
    String imgpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_);
        TextView txtArea = (TextView) findViewById(R.id.txtAreaName);
        TextView txtSlotNo = (TextView) findViewById(R.id.txtSlotNumber);
        txtArea.setText(Slot_info.getAreaName());
        txtSlotNo.setText(Slot_info.getSlotNO());
        ImageView img = (ImageView) findViewById(R.id.imageView2);
        if (!Slot_info.getSloturl().equals("")) {
            imgpath = Slot_info.getSloturl().replace("~","http://my-demo.in/QR_car_parking_SAKEC_web");
            new ImageLoadTask(imgpath, img);

            new ImageLoadTask(imgpath, img).execute();
        }




        Button btnBook = (Button) findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Slot_Activity.this)
                        .setIcon(R.drawable.ic_dialog_icon1)
                        .setTitle(R.string.app_name)
                        .setMessage("Book Slot?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BookSlot();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    public void BookSlot() {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            Progressdialog dialog = new Progressdialog();
            dg = dialog.createDialog(this);
            dg.show();

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.Book_Slot()) {
                            resp = 0;
                        } else {
                            resp = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hd.sendEmptyMessage(0);
                }
            };
            th1.start();
        } else {
            Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    final Dialog qrDislog = new Dialog(Slot_Activity.this, R.style.FullHeightDialog);
                    qrDislog.setContentView(R.layout.custom_alert_imgv);
                    qrDislog.setCancelable(true);

                    ImageView imageView = (ImageView) qrDislog.findViewById(R.id.custom_img_qr);
                    String str = Slot_info.getBookingId();
                    try {
                        Bitmap bm = encodeAsBitmap(str);
                        imageView.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Button updateButton = (Button) qrDislog.findViewById(R.id.rank_dialog_button);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qrDislog.dismiss();
                            finish();
                        }
                    });
                    //now that the dialog is set up, it's time to show it
                    qrDislog.show();
                    Toast.makeText(getApplicationContext(), " Slot Booked Successfully", Toast.LENGTH_LONG).show();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "you already booked one slot if no then Try Again...", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;

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
