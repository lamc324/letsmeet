package com.example.luism.letsmeet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ListOnline extends AppCompatActivity {

    //Firebase
    DatabaseReference onlineRef,currentUserRef,counterRef;
    FirebaseRecyclerAdapter<User,ListOnlineViewHolder> adapter;
    FirebaseRecyclerOptions<User> options;

    //View
    RecyclerView listOnline;
    RecyclerView.LayoutManager layoutManager;

    Query query = FirebaseDatabase.getInstance().getReference().child(".info/connected");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        //init view
        listOnline = (RecyclerView)findViewById(R.id.listOnline);
        listOnline.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOnline.setLayoutManager(layoutManager);

        //Set toolbar and Logout / Join menu
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitle("EDMT Presence System");
        setSupportActionBar(toolbar);

        //Firebase
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        setupSystem();
        //after setup system, we just load all user from counterRef and display on RecyclerView
        //This is online list
        updateList();

    }

    private void updateList() {
        options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(counterRef,User.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, ListOnlineViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ListOnlineViewHolder holder, int position, @NonNull User model) {
                holder.txtEmail.setText(model.getEmail());


            }

            @NonNull
            @Override
            public ListOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_layout,parent,false);
                return new ListOnlineViewHolder(view);
            }


        };
        adapter.notifyDataSetChanged();
        listOnline.setAdapter(adapter);
    }

    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class)){
                    currentUserRef.onDisconnect().removeValue();

                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser()
                            .getEmail(),"Online"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    User user = postSnapshot.getValue(User.class);
                    Log.d("LOG",""+user.getEmail()+" is "+user.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_join:
                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new User(FirebaseAuth.getInstance().getCurrentUser()
                                .getEmail(),"Online"));
                break;
            case R.id.action_logout:
                currentUserRef.removeValue();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
