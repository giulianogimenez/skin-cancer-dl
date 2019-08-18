package br.edu.fatecsjc.model;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public interface ConfigInterface {
	static final int SEED = 123;
	final int MINI_BATCH_SIZE = 2;
	int CHANNELS = 3;
	int IMG_WIDTH = 224;
	int IMG_HEIGHT = 224;
	int N_LABELS = 2;
	ParentPathLabelGenerator labelGenerator = new ParentPathLabelGenerator();
	int numEpochs = 50, transformedDataEpochs = 2;
	
	public DataSetIterator returnModelDataSetIterator();
}
