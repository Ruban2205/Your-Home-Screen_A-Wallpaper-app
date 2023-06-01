package in.rubangino.wallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.Random;

import in.rubangino.wallpaper.R;
import in.rubangino.wallpaper.model.WallpaperModel;

public class SwiperAdapter extends RecyclerView.Adapter<SwiperAdapter.SwiperHolder> {

    Context context;
    private final List<WallpaperModel> list;

    public static onDataPass dataPass;

    public SwiperAdapter(Context context, List<WallpaperModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_item,
                parent, false);
        return new SwiperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SwiperHolder holder, int position) {

        Random random = new Random();
        int color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));

        holder.constraintLayout.setBackgroundColor(color);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImage())
                .timeout(6500)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }
                })
                .into(holder.imageView);

        holder.clickListeners(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SwiperHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final ConstraintLayout constraintLayout;
        private final ImageButton saveButton;
        private final Button applyButton;

        public SwiperHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.consLayout);
            saveButton = itemView.findViewById(R.id.saveButton);
            applyButton = itemView.findViewById(R.id.applyWallpaperButton);

        }

        private void clickListeners(final int position){

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //It would be converting Image to Bitmap
                    Bitmap bitmap = ( (BitmapDrawable) imageView.getDrawable()).getBitmap();
                    dataPass.onImageSave(position, bitmap);

                }
            });

            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bitmap bitmap = ( (BitmapDrawable) imageView.getDrawable()).getBitmap();

                    dataPass.onApplyImage(position, bitmap);
                }
            });

        }

    }

    public interface onDataPass{
        void onImageSave(int position, Bitmap bitmap);
        void onApplyImage(int position, Bitmap bitmap);

    }

    public void onDataPass(onDataPass dataPass){

        SwiperAdapter.dataPass = dataPass;
    }
}
