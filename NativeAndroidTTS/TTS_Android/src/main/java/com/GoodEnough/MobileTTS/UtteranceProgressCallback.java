package com.GoodEnough.MobileTTS;

public interface UtteranceProgressCallback
{
    public void onStop(String utteranceId, boolean interrupted);
    public void onDone(String utteranceId);
    public void onStart(String utteranceId);
    public void onRangeStart(String utteranceId, int start, int end, int frame);
}
