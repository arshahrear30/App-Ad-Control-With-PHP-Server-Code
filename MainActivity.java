package com.example.api;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;  // লোডিং দেখানোর জন্য প্রোগ্রেস বার
    AdView mAdView;           // ব্যানার বিজ্ঞাপন দেখানোর জন্য AdView

    public static boolean SHOW_AD = false;  // বিজ্ঞাপন দেখানো হবে কিনা তা কন্ট্রোল করার জন্য একটি ফ্ল্যাগ । আর
    // এটি public এ ব্যবহার করা হইছে যাতে user এর mb কম খরচ হয় এবং একবার লিখলেই সবজায়গায় কনট্রল হয় ।

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        mAdView = findViewById(R.id.adView);

        // অ্যাডমবস (AdMob) ইনিশিয়ালাইজ করা হচ্ছে
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // ইনিশিয়ালাইজ শেষ হলে যা হবে
            }
        });

        // ভলির জন্য একটি রিকোয়েস্ট কিউ তৈরি করা হচ্ছে
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.google.com";  // ডেমো URL (তুমি চাও তো পরবর্তীতে ব্যবহার করতে পারো)

        // ওয়েবসাইট থেকে রেসপন্স আনার জন্য ভলির স্ট্রিং রিকোয়েস্ট
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://txt.fyi/b17516148575cfdd",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // সার্ভার থেকে রেসপন্স পাওয়ার পর এই অংশে কোড এক্সিকিউট হবে

                        //যদি তোমার php file এর web page এ ShowAd লিখা থাকে শুধু । তাহলে Ad দেখাবে । আর অন্য কিছু লিখা থাকলে ad দেখাবে না ।
                        //দেখলে - App update দেওয়া ছাড়াই কি সুন্দরে app control করা যাচ্ছে server থেকে । এই জিনিসটাই এই code থেকে শিখবো ।

                        if (response.contains("ShowAd")) {
                            SHOW_AD = true;                         // ফ্ল্যাগ সেট করা হচ্ছে
                            mAdView.setVisibility(View.VISIBLE);    // বিজ্ঞাপন দেখা যাবে
                            AdRequest adRequest = new AdRequest.Builder().build(); // অ্যাড রিকোয়েস্ট বানানো
                            mAdView.loadAd(adRequest);              // অ্যাড লোড করা
                        } else {
                            SHOW_AD = true;                         // ফ্ল্যাগ সেট করা হচ্ছে
                            mAdView.setVisibility(View.GONE);       // বিজ্ঞাপন দেখা যাবে না
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // যদি কোনো সমস্যা হয় (যেমন ইন্টারনেট না থাকে), তাহলে এই অংশ চলবে
            }
        });
        // সিস্টেম বার (নিচের নেভিগেশন বার বা ওপরে স্ট্যাটাস বার) এর ইনসেট অ্যাডজাস্ট করা হচ্ছে
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // রিকোয়েস্ট কিউ-তে আমাদের তৈরি করা stringRequest যোগ করা হচ্ছে
        queue.add(stringRequest);
    }
}
