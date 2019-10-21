package br.sp.gov.fatec.cancerconsultapi.service;

import br.sp.gov.fatec.cancerconsultapi.Result;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainedModelService {
    private String MODEL_FILE = "model.zip";

    public List<Result> consultTrainedModel(String filename) throws IOException {
        List<Result> resultList = new ArrayList<>();
        String[] labels = { "benign", "malignant"} ;
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork("model.zip") ;
        NativeImageLoader loader = new NativeImageLoader(262, 350, 3);
        INDArray image = loader.asMatrix(new File(filename));
        ImagePreProcessingScaler preProcessor = new ImagePreProcessingScaler(0, 1);
        preProcessor.transform(image);
        INDArray output = model.output(image, false);
        for (int i = 0; i < labels.length; i++) {
            float double1 = Math.abs(output.getFloat(i));
            resultList.add(new Result(labels[i] , Double.toString(Math.round(double1 * 100.0))));
        }
        return resultList;
    }
}
