package in.rubangino.wallpaper.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import in.rubangino.wallpaper.R;
import in.rubangino.wallpaper.SwiperActivity;
import in.rubangino.wallpaper.model.WallpaperModel;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperHolder> {

    private List<WallpaperModel> list;

    public WallpaperAdapter(List<WallpaperModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WallpaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_items, parent, false);
        return new WallpaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WallpaperHolder holder, final int position) {

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

        holder.setClickListener();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class WallpaperHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public WallpaperHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }

        void setClickListener(){

            //Set OnclickListener for wallpaper Images
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    Intent intent = new Intent(itemView.getContext().getApplicationContext(), SwiperActivity.class);
                    intent.putExtra("position", position);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
