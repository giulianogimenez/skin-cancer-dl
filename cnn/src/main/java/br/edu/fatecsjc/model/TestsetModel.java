package br.edu.fatecsjc.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class TestsetModel implements ConfigInterface {
	private ZooModel<?> zooModel;
	DataSetIterator testDataSet;
	ImageRecordReader testReader;
	
	public TestsetModel() throws IOException {
		File cancerTrainRootDir = Paths.get("/Users/giulianogimenez/Documents/isic/testset").toFile();
		InputSplit trainData = new FileSplit(cancerTrainRootDir, BaseImageLoader.ALLOWED_FORMATS, new Random());
		testReader = new ImageRecordReader(IMG_WIDTH, IMG_HEIGHT, CHANNELS, labelGenerator);
		testDataSet = new RecordReaderDataSetIterator(testReader, MINI_BATCH_SIZE, 1, N_LABELS);
		this.zooModel = new ResNet50(N_LABELS, SEED, 1);
		testReader.initialize(trainData);
	}
	
	public ZooModel<?> getZooModel() {
		return zooModel;
	}
	
	public ImageRecordReader getTestReader() {
		return testReader;
	}

	public DataSetIterator returnModelDataSetIterator() {
		return testDataSet;
	}
}
