package com.GoodEnough.MobileTTS;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;

import java.util.Set;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TTS
{
    private static TTS instance;
    private static PluginCallback _onInitCallback;
    private TextToSpeech textToSpeech;

    public Set<Voice> AllVoices;
    public boolean IsInitialized = false;

    public TTS(Context context)
    {
        textToSpeech = new TextToSpeech(context, new OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if (status == TextToSpeech.SUCCESS)
                {
                    AllVoices = textToSpeech.getVoices();
                    IsInitialized = true;
                    _onInitCallback.onSuccess();
                }
                else
                {
                    _onInitCallback.onError("TTS Initialization failed");
                }
            }
        });
    }

    public static TTS GetInstance(Context context, PluginCallback onInitCallback)
    {
        if (instance == null)
        {
            _onInitCallback = onInitCallback;
            instance = new TTS(context);
        }
        return instance;
    }

    public void Speak(String textToSpeak, float pitch, long preUtteranceDelay,
                      long postUtteranceDelay, float rate, String voiceId, float volume)
    {
        if(preUtteranceDelay > 0)
            textToSpeech.playSilentUtterance(preUtteranceDelay, TextToSpeech.QUEUE_ADD, "");

        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume);
        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(rate);

        Voice voice = GetVoice(voiceId);
        if(voice != null)
            textToSpeech.setVoice(voice);
        else
            textToSpeech.setVoice(GetDefaultVoice());
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_ADD, params, textToSpeak);

        if(postUtteranceDelay > 0)
            textToSpeech.playSilentUtterance(postUtteranceDelay, TextToSpeech.QUEUE_ADD, "");
    }

    public int Stop()
    {
        return textToSpeech.stop();
    }

    public boolean IsSpeaking()
    {
        return textToSpeech.isSpeaking();
    }

    private Voice GetDefaultVoice()
    {
        return textToSpeech.getDefaultVoice();
    }

    private Voice GetVoice(int index)
    {
        return (Voice) AllVoices.toArray()[index];
    }

    private Voice GetVoice(String voiceName)
    {
        for (Voice voice : AllVoices)
        {
            if (voice.getName().equals(voiceName))
            {
                return voice;
            }
        }
        return null;
    }

    public int GetNumberOfVoices()
    {
        return AllVoices.size();
    }

    public void SetOnUtteranceProgressListener(final UtteranceProgressCallback progressCallback)
    {
        UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId)
            {
                progressCallback.onStart(utteranceId);
            }

            @Override
            public void onDone(String utteranceId)
            {
                progressCallback.onDone(utteranceId);
            }

            @Override
            public void onError(String utteranceId)
            {
            }

            @Override
            public void onRangeStart (String utteranceId,
                                      int start,
                                      int end,
                                      int frame)
            {
                progressCallback.onRangeStart(utteranceId, start, end, frame);
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted)
            {
                progressCallback.onStop(utteranceId, interrupted);
            }
        };

        textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
    }

    public String GetVoiceId(int index)
    {
        return GetVoice(index).getName();
    }

    public String GetVoiceLocale(int index)
    {
        return GetVoice(index).getLocale().toString();
    }

    public int GetVoiceQuality(int index)
    {
        return GetVoice(index).getQuality();
    }
}
