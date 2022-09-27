package com.example.cvs11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Adapter adapter_11 = new Adapter();
    Adapter adapter_21 = new Adapter();
    Adapter adapter_find;
    RecyclerView recyclerView;
    Button btn1, btn2, btn3, btn4;
    DataModel[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        String cTime = "";
        int cYear = 0;
        int cMonth = 0;
        int cDay = 0;

        for(int d=day ; d>=1 ; d--) {
            LocalDate tmp = LocalDate.of(year, month, d);
            if(d == 1 || tmp.getDayOfWeek().getValue() == 1) {
                day = d;
                break;
            }
        }

        Path file = Paths.get("/data/data/com.example.cvs11/files/res.json");
        try {
            FileTime creationTime = (FileTime) Files.getAttribute(file, "creationTime");
            cTime = creationTime.toString().substring(0, 10);
            String[] s = cTime.split("-");
            cYear = Integer.valueOf(s[0]);
            cMonth = Integer.valueOf(s[1]);
            cDay = Integer.valueOf(s[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(365*(year - cYear) + 30*(month - cMonth) + (day - cDay) > 0 && now.getHour() >= 1) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4x7eq8dm2f.execute-api.ap-northeast-2.amazonaws.com/getapi/").newBuilder();
            String url = urlBuilder.build().toString();
            Request.Builder builder = new Request.Builder().url(url)
                    .addHeader("x-api-key", "YourApiKey");
                    // Api Key를 이용해 연결
            Request req = builder.build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OutputStreamWriter os = new OutputStreamWriter(openFileOutput("res.json", Context.MODE_PRIVATE));
                                os.write(myResponse);
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

        try {
            InputStream in = openFileInput("res.json");
            byte[] b = new byte[in.available()];

            in.read(b);
            String s = new String(b);
            Gson gson = new Gson();
            data = gson.fromJson(s, DataModel[].class);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView)findViewById(R.id.items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        for(int i=0 ; i<8 ; i+=2) {
            for(int j=0 ; j<data[i].prod_list.length ; j++) {
                item_info item = new item_info();
                if(data[i].brand.equals("emart24"))
                    item.imageURL = data[i].prod_list[j].image.substring(0, 4) + data[i].prod_list[j].image.substring(5);
                else item.imageURL = data[i].prod_list[j].image;
                item.name = data[i].prod_list[j].name;
                item.price = data[i].prod_list[j].price;
                item.tag = data[i].brand + ' ' + data[i].type;
                adapter_11.setArrayData(item);
            }
        }

        for(int i=1 ; i<8 ; i+=2) {
            for(int j=0 ; j<data[i].prod_list.length ; j++) {
                item_info item = new item_info();
                if(data[i].brand.equals("emart24"))
                    item.imageURL = data[i].prod_list[j].image.substring(0, 4) + data[i].prod_list[j].image.substring(5);
                else item.imageURL = data[i].prod_list[j].image;
                item.name = data[i].prod_list[j].name;
                item.price = data[i].prod_list[j].price;
                item.tag = data[i].brand + ' ' + data[i].type;
                adapter_21.setArrayData(item);
            }
        }

        btn1 = (Button)findViewById(R.id.oneone);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter_11);
            }
        });

        btn2 = (Button)findViewById(R.id.twoone);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter_21);
            }
        });

        btn3 = (Button)findViewById(R.id.map);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        EditText find_name = (EditText)findViewById(R.id.prod_name);
        btn4 = (Button) findViewById(R.id.find);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter_find = new Adapter();
                for(int i=0 ; i<8 ; i++) {
                    for(int j=0 ; j<data[i].prod_list.length ; j++) {
                        if(data[i].prod_list[j].name.contains(find_name.getText()) == true) {
                            item_info item = new item_info();
                            if(data[i].brand.equals("emart24"))
                                item.imageURL = data[i].prod_list[j].image.substring(0, 4) + data[i].prod_list[j].image.substring(5);
                            else item.imageURL = data[i].prod_list[j].image;
                            item.name = data[i].prod_list[j].name;
                            item.price = data[i].prod_list[j].price;
                            item.tag = data[i].brand + ' ' + data[i].type;
                            adapter_find.setArrayData(item);
                        }
                    }
                }
                recyclerView.setAdapter(adapter_find);
            }
        });

    }
}
