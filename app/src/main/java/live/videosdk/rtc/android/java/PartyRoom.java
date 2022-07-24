package live.videosdk.rtc.android.java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PartyRoom extends AppCompatActivity {
    private Button returnToCreateBtn;
    private FirebaseAuth mAuth;
    private ListView roomsList;
    private List<String> nameOfTheRooms;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_room_layout);
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
    }


}
