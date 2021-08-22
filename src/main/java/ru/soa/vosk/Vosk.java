package ru.soa.vosk;


import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Vosk {
    private int sampleRate = 16000;
    private Model model;
    private Recognizer recognizer;

    public Vosk() {
        init();
    }

    public Vosk(int sampleRate) {
        this.sampleRate = sampleRate;
        init();
    }

    private void init() {
        model = new Model("model");
        recognizer = new Recognizer(model, sampleRate);
    }


    public void recognize(AudioInputStream audioInputStream) throws IOException {
        InputStream ais = audioInputStream;
        int nbytes;
        byte[] b = new byte[4096];
        while ((nbytes = ais.read(b)) >= 0) {
           recognizer.acceptWaveForm(b, nbytes);
        }
        System.out.println(recognizer.getFinalResult() + "final");
    }

    public void close() {
        this.model.close();
        this.recognizer.close();
    }
}
