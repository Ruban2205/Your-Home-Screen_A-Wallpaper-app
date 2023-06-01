 package in.rubangino.wallpaper.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import in.rubangino.wallpaper.R;
import in.rubangino.wallpaper.model.WallpaperModel;

import static android.content.Context.MODE_PRIVATE;
import static in.rubangino.wallpaper.FavouriteActivity.BOOKMARK_PREF;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {

    private List<WallpaperModel> list;
    private Context context;

    private OnImageRemoved imageRemoved;


    public FavouriteAdapter(List<WallpaperModel> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items, parent, false);
        return new FavouriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteAdapter.FavouriteHolder holder, final int position) {

        Random random = new Random();

        int color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getImage())
                .timeout(7500)
                .placeholder(new ColorDrawable(color))
                .into(holder.imageView);

        if (holder.imageMatched(position)){
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        }else{
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
        }

        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.imageMatched(position)){
                    imageRemoved.onImageRemoved(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FavouriteHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private FloatingActionButton actionButton;

        SharedPreferences sharedPreferences;
        Gson gson;

        int matchPosition;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            actionButton = itemView.findViewById(R.id.actionButton);

            sharedPreferences = context.getSharedPreferences(BOOKMARK_PREF, MODE_PRIVATE);
            gson = new Gson();
        }

        private boolean imageMatched(int currentPosition){

            int i = 0;
            boolean matched = false;

            for (WallpaperModel model : list){

                if (model.getId().equals(list.get(currentPosition).getId())
                        & model.getImage().equals(list.get(currentPosition).getImage())){
                    matched = true;
                    matchPosition = i;
                }
                i++;
            }
            return matched;
        }
    }

    public interface OnImageRemoved {
        void onImageRemoved(int position);
    }

    public void OnImageRemoved(OnImageRemoved imageRemoved){
        this.imageRemoved = imageRemoved;
    }
}
