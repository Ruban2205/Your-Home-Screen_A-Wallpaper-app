package in.rubangino.wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    public static int SPLASH_SCREEN = 5000;

    //variables
    Animation top_anim, fadein_anim,botton_anim;
    TextView header1, header2, header3, copyrightText, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //Animations
        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        fadein_anim = AnimationUtils.loadAnimation(this, R.anim.fadein_animation);
        botton_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        header1 = findViewById(R.id.head1);
        header2 = findViewById(R.id.head2);
        header3 = findViewById(R.id.head3);
        slogan = findViewById(R.id.slogan);
        copyrightText = findViewById(R.id.copyright);

        header1.setAnimation(fadein_anim);
        header2.setAnimation(fadein_anim);
        header3.setAnimation(fadein_anim);
        copyrightText.setAnimation(top_anim);
        slogan.setAnimation(botton_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}