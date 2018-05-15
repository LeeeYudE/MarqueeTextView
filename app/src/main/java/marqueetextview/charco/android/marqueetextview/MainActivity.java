package marqueetextview.charco.android.marqueetextview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorPickerView;

import marqueetextview.charco.android.marqueetextview.bean.Config;
import marqueetextview.charco.android.marqueetextview.view.MarqueeTextView;

public class MainActivity extends AppCompatActivity implements ColorPickerView.ColorListener, RadioGroup.OnCheckedChangeListener {

    private ColorPickerView mColorPickerView;
    private EditText mEtContent;
    private View mViewText , mViewBackgroud , mLltColor;
    private RadioGroup mRgSpeed;
    private int colortype;
    private Config mConfig = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mEtContent = findViewById(R.id.et_content);
        mViewText = findViewById(R.id.view_text);
        mViewBackgroud = findViewById(R.id.view_backgroud);
        mLltColor = findViewById(R.id.llt_color);
        mColorPickerView = findViewById(R.id.colorPickerView);
        mColorPickerView.setColorListener(this);
        mRgSpeed = findViewById(R.id.rg_speed);
        mRgSpeed.setOnCheckedChangeListener(this);
        mConfig.setSpeed(MarqueeTextView.SPEED_NORMAL);
        mConfig.setBackgroudColor(getResources().getColor(R.color.background_color));
        mConfig.setTextColor(getResources().getColor(R.color.text_color));
    }

    @Override
    public void onColorSelected(int color) {
        switch (colortype){
            case 0:
                mViewText.setBackgroundColor(color);
                mConfig.setTextColor(color);
                break;
            case 1:
                mViewBackgroud.setBackgroundColor(color);
                mConfig.setBackgroudColor(color);
                break;
        }
    }

    public void showTextColor(View view){
        mLltColor.setVisibility(View.VISIBLE);
        colortype = 0;
    }

    public void showBackgroudColor(View view){
        mLltColor.setVisibility(View.VISIBLE);
        colortype = 1;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id){
            case R.id.rb_slow:
                mConfig.setSpeed(MarqueeTextView.SPEED_SLOW);
                break;
            case R.id.rb_normal:
                mConfig.setSpeed(MarqueeTextView.SPEED_NORMAL);
                break;
            case R.id.rb_fast:
                mConfig.setSpeed(MarqueeTextView.SPEED_FAST);
                break;
        }
    }

    public void resetTextColor(View view) {
        int color = getResources().getColor(R.color.text_color);
        mViewBackgroud.setBackgroundColor(color);
        mConfig.setTextColor(color);

    }

    public void resetBackgroudText(View view) {
        int color = getResources().getColor(R.color.background_color);
        mViewBackgroud.setBackgroundColor(color);
        mConfig.setBackgroudColor(color);
    }

    public void hideColor(View view) {
        mLltColor.setVisibility(View.GONE);
    }

    public void complete(View view){
        String content = mEtContent.getText().toString();
        if (!TextUtils.isEmpty(content)){
            mConfig.setContent(content);
            Intent intent =  new Intent(this,TextActivity.class);
            intent.putExtra("config",mConfig);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
        }
    }

}
