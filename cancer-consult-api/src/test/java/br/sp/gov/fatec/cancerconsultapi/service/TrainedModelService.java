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
    private String MODEL_FILE = "./skin-cancer-model.zip";

    public int[] consultTrainedModel(String filename) throws IOException {
        String[] labels = { "cinco_centavos", "cinquenta_centavos", "dez_centavos", "um_real", "vinte_cinco_centavos"} ;

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(Paths.get(MODEL_FILE).toFile());
        model.init();
        NativeImageLoader loader1 = new NativeImageLoader(128, 128, 3);
        NativeImageLoader loader2 = new NativeImageLoader(128, 128, 3);
        INDArray img = loader1.asMatrix(Paths.get(filename).toFile());
        INDArray output1 = model.output(img);
        for (int i = 0; i < labels.length; i++) {
            float double1 = Math.abs(output1.getFloat(i));
            if(double1 > 0.0)
                System.out.println(labels[i] + ": " + double1 + "%");
        }
        int[] predict = model.predict(img);
        for (int i = 0; i < predict.length; i++) {
            System.out.println(predict[i]);
        }
        System.out.println(predict);
        return predict;
    }
}
