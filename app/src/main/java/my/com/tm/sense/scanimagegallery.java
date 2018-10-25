package my.com.tm.sense;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class scanimagegallery extends Fragment {


    ImageView imageView;
    View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scanimagegallery, container, false);

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scanimagegallery);
//
//
//        imageView = (ImageView)findViewById(R.id.scanimage);
//
//
//    }


        imageView = (ImageView)myView.findViewById(R.id.scanimage);
        Button selectimage = (Button)myView.findViewById(R.id.findimage);

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectgallerywall1();
            }
        });

        return myView;
    }

    public void selectgallerywall1() {




            // Perform action on click
            Intent camera_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            File file = getFile("scanimage");

            Uri apkURI = FileProvider.getUriForFile(
                    scanimagegallery.this.getActivity(),
                    scanimagegallery.this.getActivity()
                            .getPackageName() + ".provider", file);


            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                    //photoURI
                    apkURI

            );


            startActivityForResult(camera_intent, 1);


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data != null) {
            Uri contentURI = data.getData();


            final String path = Environment.getExternalStorageDirectory() +
                    File.separator + "DCIM/scanimage.jpg";

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error image Galery", e.toString());
            }
            Bitmap photo = Bitmap.createScaledBitmap(bmp, 1280, 1024, true);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "DCIM/scanimage.jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int[] intArray = new int[bmp.getWidth()*bmp.getHeight()];
//copy pixel data from the Bitmap into the 'intArray' array
            bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bmp.getWidth(), bmp.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(bitmap);
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }

            if(result != null) {
                String contents = result.getText();

                Toast toast = Toast.makeText(getActivity(), contents, Toast.LENGTH_SHORT);
                toast.show();

                imageView.setImageDrawable(Drawable.createFromPath(path));

                String scantexttr = result.getText();
                Intent nextpage = new Intent(getActivity(),selectactivityrate.class);
                nextpage.putExtra("employeename",scantexttr);
                startActivity(nextpage);

            }
            else{

                Toast toast = Toast.makeText(getActivity(), "Image is not readable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
}
