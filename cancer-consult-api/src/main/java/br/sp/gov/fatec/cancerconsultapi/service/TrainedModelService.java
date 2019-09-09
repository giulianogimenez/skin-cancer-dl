package br.sp.gov.fatec.cancerconsultapi.service;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class TrainedModelService {
    private String MODEL_FILE = "model.zip";

    public String[] consultTrainedModel(String filename) throws IOException {
        String[] labels = { "benign", "malignant"} ;
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork("model.zip") ;
        NativeImageLoader loader = new NativeImageLoader(262, 350, 3);
        INDArray image = loader.asMatrix(new File(filename));
        ImagePreProcessingScaler preProcessor = new ImagePreProcessingScaler(0, 1);
        preProcessor.transform(image);
        INDArray output = model.output(image, false);
        String[] result = {"", ""};
        for (int i = 0; i < labels.length; i++) {
            float double1 = Math.abs(output.getFloat(i));
            result[i] = labels[i] + " [" + Double.toString(double1 * 100.0)  + "%]";
        }
        return result;
    }
}
