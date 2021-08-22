package ru.soa.reader;

import ru.soa.reader.config.SoundLine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class VoiceRecognizer extends RecognizerConfig {

    public VoiceRecognizer(SoundLine soundLine) {
        super(soundLine);
    }

    public void speechToText() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TargetDataLine microphone = soundLine.startMicrophone();
            byte[] data = new byte[microphone.getBufferSize() / 5];
            recordVoice(out, microphone, data);
            microphone.close();
            AudioInputStream ais = convertToWave(out.toByteArray());
            vosk.recognize(ais);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream recordVoice(ByteArrayOutputStream outputStream, TargetDataLine microphone, byte[] buffer) {
        int bytesRead = 0, numBytesRead;
        while (bytesRead < 300000) { // Just so I can test if recording
            numBytesRead = microphone.read(buffer, 0, buffer.length);
            System.out.println(calculateRMSLevel(buffer));
            bytesRead = bytesRead + numBytesRead;
            System.out.println(bytesRead);
            outputStream.write(buffer, 0, numBytesRead);
        }
        return outputStream;
    }


    public void TTSWhenStartTalking() {

    }

    public void tts() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TargetDataLine microphone = soundLine.startMicrophone();
            byte[] data = new byte[microphone.getBufferSize() / 5];

            int i = 0;
            int numBytesRead;
            while (true) {
                i++;
                microphone.read(data, 0, data.length);
                out.write(data);
                if (i == 10) {
                    vosk.recognize(convertToWave(out.toByteArray()));
                    i = 0;
                    out.reset();
                }
            }

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
