package br.edu.fatecsjc.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.CropImageTransform;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.ScaleImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class TrainUsingZoo {

    private static final int SEED = 123;

    static final int MINI_BATCH_SIZE = 2;

    static int CHANNELS = 3;
    static int IMG_WIDTH = 224;
    static int IMG_HEIGHT = 224;

    static int N_LABELS = 2;

    static int numEpochs = 50, transformedDataEpochs = 2;

    private static Logger log = LoggerFactory.getLogger(TrainUsingZoo.class);

    public static void main(String[] args) throws IOException {

        ParentPathLabelGenerator labelGenerator = new ParentPathLabelGenerator();
        ImageTransform[] transforms = getTransforms();

        File trainRootDir = Paths.get("E:\\isic\\trainset").toFile();
        File testRootDir = Paths.get("E:\\isic\\testset").toFile();
        InputSplit trainData = new FileSplit(trainRootDir, BaseImageLoader.ALLOWED_FORMATS, new Random());
        InputSplit testData = new FileSplit(testRootDir, BaseImageLoader.ALLOWED_FORMATS, new Random());
        ImageRecordReader trainReader = new ImageRecordReader(IMG_WIDTH, IMG_HEIGHT, CHANNELS, labelGenerator);
        ImageRecordReader testReader = new ImageRecordReader(IMG_WIDTH, IMG_HEIGHT, CHANNELS, labelGenerator);
        System.out.println("initializing");
        trainReader.initialize(trainData);
        testReader.initialize(testData);
        System.out.println(trainReader.getLabels());
        System.out.println(testReader.getLabels());
        DataSetIterator trainDataSet = new RecordReaderDataSetIterator(trainReader, MINI_BATCH_SIZE, 1, N_LABELS);
        DataSetIterator testDataSet = new RecordReaderDataSetIterator(testReader, MINI_BATCH_SIZE, 1, N_LABELS);

        ZooModel<?> zooModel = new ResNet50(N_LABELS, SEED, 1);
        ComputationGraph initializedZooModel  = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .learningRate(0.0001)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                .seed(123)
                .build();
        System.out.println(initializedZooModel.summary());
        ComputationGraph modelTransfer = new TransferLearning.GraphBuilder(initializedZooModel)
                .fineTuneConfiguration(fineTuneConf)
                .setFeatureExtractor("flatten_3")
                .removeVertexKeepConnections("fc1000")
                .addLayer("fc1000",
                        new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                                .nIn(2048).nOut(N_LABELS)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.SOFTMAX).build(), "flatten_3")
                .build();
        System.out.println(modelTransfer.summary());
        modelTransfer.setListeners(new ScoreIterationListener(MINI_BATCH_SIZE));
        UIServer uiServer = UIServer.getInstance();

        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);

        //Then add the StatsListener to collect this information from the network, as it trains
        modelTransfer.setListeners(new StatsListener(statsStorage));
        log.info("Train model....");

        log.info("Training with original data");
        for (int i = 0; i < numEpochs; i++) {
            log.info("Epoch " + i);
            modelTransfer.fit(trainDataSet);
        }

        log.info("Training with transformed data");
        for (int i = 0; i < transformedDataEpochs; i++) {
            log.info("Epoch " + i  + " (transformed data)");
            for (int j = 0; j < transforms.length; j++) {
                ImageTransform imageTransform = transforms[j];
                log.info("Epoch " + i  + " (transform " + imageTransform + ")");
                trainReader.initialize(trainData, imageTransform);
                trainDataSet = new RecordReaderDataSetIterator(trainReader, MINI_BATCH_SIZE, 1, N_LABELS);
                modelTransfer.fit(trainDataSet);
            }
        }

        log.info("Evaluate model....");
        Evaluation eval = new Evaluation(N_LABELS);
        while (testDataSet.hasNext()) {
            DataSet next = testDataSet.next();
            INDArray[] output = modelTransfer.output(next.getFeatureMatrix());
            for (int i = 0; i < output.length; i++) {
                eval.eval(next.getLabels(), output[i]);
            }
        }

        String stats = eval.stats();
        log.info(stats);
        log.info("****************Example finished********************");
        Files.write(stats.getBytes(), Paths.get("latest-output.txt").toFile());
        File file = new File("skin-cancer-model.zip");
        ModelSerializer.writeModel(modelTransfer, file, true);

    }


    private static ImageTransform[] getTransforms() {
        ImageTransform randCrop = new CropImageTransform(new Random(), 10);
        ImageTransform warpTransform = new WarpImageTransform(new Random(), 42);
        ImageTransform flip = new FlipImageTransform(new Random());
        ImageTransform scale = new ScaleImageTransform(new Random(), 1);
        return new ImageTransform[] { randCrop, warpTransform, flip, scale };
    }

}