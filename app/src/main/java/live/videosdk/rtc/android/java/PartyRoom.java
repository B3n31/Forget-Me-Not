package live.videosdk.rtc.android.java;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PartyRoom extends AppCompatActivity {
    private Button returnToCreateBtn;
    private FirebaseAuth mAuth;
    private ListView roomsList;
    private List<String> nameOfTheRooms;
    private ArrayAdapter adapter;
    private final String AUTH_TOKEN = BuildConfig.AUTH_TOKEN;
    private final String AUTH_URL = BuildConfig.AUTH_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_room_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Room List");

        returnToCreateBtn = findViewById(R.id.returnToMainButtonInAParty);
        roomsList = findViewById(R.id.friendsListView);
        mAuth = FirebaseAuth.getInstance();
        nameOfTheRooms = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameOfTheRooms);
        roomsList.setAdapter(adapter);
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String room = ds.getKey();
                    nameOfTheRooms.add(room);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore potential errors!
            }
        };
        itemsRef.addListenerForSingleValueEvent(eventListener);

        returnToCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PartyRoom.this, CreateOrJoinActivity.class);
                startActivity(i);
                finish();
            }
        });
        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String roomNumber = adapterView.getItemAtPosition(i).toString().trim();
                Toast.makeText(getApplicationContext(),"Clicked! " + roomNumber, Toast.LENGTH_SHORT).show();
                getToken(roomNumber);
            }
        });
    }
    private void getToken(@Nullable String meetingId) {
        if (!isNetworkAvailable()) {
            return;
        }

        if (!isNullOrEmpty(AUTH_TOKEN) && !isNullOrEmpty(AUTH_URL)) {
            Toast.makeText(PartyRoom.this,
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
                            Toast.makeText(PartyRoom.this,
                                    anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });

            return;
        }

        Toast.makeText(PartyRoom.this,
                "Please Provide auth_token or auth_url", Toast.LENGTH_SHORT).show();


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
    private void createMeeting(String token) {
        AndroidNetworking.post("https://api.videosdk.live/v1/meetings")
                .addHeaders("Authorization", token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String meetingId = response.getString("meetingId");

                            Intent intent = new Intent(PartyRoom.this, JoinActivity.class);
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
                        Toast.makeText(PartyRoom.this, anError.getMessage(),
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
                        Intent intent = new Intent(PartyRoom.this, JoinActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("meetingId", meetingId);

                        startActivity(intent);

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(PartyRoom.this, anError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
