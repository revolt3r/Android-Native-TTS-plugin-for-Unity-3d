package com.GoodEnough.MobileTTS;

public interface PluginCallback
{
    void onSuccess();
    void onError(String errorMessage);
}