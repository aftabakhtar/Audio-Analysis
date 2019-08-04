package pk.edu.nust.seecs.oop.project.helpers;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class AudioFingerPrintingHelper {
    // Attributes.

    // Methods.
    public double audioSimilarity(String pathOfComparisionSource, String pathOfFileToCompare) {
        double similarityToReturn;

        // Creating wave files.
        Wave audioToCompare = new Wave(pathOfFileToCompare);
        Wave comparisionAudio = new Wave(pathOfComparisionSource);

        // Creating reference of fingerprint similarity.
        FingerprintSimilarity similarity;

        // Fetching similarity.
        similarity = audioToCompare.getFingerprintSimilarity(comparisionAudio);

        similarityToReturn = similarity.getSimilarity();
        similarityToReturn *= 100;

        return similarityToReturn;
    }
}
