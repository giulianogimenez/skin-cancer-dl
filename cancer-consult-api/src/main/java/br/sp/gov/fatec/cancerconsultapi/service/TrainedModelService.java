package br.sp.gov.fatec.cancerconsultapi.service;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class TrainedModelService {
    private String MODEL_FILE = "skin-cancer-model-77.zip";

    public String[] consultTrainedModel(String filename) throws IOException {
        String[] labels = { "benign", "malignant"} ;
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(Paths.get(MODEL_FILE).toFile());
        model.init();
        NativeImageLoader loader1 = new NativeImageLoader(262, 350, 3);
        INDArray img = loader1.asMatrix(Paths.get(filename).toFile());
        INDArray output1 = model.output(img);
        String[] result = {"", ""};
        for (int i = 0; i < labels.length; i++) {
            float double1 = Math.abs(output1.getFloat(i));
            if(double1 > 0.0)
                System.out.println(labels[i] + ": " + double1 + "%");
                result[i] = labels[i] + " [" + Double.toString(double1 * 100.0)  + "%]";
        }
        return result;
    }
}
