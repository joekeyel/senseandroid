package my.com.tm.sense;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class generateqrcode extends AppCompatActivity {


    ImageView imageView;
    String activity,activityremark;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generateqrcode);


        Intent i = getIntent();
        activity = i.getStringExtra("activity");
        activityremark = i.getStringExtra("remarkactivity");




        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentFirebaseUser.getUid();
        String email = currentFirebaseUser.getEmail();


        final String query = email+","+ activity+","+ activityremark;

        imageView = (ImageView)findViewById(R.id.scanimage);
        Button selectimage = (Button)findViewById(R.id.findimage);

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectgallerywall1(query);
            }
        });


//straght generate code

        try {
            // generate a 150x150 QR code
            Bitmap bm = encodeAsBitmap(query);

            if(bm != null) {
                imageView.setImageBitmap(bm);
            }
        } catch (WriterException e) { //eek }

        }

    }



    public void selectgallerywall1(String queryitem) {



        try {
            // generate a 150x150 QR code
            Bitmap bm = encodeAsBitmap(queryitem);

            if(bm != null) {
                imageView.setImageBitmap(bm);
            }
        } catch (WriterException e) { //eek }

        }




    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 150, 150, null);
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
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 150, 0, 0, w, h);
        return bitmap;
    }

    private File getFile(String filename){


        File Folder = new File(Environment.getExternalStorageDirectory() +
                File.separator +"DCIM");

        if(!Folder.exists()){

            Folder.mkdir();
        }

        File image_file = new File(Folder,filename+".jpg");

        return image_file;


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }
}
