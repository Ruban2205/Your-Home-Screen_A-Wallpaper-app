package in.rubangino.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.rubangino.wallpaper.adapter.FavouriteAdapter;
import in.rubangino.wallpaper.model.WallpaperModel;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<WallpaperModel> bookmarkList;
    Gson gson;

    FavouriteAdapter adapter;

    public static final String BOOKMARK_PREF = "bookmarkPrefs";
    public static final String BOOKMARK_LIST = "bookmarkLists";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_favourite);

        init();

        getImages();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FavouriteAdapter(bookmarkList, this);

        recyclerView.setAdapter(adapter);

        onDataHandle();

    }

    private void onDataHandle(){

        adapter.OnImageRemoved(new FavouriteAdapter.OnImageRemoved() {
            @Override
            public void onImageRemoved(int position) {
                bookmarkList.remove(position);

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void storeImage(){
        String json = gson.toJson(bookmarkList);

        editor = sharedPreferences.edit();
        editor.putString(BOOKMARK_LIST, json);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeImage();
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView);

        sharedPreferences = getSharedPreferences(BOOKMARK_PREF, MODE_PRIVATE);

        gson = new Gson();
    }

    private void getImages(){
        String json = sharedPreferences.getString(BOOKMARK_LIST, "");

        Type type = new TypeToken<List<WallpaperModel>>(){
        }.getType();

        bookmarkList = gson.fromJson(json, type);

        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.privacy_fav_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! This app has cool wallpapers check it out now: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            return true;
        }

        else if(id == R.id.exit){
            finish();
            return true;
        }
        return false;
    }

}