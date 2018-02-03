package com.example.oways.firebaserealtimedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";
    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;
    ListView listViewArtists;
    List<Artist> artists;
    DatabaseReference databaseArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtists);
        artists = new ArrayList<>();

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artists.get(position);
                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());
                startActivity(intent);
            }
        });

        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artists.get(position);
                showUpdateDialog(artist.getArtistId(), artist.getArtistName());
                return false;

            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artists.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this, artists);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDialog(final String artistId, String artistName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinnerGenres);

        dialogBuilder.setTitle("Updating Artist " + artistName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenres.getSelectedItem().toString();
                if (TextUtils.isEmpty(name)) {
                    editTextName.setError("Name required");
                    return;
                }
                updateArtist(artistId,name,genre);
                alertDialog.dismiss();
                }
        });
buttonDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        deleteArtist(artistId);
        alertDialog.dismiss();

    }
});
    }

    private void deleteArtist(String artistId) {
        DatabaseReference drArtist=FirebaseDatabase.getInstance().getReference("artists").child(artistId);
        DatabaseReference drTracks=FirebaseDatabase.getInstance().getReference("tracks").child(artistId);
        drArtist.removeValue();
        drTracks.removeValue();
        Toast.makeText(this, "Artist is deleted!", Toast.LENGTH_SHORT).show();

    }

    private boolean updateArtist(String id,String name, String genre){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("artists").child(id);
        Artist artist=new Artist(id, name ,genre);
        databaseReference.setValue(artist);
        Toast.makeText(this, "Artist Updated! ", Toast.LENGTH_SHORT).show();
        return true;
    }


    private void addArtist() {
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, genre);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_SHORT).show();
        }
    }

}
