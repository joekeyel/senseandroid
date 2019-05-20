package my.com.tm.sense;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class searchemployee extends Fragment implements ListView.OnItemClickListener{


    private ProgressDialog loading;

    private ListView listView;
    EditText editext;
    Button btnsearch,back;
    SearchView searchView;
    View myView;
    private String JSON_STRING;
    private String JSON_RESULT;
    private AlertDialog.Builder alertDialogform;
    AlertDialog alertform = null;

    private ArrayList<employeemodel> employeelist = new ArrayList<>();
    private String activity,activityremark;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.search_employee_layout, container, false);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();

        registerUser(uid,email);




        listView = (ListView) myView.findViewById(R.id.list);

        listView.setOnItemClickListener(this);

        back = (Button) myView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),scanqractivity.class));
//                Fragment fragment = new Ipmsansite();
//
//
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();

            }
        });

        searchView = (SearchView)myView.findViewById(R.id.searchemployee);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();

//               if(query.length()>3){
                Log.e("queryText",query);
              // }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!
                // listAdapter.getFilter().filter(query);
                if(newText.length()>3) {
                    Log.e("queryTextSubmit", newText);

                }
                return false;
            }
        });


        getJSON(email);


        btnsearch = (Button)myView.findViewById(R.id.searchbutton);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchname = String.valueOf(searchView.getQuery());

                Log.e("queryTextSubmitButton", searchname);

                if(searchname.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(), "Pls enter employee name", Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    getJSON(searchname);

                }
            }
        });


        Button scanimagegalery = (Button)myView.findViewById(R.id.scangallery);
        scanimagegalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectgallerywall1();
            }
        });

        if (getArguments() != null) {

            Toast toast = Toast.makeText(getActivity(), getArguments().getString("email"), Toast.LENGTH_SHORT);
            toast.show();

            String scantextqr = getArguments().getString("email");
            Log.e("scantext",getArguments().getString("email"));






        }



        return myView;
    }


    private void showEmployee(){
        JSONObject jsonObject = null;

        ArrayList<employeemodel> employeelist = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("employeelist");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String a = jo.getString("employeename");
                String b = jo.getString("email");
                String c = jo.getString("staffid");
                String d = jo.getString("division");
                String e = jo.getString("uid");





                employeemodel employeeeobject = new employeemodel();
                employeeeobject.setName(a);
                employeeeobject.setEmail(b);
                employeeeobject.setDivision(d);
                employeeeobject.setStaffid(c);
                employeeeobject.setUid(e);



                employeelist.add(employeeeobject);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        ListAdapter adapter = new SimpleAdapter(
//                getActivity(), list, R.layout.totalpstnsite,
//                new String[]{"STATE","total"},
//
//                new int[]{R.id.satu, R.id.dua});

        employeeadaptor adapter2 =  new employeeadaptor(getActivity(),R.layout.employee_row,employeelist);


        listView.setAdapter(adapter2);



    }


    private void getJSON(final String query){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              loading = ProgressDialog.show(getActivity(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   loading.dismiss();
                //   loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
                JSON_STRING = s;
                //  showData();
                showEmployee();
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendGetRequest(Config.URL_EMPLOYEE+"?query="+query);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void registerUser(final String uid, final String email){


        class GetJSON extends AsyncTask<String,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(),"Loading Data","Wait...",false,false);

                // loading = ProgressDialog.show(getActivity(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   loading.dismiss();
                //   loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
                JSON_RESULT = s;

                if(!s.isEmpty()){
//                Toast toast1 = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
//                toast1.show();



                try {
                    JSONObject jsonreturn = new JSONObject(s);

                    String result = jsonreturn.getString("result");
//
//                    Toast toast1 = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
//                    toast1.show();


                    if(result.equals("user not exist")){
                        showdialogbox();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();

                }

                }
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> map = new HashMap<>();

                map.put("uid",params[0]);
                map.put("email",params[1]);

                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendPostRequest(Config.URL_EMPLOYEEUPDATE,map);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(uid,email);
    }


    private void insertuser(final String employeename,String division,String staffid,String email,String uid){


        class insertusertask extends AsyncTask<String,Void,String> {

            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_RESULT = s;

                if(!s.isEmpty()){



                    try {
                        JSONObject jsonreturn = new JSONObject(s);

                        String result = jsonreturn.getString("result");

                    Toast toast1 = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
                    toast1.show();


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();

                    }

                }
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> map = new HashMap<>();

                map.put("uid",params[4]);
                map.put("email",params[3]);
                map.put("division",params[1]);
                map.put("staffid",params[2]);
                map.put("employeename",params[0]);

                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendPostRequest(Config.URL_EMPLOYEEINSERT,map);
                return s;
            }
        }
        insertusertask gj = new insertusertask();
        gj.execute(employeename,division,staffid,email,uid);
    }


    private void showdialogbox() {


        alertDialogform = new AlertDialog.Builder(getActivity());

        alertform = alertDialogform.create();

        alertform.setTitle("Insert Your Info");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.userinfoformdialog, null);
        // find the ListView in the popup layout


       final EditText name = (EditText)convertView.findViewById(R.id.nameet);
        final EditText division = (EditText)convertView.findViewById(R.id.divisionet);
        final EditText staffid = (EditText)convertView.findViewById(R.id.staffidet);

        Button updatebutton = (Button)convertView.findViewById(R.id.updatebutton);




        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String namestr = name.getText().toString();
                final String divisionstr = division.getText().toString();
                final String staffidstr = staffid.getText().toString();

                if(namestr.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(), "Please insert Name", Toast.LENGTH_SHORT);
                    toast.show();

                }
                if(divisionstr.isEmpty()){

                    Toast toast = Toast.makeText(getActivity(), "Please insert Division", Toast.LENGTH_SHORT);
                    toast.show();

                }
                if(staffidstr.isEmpty())

                {

                    Toast toast = Toast.makeText(getActivity(), "Please insert Staffid", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(!staffidstr.isEmpty() && !divisionstr.isEmpty() && !namestr.isEmpty())

                    {

                    Toast toast = Toast.makeText(getActivity(), "Submitted", Toast.LENGTH_SHORT);
                    toast.show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();

                    insertuser(namestr,divisionstr,staffidstr,email,uid);
                    alerthide();

                }


            }
        });



        alertform.setView(convertView);
        alertform.setCanceledOnTouchOutside(false);
        alertform.setCancelable(false);


            alertform.show();


    }


    public void alerthide(){

        alertform.dismiss();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




        employeemodel obj = (employeemodel) listView.getAdapter().getItem(position);

        String empId = obj.getName();
        String emailselected = obj.getEmail();
        String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();




//        Bundle args = new Bundle();
//        args.putString("employeename", empId);
//        fragment1.setArguments(args);

        Context context = getActivity();
        CharSequence text = empId;
        int duration = Toast.LENGTH_SHORT;


//
//        if(fragment1 != null){
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment1);
//            ft.commit();
//        }


        if(!emailselected.equals(useremail)) {

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent nextpage = new Intent(getActivity(), selectactivityrate.class);
            nextpage.putExtra("employeename", empId);
            nextpage.putExtra("division",obj.getDivision());
            nextpage.putExtra("staffid",obj.getStaffid());
            nextpage.putExtra("email",obj.getEmail());
            startActivity(nextpage);
        }else{
            Toast toast2 = Toast.makeText(context, "You cannot rate yourself", duration);
            toast2.show();

        }
     //   intent.putExtra("STATE", empId);




    }




    public void selectgallerywall1() {


        requestPermissions(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                3);




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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {



        switch (requestCode) {


            case 3:{

                // Perform action on click
                Intent camera_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                File file = getFile("scanimage");

                Uri apkURI = FileProvider.getUriForFile(
                        searchemployee.this.getActivity(),
                        searchemployee.this.getActivity()
                                .getPackageName() + ".provider", file);


                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                        //photoURI
                        apkURI

                );


                startActivityForResult(camera_intent, 1);
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
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

//                Toast toast = Toast.makeText(getActivity(), contents, Toast.LENGTH_SHORT);
//                toast.show();


                String scantexttr = result.getText();

                List<String> qrlist = Arrays.asList(scantexttr.split(","));

                // ArrayList<String> qrlist = (ArrayList<String>)Arrays.asList(scantexttr.split(","));

                if(qrlist.size() == 3) {
                    String email = qrlist.get(0);
                    activity = qrlist.get(1);
                    activityremark = qrlist.get(2);


                    //  Toast.makeText(getActivity(), scantexttr, Toast.LENGTH_SHORT).show();

                    getJSON2(email);
                }else{

                    Toast toast = Toast.makeText(getActivity(), "Image is not readable", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else{

                Toast toast = Toast.makeText(getActivity(), "Image is not readable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }

    private void getJSON2(final String query){
        class GetJSON extends AsyncTask<Void,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getBaseContext(),"Loading Data","Wait...",false,false);
                // loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   loading.dismiss();
                //   loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dashb));
                JSON_STRING = s;
                //  showData();
                showEmployee2();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendGetRequest(Config.URL_EMPLOYEE+"?query="+query);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showEmployee2(){



        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("employeelist");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String a = jo.getString("employeename");
                String b = jo.getString("email");
                String c = jo.getString("staffid");
                String d = jo.getString("division");
                String e = jo.getString("uid");





                employeemodel employeeeobject = new employeemodel();
                employeeeobject.setName(a);
                employeeeobject.setEmail(b);
                employeeeobject.setDivision(d);
                employeeeobject.setStaffid(c);
                employeeeobject.setUid(e);



                employeelist.add(employeeeobject);


            }

            if(employeelist.size()>0) {


                if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(employeelist.get(0).getEmail())){

                    Intent nextpage = new Intent(getActivity(), rateactivity.class);
                    nextpage.putExtra("employeename", employeelist.get(0).getName());
                    nextpage.putExtra("email", employeelist.get(0).getEmail());
                    nextpage.putExtra("division", employeelist.get(0).getDivision());
                    nextpage.putExtra("staffid", employeelist.get(0).getStaffid());
                    nextpage.putExtra("division", employeelist.get(0).getDivision());
                    nextpage.putExtra("staffid", employeelist.get(0).getStaffid());
                    nextpage.putExtra("activity", activity);
                    nextpage.putExtra("remarkactivity", activityremark);


                    startActivity(nextpage);
                }
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(employeelist.get(0).getEmail())){

                    Toast.makeText(getActivity(), "U Cannot Rate Yourself", Toast.LENGTH_SHORT).show();

                }


            }
            else
            {

                Toast.makeText(getActivity(), "User Not Found", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main,menu);



//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//
//
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
    }
}
