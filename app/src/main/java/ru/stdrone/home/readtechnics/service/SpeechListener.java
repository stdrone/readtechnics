package ru.stdrone.home.readtechnics.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;


public class SpeechListener implements RecognitionListener {

    private static final String TAG = "SpeechListener";
    private static final int MAX_RESULTS = 25;
    private SpeechRecognizer mRecognizer;
    private ListenObserver mListenObserver;
    private EventObserver mActionObserver;
    private boolean mIsListening = false;
    private Locale mLocale;

    private AudioManager mAudioManager;
    private int mStreamVolume = 0;

    public SpeechListener(Context context) {
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mRecognizer.setRecognitionListener(this);
        mLocale = context.getResources().getConfiguration().locale;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void setListenObserver(ListenObserver observer) {
        mListenObserver = observer;
    }

    public void setActionObserver(EventObserver observer) {
        mActionObserver = observer;
    }

    public boolean isListening() {
        return mIsListening;
    }

    private void start() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        //intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, MAX_RESULTS);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, MAX_RESULTS * 1000);
        //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLocale.toString());

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_RESULTS);

        if (mIsListening) {
            mStreamVolume = Math.max(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC), mStreamVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }

        mRecognizer.startListening(intent);
        setIsListening(true);
    }

    public synchronized void reset(boolean isListening) {
        if (isListening)
            start();
        else
            stop();
    }

    private void processResults(ArrayList<String> results) {
        if (mListenObserver != null) {
            for (String words : results) {
                for (String word : words.split("\\s+")) {
                    mListenObserver.OnListenWord(word);
                    Log.i(TAG, "processResults " + word);
                }
            }
        }
    }

    private void stop() {
        if (mStreamVolume > 0)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
        mRecognizer.stopListening();
        mRecognizer.cancel();
        setIsListening(false);
    }

    private void setIsListening(boolean isListening) {
        mIsListening = isListening;
        if (mActionObserver != null) mActionObserver.OnListenEvent();
    }

    public void onReadyForSpeech(Bundle params) {
        if (mStreamVolume > 0)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
        Log.d(TAG, "onReadyForSpeech");
    }

    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    public void onRmsChanged(float rmsdB) {
        //Log.d(TAG, "onRmsChanged");
    }

    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    public void onEndOfSpeech() {
        reset(mIsListening);
        Log.d(TAG, "onEndOfSpeech");
    }

    public void onError(int error) {
        Log.e(TAG, "onError " + error);
        if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)
            reset(mIsListening);
        if (error == SpeechRecognizer.ERROR_NO_MATCH)
            reset(mIsListening);
    }

    @Override
    public void onResults(Bundle results) {
        processResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        reset(mIsListening);

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        processResults(partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }

    public interface ListenObserver {
        void OnListenWord(String word);
    }

    public interface EventObserver {
        void OnListenEvent();
    }

}