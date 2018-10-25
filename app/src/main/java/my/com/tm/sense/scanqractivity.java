package my.com.tm.sense;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanqractivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String FLASH_STATE = "FLASH_STATE";

    private ZXingScannerView mScannerView;
    private boolean mFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqractivity);


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
        mScannerView.setAspectTolerance(0.2f);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }


    @Override
    public void handleResult(Result result) {
        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

//        String scantext = result.getText();
//                   Fragment fragment = new searchemployee();
//
//                Bundle args = new Bundle();
//               args.putString("email", scantext);
//               fragment.setArguments(args);
//
//                    FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//                    ft.remove(fragment);
//                    ft.replace(R.id.content_frame, fragment);
//
//                    ft.commit();

        String scantexttr = result.getText();
        Intent nextpage = new Intent(getApplicationContext(),selectactivityrate.class);
        nextpage.putExtra("employeename",scantexttr);
        startActivity(nextpage);


        mScannerView.stopCamera();
        finish();


    }
}
