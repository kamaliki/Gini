package com.example.gini;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    View screenView;
    TextToSpeech textToSpeechSystem;

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(view -> MainActivity.this.speak());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint({"WrongConstant", "ShowToast"})
    private void speak() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", "Hi speak something");
        try {
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), 0).show();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("ResourceType")
    @Override // androidx.fragment.app.FragmentActivity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.screenView = findViewById(R.layout.activity_main);
        if (requestCode == 100) {
            if (resultCode == -1 && data != null) {
                final ArrayList<String> result = data.getStringArrayListExtra("android.speech.extra.RESULTS");
                this.txtSpeechInput.setText(result.get(0));
                this.textToSpeechSystem = new TextToSpeech(this, status -> {
                    if (result.contains("red")) {
                        MainActivity.this.textToSpeechSystem.speak("Displaying, the color red on the screen", 1, null);
                        MainActivity.this.screenView.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                    }
                    if (result.contains("blue")) {
                        MainActivity.this.textToSpeechSystem.speak("Displaying, the color blue on the screen", 1, null);
                        MainActivity.this.screenView.setBackgroundColor(-16776961);
                    }
                });
                return;
            }
            return;
        }
        return;
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onPause() {
        TextToSpeech textToSpeech = this.textToSpeechSystem;
        if (textToSpeech != null) {
            textToSpeech.stop();
            this.textToSpeechSystem.shutdown();
        }
        super.onPause();
    }

    public int getREQ_CODE_SPEECH_INPUT() {
        return REQ_CODE_SPEECH_INPUT;
    }
}
