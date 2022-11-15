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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class DailyTimeTable extends Fragment {
    View view;
    String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String[] batch = {"1", "2", "3",};
    String[] division = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
    RecyclerView timetable;
    Button Search;
    MainModel[] data;
    String user,url="https://ea30-190-2-131-201.eu.ngrok.io";

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
        Spinner Batch = getView().findViewById(R.id.batch);
        Spinner Division = getView().findViewById(R.id.Division);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, day);
        Day.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, batch);
        Batch.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, division);
        Division.setAdapter(adapter2);
        timetable=getView().findViewById(R.id.timetable);
        timetable.setLayoutManager(new LinearLayoutManager(getContext()));
        Search = getView().findViewById(R.id.search);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectday = Day.getSelectedItem().toString();
                String selectdivision = Division.getSelectedItem().toString();
                String selectbatch = Batch.getSelectedItem().toString();
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
                        param.put("Division", selectdivision);
                        param.put("Batch", selectbatch);
                        return param;
                    }
                };
                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(request);
            }
        });


    }
}