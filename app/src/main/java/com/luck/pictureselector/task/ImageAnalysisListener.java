package com.luck.pictureselector.task;

public interface ImageAnalysisListener {
    void onProgress(int progress);
    void onSuccess();
    void onFaied();
    void onPaused();
    void onCancled();
}
