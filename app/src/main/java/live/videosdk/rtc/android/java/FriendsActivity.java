package live.videosdk.rtc.android.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {
    Button returnToCreateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        returnToCreateBtn = findViewById(R.id.returnToMainButton);
        returnToCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendsActivity.this, CreateOrJoinActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
