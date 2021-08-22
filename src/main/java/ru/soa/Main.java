package ru.soa;

import ru.soa.reader.VoiceRecognizer;
import ru.soa.reader.config.SoundLine;

public class Main {
    public static void main(String[] args) {
        SoundLine soundLine = new SoundLine(44000);
        VoiceRecognizer voiceRecognizer = new VoiceRecognizer(soundLine);
        voiceRecognizer.tts();
    }
}
