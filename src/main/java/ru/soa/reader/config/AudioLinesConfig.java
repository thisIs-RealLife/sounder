package ru.soa.reader.config;

import javax.sound.sampled.*;

public abstract class AudioLinesConfig {
    private AudioFormat audioFormat;
    private TargetDataLine microphone;
    private SourceDataLine sourceDataLine;
    private int sampleRate = 16000;
    private int sampleSizeLnBits = 16;
    private int channel = 1;
    private boolean signed = true;
    private boolean bigEndian = true;

    public AudioLinesConfig() {
        this.audioFormat = new AudioFormat(this.sampleRate, this.sampleSizeLnBits, this.channel, this.signed, this.bigEndian);
        this.initMicro();
        this.initPlayer();
    }

    public AudioLinesConfig(int sampleRate){
        this.sampleRate = sampleRate;
        this.audioFormat = new AudioFormat(this.sampleRate, this.sampleSizeLnBits, this.channel, this.signed, this.bigEndian);
        this.initMicro();
        this.initPlayer();
    }

    public AudioLinesConfig(int sampleRate, int sampleSizeLnBits, int channel, boolean signed, boolean bigEndian) {
        this.sampleRate = sampleRate;
        this.sampleSizeLnBits = sampleSizeLnBits;
        this.channel = channel;
        this.signed = signed;
        this.bigEndian = bigEndian;
        this.audioFormat = new AudioFormat(sampleRate, sampleSizeLnBits, channel, signed, bigEndian);
        this.initMicro();
        this.initPlayer();
    }

    public void initMicro() {
        try {
            this.microphone = AudioSystem.getTargetDataLine(audioFormat);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void initPlayer(){
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }


    public TargetDataLine getMicrophone() {
        return this.microphone;
    }

    public SourceDataLine getSourceDataLine() {
        return sourceDataLine;
    }

    public void setSourceDataLine(SourceDataLine sourceDataLine) {
        this.sourceDataLine = sourceDataLine;
    }

    public abstract TargetDataLine startMicrophone() throws LineUnavailableException;
    public abstract SourceDataLine startPlayer() throws LineUnavailableException;
}
