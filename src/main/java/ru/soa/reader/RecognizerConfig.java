package ru.soa.reader;

import ru.soa.reader.config.SoundLine;
import ru.soa.vosk.Vosk;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public abstract class RecognizerConfig {
    public SoundLine soundLine;
    protected AudioFormat audioFormat;
    protected Vosk vosk;

    protected RecognizerConfig(SoundLine soundLine) {
        this.soundLine = soundLine;
        this.audioFormat = soundLine.getAudioFormat();
        this.vosk = new Vosk((int) soundLine.getAudioFormat().getSampleRate());
    }

    public AudioInputStream convertToWave(byte[] rawData) {
        try {
            InputStream inputStream = new ByteArrayInputStream(rawData);
            AudioInputStream audioInputStream = new AudioInputStream(inputStream, audioFormat, rawData.length / audioFormat.getFrameSize());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
            InputStream record = new BufferedInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            return new AudioInputStream(record, audioFormat, rawData.length / audioFormat.getFrameSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int calculateRMSLevel(byte[] audioData) {
        long lSum = 0;
        for (int i = 0; i < audioData.length; i++)
            lSum = lSum + audioData[i];
        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0d;
        for (int j = 0; j < audioData.length; j++)
            sumMeanSquare += Math.pow(audioData[j] - dAvg, 2d);
        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
    }
}
