package pk.edu.nust.seecs.oop.project.helpers;

import com.musicg.graphic.GraphicRender;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveTypeDetector;
import com.musicg.wave.extension.Spectrogram;

import java.io.File;

public class GraphHelper {
    // Methods.
    // Method to get file name from the string of path.
    private String separateName(String pathOfFile) {
        return pathOfFile.substring(1 + pathOfFile.lastIndexOf(File.separator));
    }

    // Method to add .jpg extension at the end.
    private String addExtension(String fileName) {
        if (fileName.substring(fileName.length() - 4).equalsIgnoreCase(".jpg")) {
            return fileName;
        }
        else {
            return fileName + "jpg";
        }
    }

    // Method to generate waveform.
    public void generateGraph(String pathOfFile, String savePath, boolean firstTime, boolean waveform) {
        String outputPath;

        // Creating wave file.
        Wave wave = new Wave(pathOfFile);

        // Creating new spectrogram.
        Spectrogram spectrogram = new Spectrogram(wave);

        // Graphic Renderer Object.
        GraphicRender graphicRender = new GraphicRender();

        // Making output path.
        if (firstTime) {
            outputPath = savePath + "/temp.jpg";

            if (waveform) {
                // Rendering the waveform
                graphicRender.renderWaveform(wave, outputPath);
            }
            else {
                // Rendering spectrogram
                graphicRender.renderSpectrogram(spectrogram, outputPath);
            }
        }
        else {
            outputPath = addExtension(savePath);

            if (waveform) {
                // Rendering waveform
                graphicRender.renderWaveform(wave, outputPath);
            }
            else {
                // Render spectrogram.
                graphicRender.renderSpectrogram(spectrogram, outputPath);
            }

        }
    }

    // Method to get whistle probability.
    public double getWhistleProbability(String pathOfFile) {
        double probability;

        // Creating wave file.
        Wave wave = new Wave(pathOfFile);

        // Wave type detector.
        WaveTypeDetector waveTypeDetector = new WaveTypeDetector(wave);
        try {
            probability = waveTypeDetector.getWhistleProbability();
        }
        catch (Exception e) {
            probability = -1;
        }
        return probability;
    }
}
