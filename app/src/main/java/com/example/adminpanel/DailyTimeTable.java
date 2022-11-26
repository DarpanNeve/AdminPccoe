package com.example.adminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class DailyTimeTable extends Fragment {
    View view;
    String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    RecyclerView timetable;
    Button Search;
    MainModel[] data;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user,email,name,dummy;
    private final String url="https://3c28-103-151-234-62.in.ngrok.io";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily_time_table, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //To select the data to enter in recyclerview
        Spinner Day = requireView().findViewById(R.id.Day);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, day);
        Day.setAdapter(adapter);
        timetable=getView().findViewById(R.id.timetable);
        timetable.setLayoutManager(new LinearLayoutManager(getContext()));
        Search = getView().findViewById(R.id.search);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            email=firebaseUser.getEmail();
            dummy=email.replace("."," ");
            name=dummy.replace("@pccoepune.org","");

        }


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectday = Day.getSelectedItem().toString();
                StringRequest request = new StringRequest(Request.Method.POST, url+"/fetch_input.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builder=new GsonBuilder();
                        Gson gson= builder.create();
                        data =gson.fromJson(response,MainModel[].class);
                        user= gson.toJson(data);
                        if(user.length()!=2) {
                            DailyDataAdapter adapters = new DailyDataAdapter(data);
                            timetable.setAdapter(adapters);
                            Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "No Response Found", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("Day", selectday);
                        param.put("Name", name);
                        return param;
                    }
                };
                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(request);
            }
        });


    }
}