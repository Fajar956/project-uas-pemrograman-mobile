package com.example.travelapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull; // ✅ Tambahkan ini
import com.example.travelapp.R;
import com.example.travelapp.adapter.DestinationAdapter;
import com.example.travelapp.model.Destination;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ExplorerActivity extends AppCompatActivity {

    private RecyclerView allDestinationsRecyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList = new ArrayList<>();
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        allDestinationsRecyclerView = findViewById(R.id.allDestinationsRecyclerView);
        adapter = new DestinationAdapter(destinationList);
        allDestinationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allDestinationsRecyclerView.setAdapter(adapter); // ✅ Akan jalan setelah import benar

        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        databaseRef = FirebaseDatabase.getInstance().getReference("destinations");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // ✅ @NonNull ditambahkan
                destinationList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Destination dest = ds.getValue(Destination.class);
                    if (dest != null) {
                        dest.id = ds.getKey();
                        destinationList.add(dest);
                    }
                }
                adapter.updateList(destinationList); // ✅ Method harus ada di adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Optional: show error message
            }
        });
    }
}