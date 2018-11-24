package com.GoodEnough.MobileTTS;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
import com.unity3d.player.UnityPlayer;


public class TTS {

    private Context context;
    private TextToSpeech t1;
    private String textToSpeak = "hello";
    private static TTS instance;
    public float Speed = 1f;
    public float Pitch = 1f;

    private String _gameObject;
    private String _callback;

    public TTS() {
        this.instance = this;
    }

    public static TTS instance() {
        if (instance == null) {
            instance = new TTS();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    String Error = "";

    public void TTSME(String text) {
        textToSpeak = text;
        TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t1.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Error = "This language is not supported!";
                    }
                    t1.setSpeechRate(Speed);
                    t1.setPitch(Pitch);
                    t1.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, textToSpeak);
                } else {
                    Error = "TTS Initialization failed!";

                }

            }
        };

        t1 = new TextToSpeech(context, onInitListener);

    }

    public void SetLang(String loc) {
        switch (loc) {
            case "UK":
                if (t1 != null)
                    t1.setLanguage(Locale.UK);
                break;
            case "US":
                if (t1 != null)
                    t1.setLanguage(Locale.US);
                break;
        }
    }
}
