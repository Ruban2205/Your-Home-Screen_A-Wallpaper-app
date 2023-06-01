package in.rubangino.wallpaper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.rubangino.wallpaper.adapter.WallpaperAdapter;
import in.rubangino.wallpaper.model.WallpaperModel;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<WallpaperModel> list;
    private WallpaperAdapter adapter;
    DatabaseReference reference;

    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        reference = FirebaseDatabase.getInstance().getReference().child("Wallpapers");


        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);
        if (firstStart) {
            showStartDialog();
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    WallpaperModel model = dataSnapshot.getValue(WallpaperModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        list = new ArrayList<>();
        adapter = new WallpaperAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.favourite){
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
        }

        else if(id == R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! This app has cool wallpapers check it out now: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        else if(id == R.id.feedback){
            Toast.makeText(this, "Please wait", Toast.LENGTH_LONG).show();

            Log.d(" ", "Send Email:");
            String[] TO_EMAILS = {"app.feedback.rubancreations@gmail.com"};

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);

            intent.putExtra(Intent.EXTRA_SUBJECT, "Your subject goes here...");
            intent.putExtra(Intent.EXTRA_TEXT, "Your message goes here...");

            startActivity(Intent.createChooser(intent, "Choose your mail client:"));

            return true;
        }

        else if(id == R.id.privacy){
            Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
            startActivity(intent);

            return true;
        }

        else if(id == R.id.developerSite){
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rubangino.in/")));

            return true;
        }

        else if (id == R.id.about){
            aboutDialog();
        }

        else if(id == R.id.exit){
            Toast.makeText(this, "Hey!! Thanks for using Your Home Screen", Toast.LENGTH_LONG).show();
            finish();

            return true;
        }

        return false;
    }

    private void aboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About Your Home Screen")
                .setMessage("A wallpaper app connecting to a realtime database. Each and every wallpapers will update to new wallpaper with and interval of two weeks. Keep calm and change your wallpaper!. Once wallpapers are updated to the app, users will get notified. Users can able to save the wallpapers in their phones " +
                        "Each wallpapers are stored in the DCIM folder of the file manager." +
                        "Have it yours and enjoy")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    private void showStartDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Your Home Screen!")
                .setMessage("Hello Howdy Users, A wallpaper app with realtime database. New Wallpapers every two week. Share with your friends and Enjoy")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

        SharedPreferences preferences = getSharedPreferences( "preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }

}