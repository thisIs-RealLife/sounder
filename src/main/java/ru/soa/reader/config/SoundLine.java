package ru.soa.reader.config;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SoundLine extends AudioLinesConfig {

    public SoundLine() {
    }

    public SoundLine(int sampleRate) {
        super(sampleRate);
    }

    public SoundLine(int sampleRate, int sampleSizeLnBits, int channel, boolean signed, boolean bigEndian) {
        super(sampleRate, sampleSizeLnBits, channel, signed, bigEndian);
    }

    public void testLines() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int numBytesRead;
        int CHUNK_SIZE = 1024;
        TargetDataLine microphone = getMicrophone();
        try {
            microphone.open(getAudioFormat());

            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();
            int bytesRead = 0;
            while (bytesRead < 100000) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                bytesRead = bytesRead + numBytesRead;
                System.out.println(bytesRead);
                out.write(data, 0, numBytesRead);
            }
            byte audioData[] = out.toByteArray();
            InputStream byteArrayInputStream = new ByteArrayInputStream(
                    audioData);
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, getAudioFormat(), audioData.length / getAudioFormat().getFrameSize());
            SourceDataLine sourceDataLine = startPlayer();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            while (true) {

                if ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) == -1)
                    break;

                if (cnt > 0) {
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
            sourceDataLine.drain();
            sourceDataLine.close();
            microphone.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public TargetDataLine startMicrophone() throws LineUnavailableException {
        TargetDataLine targetDataLine = getMicrophone();
        targetDataLine.open(getAudioFormat());
        targetDataLine.start();
        return targetDataLine;
    }

    @Override
    public SourceDataLine startPlayer() throws LineUnavailableException {
        SourceDataLine sourceDataLine = getSourceDataLine();
        sourceDataLine.open(getAudioFormat());
        sourceDataLine.start();
        return sourceDataLine;
    }
}
