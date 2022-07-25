package live.videosdk.rtc.android.java;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import live.videosdk.rtc.android.java.Model.Users;

public class CreateOrJoinActivity extends AppCompatActivity {

    // Firebase
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    private final String AUTH_TOKEN = BuildConfig.AUTH_TOKEN;
    private final String AUTH_URL = BuildConfig.AUTH_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Forget Me Not");

        setSupportActionBar(toolbar);

        isNetworkAvailable();

        final Button btnCreate = findViewById(R.id.btnCreateMeeting);

        final Button friendBut = findViewById(R.id.Friend_Button);
        final Button returnToMain = findViewById(R.id.returnToMainButtonInAParty);


        btnCreate.setOnClickListener(v -> {
            getToken(null);
        });
        friendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateOrJoinActivity.this, PartyRoom.class);
                startActivity(i);
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Users users = datasnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = networkInfo != null && networkInfo.isConnected();

        if (!isAvailable) {
            Snackbar.make(findViewById(R.id.layout), "No Internet Connection",
                    Snackbar.LENGTH_LONG).show();
        }
        return isAvailable;
    }

    private boolean isNullOrEmpty(String str) {
        return "null".equals(str) || "".equals(str) || null == str;
    }

    private void getToken(@Nullable String meetingId) {
        if (!isNetworkAvailable()) {
            return;
        }

        if (!isNullOrEmpty(AUTH_TOKEN) && !isNullOrEmpty(AUTH_URL)) {
            Toast.makeText(CreateOrJoinActivity.this,
                    "Please Provide only one - either auth_token or auth_url",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNullOrEmpty(AUTH_TOKEN)) {
            if (meetingId == null) {
                createMeeting(AUTH_TOKEN);
            } else {
                joinMeeting(AUTH_TOKEN, meetingId);
            }
            return;
        }

        if (!isNullOrEmpty(AUTH_URL)) {
            AndroidNetworking.get(AUTH_URL + "/get-token")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("token");
                                if (meetingId == null) {
                                    createMeeting(token);
                                } else {
                                    joinMeeting(token, meetingId);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            Toast.makeText(CreateOrJoinActivity.this,
                                    anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
            return;
        }

        Toast.makeText(CreateOrJoinActivity.this,
                "Please Provide auth_token or auth_url", Toast.LENGTH_SHORT).show();
    }


    private void createMeeting(String token) {
        AndroidNetworking.post("https://api.videosdk.live/v1/meetings")
                .addHeaders("Authorization", token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String meetingId = response.getString("meetingId");

                            Intent intent = new Intent(CreateOrJoinActivity.this, JoinActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("meetingId", meetingId);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(CreateOrJoinActivity.this, anError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void joinMeeting(String token, String meetingId) {
        AndroidNetworking.post("https://api.videosdk.live/v1/meetings/" + meetingId)
                .addHeaders("Authorization", token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(CreateOrJoinActivity.this, JoinActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("meetingId", meetingId);

                        startActivity(intent);

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(CreateOrJoinActivity.this, anError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Adding Logout Functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateOrJoinActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }

}