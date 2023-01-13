package com.Pccoe.Teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeeklyTImeTable extends AppCompatActivity {
    String[] division = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
    Button search,export;
    private final int REQUESTCODE=100;
    RecyclerView mondaydata ,tuesdaydata,wednesdaydata,thursdaydata,fridaydata;
    Bitmap bitmap, ImageBitmap,scaledImageBitmap;
    LinearLayout layout;
    String selectdivision,user;
    ScrollView timetableview;
    private final String url ="https://d291-103-151-234-92.in.ngrok.io";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_time_table);
        Spinner Division = findViewById(R.id.Division);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, division);
        Division.setAdapter(adapter2);
        search = findViewById(R.id.search);
        mondaydata = findViewById(R.id.Mondaydata);
        mondaydata.setLayoutManager(new LinearLayoutManager(WeeklyTImeTable.this, LinearLayoutManager.HORIZONTAL, false));
        tuesdaydata = findViewById(R.id.Tuesdaydata);
        tuesdaydata.setLayoutManager(new LinearLayoutManager(WeeklyTImeTable.this, LinearLayoutManager.HORIZONTAL, false));
        wednesdaydata = findViewById(R.id.Wednesdaydata);
        wednesdaydata.setLayoutManager(new LinearLayoutManager(WeeklyTImeTable.this, LinearLayoutManager.HORIZONTAL, false));
        thursdaydata = findViewById(R.id.Thursdaydata);
        thursdaydata.setLayoutManager(new LinearLayoutManager(WeeklyTImeTable.this, LinearLayoutManager.HORIZONTAL, false));
        fridaydata = findViewById(R.id.Fridaydata);
        fridaydata.setLayoutManager(new LinearLayoutManager(WeeklyTImeTable.this, LinearLayoutManager.HORIZONTAL, false));
        layout=findViewById(R.id.layout);
        timetableview=findViewById(R.id.timetableview);
        timetableview.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fetching data of monday
                // refer github for raw code
                //raw api fetching

                selectdivision = Division.getSelectedItem().toString();
                //calling method to show data in recyclerview
                showdata("Monday",mondaydata,selectdivision);
                showdata("Tuesday",tuesdaydata,selectdivision);
                showdata("Wednesday",wednesdaydata,selectdivision);
                showdata("Thursday",thursdaydata,selectdivision);
                showdata("Friday",fridaydata,selectdivision);
                timetableview.setVisibility(View.VISIBLE);
            }
        });
        export();
    }
    private void showdata(String selectday,RecyclerView RV,String selectdivision) {
        StringRequest request = new StringRequest(Request.Method.POST, url + "/fetch_Data.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                MainModel[] data = gson.fromJson(response, MainModel[].class);
                user= gson.toJson(data);
                if(user.length()!=2) {
                    WeeklyDataAdapter adapters = new WeeklyDataAdapter(data);
                    RV.setAdapter(adapters);
                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Response Found", Toast.LENGTH_SHORT).show();

                }
               
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Division", selectdivision);
                param.put("Day", selectday);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(WeeklyTImeTable.this);
        queue.add(request);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showDialogForPermission() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTCODE);
            }
        }
        if (Build.VERSION.SDK_INT>=Build. VERSION_CODES. R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android. intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityIfNeeded(intent, REQUESTCODE);
                } catch (Exception exception) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityIfNeeded(intent, REQUESTCODE);
                }
            }
        }
        if(ContextCompat.checkSelfPermission(WeeklyTImeTable.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            try {
                Createpdf(selectdivision);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUESTCODE) {
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted Done", Toast.LENGTH_SHORT).show();
                try {
                    Createpdf(selectdivision);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                showDialogForPermission();
                Toast.makeText(this, "Permission Denied ", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void export() {
        export=findViewById(R.id.Export);
        export.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api=Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                Log.d("size",""+layout.getWidth()+""+layout.getWidth());
                bitmap=LoadBitmap(layout,layout.getWidth(),layout.getHeight());
                ImageBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.time_table);
                scaledImageBitmap=Bitmap.createScaledBitmap(ImageBitmap,2526,1785,false);
                Toast.makeText(WeeklyTImeTable.this, ""+layout.getWidth()+","+layout.getWidth(), Toast.LENGTH_SHORT).show();
                showDialogForPermission();
            }
        });
    }
    private Bitmap LoadBitmap(LinearLayout layout, int width, int height) {
        Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }
    @SuppressLint("ResourceAsColor")
    private void Createpdf(String Division) throws FileNotFoundException {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2526, 1785, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, 2092, 1048, true);
        canvas.drawBitmap(scaledImageBitmap,0,0,null);
        canvas.drawBitmap(bitmap,216,443,null);

        pdfDocument.finishPage ( page ) ;



        File file = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/"+selectdivision+" Division weekly timetable"+".pdf") ;
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

}