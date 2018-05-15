package marqueetextview.charco.android.marqueetextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import marqueetextview.charco.android.marqueetextview.bean.Config;
import marqueetextview.charco.android.marqueetextview.view.MarqueeTextView;

public class TextActivity extends AppCompatActivity {

    private MarqueeTextView marqueeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_text);
        initView();
    }

    private void initView() {
        marqueeTextView = findViewById(R.id.tv_marauee);
        Config config = (Config) getIntent().getSerializableExtra("config");
        marqueeTextView.setSpeed(config.getSpeed());
        marqueeTextView.setBackgroundColor(config.getBackgroudColor());
        marqueeTextView.setTextColor(config.getTextColor());
        marqueeTextView.setTextContent(config.getContent());
    }
}
