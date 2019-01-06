package com.example.arcorewith3dsound;

import android.content.Context;

import com.google.ar.core.Pose;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.vr.sdk.audio.GvrAudioEngine;

public class AudioArFragment extends ArFragment {
    private GvrAudioEngine audioEngine;
    private String audioFilename = "jingle-bells.wav";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        loadAudio();
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        updateAudioEngineLocation();
    }

    @Override
    public void onPause() {
        super.onPause();

        audioEngine.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        audioEngine.resume();
    }

    public void setAudioPosition(Vector3 pos) {
        new Thread(() -> {
            int objectIdentifier = audioEngine.createSoundObject(audioFilename);
            audioEngine.playSound(objectIdentifier, true);
            audioEngine.setSoundObjectPosition(objectIdentifier, pos.x, pos.y, pos.z);
        }).start();

    }

    private void updateAudioEngineLocation() {
        Pose pose = getArSceneView().getArFrame().getCamera().getDisplayOrientedPose();

        audioEngine.setHeadRotation(
                pose.qx(), pose.qy(), pose.qz(), pose.qw());
        audioEngine.setHeadPosition(pose.tx(), pose.ty(), pose.tz());
        audioEngine.update();
    }

    private void loadAudio() {
        new Thread(() -> audioEngine.preloadSoundFile(audioFilename)).start();
    }
}
