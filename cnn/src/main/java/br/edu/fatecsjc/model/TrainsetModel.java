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

public class TrainsetModel implements ConfigInterface {
	private ZooModel<?> zooModel;
	DataSetIterator trainDataSet;
	ImageRecordReader trainReader;
	
	public TrainsetModel() throws IOException {
		File cancerTrainRootDir = Paths.get("/Users/giulianogimenez/Documents/isic/trainset").toFile();
		InputSplit trainData = new FileSplit(cancerTrainRootDir, BaseImageLoader.ALLOWED_FORMATS, new Random());
		trainReader = new ImageRecordReader(IMG_WIDTH, IMG_HEIGHT, CHANNELS, labelGenerator);
		trainDataSet = new RecordReaderDataSetIterator(trainReader, MINI_BATCH_SIZE, 1, N_LABELS);
//		this.zooModel = new ResNet50(N_LABELS, SEED, 1);
		trainReader.initialize(trainData);
	}
	
	public ZooModel<?> getZooModel() {
		return zooModel;
	}
	
	public ImageRecordReader getTrainReader() {
		return trainReader;
	}

	public DataSetIterator returnModelDataSetIterator() {
		return trainDataSet;
	}

	public void setTrainDataSet(DataSetIterator trainDataSet) {
		this.trainDataSet = trainDataSet;
	}
}
