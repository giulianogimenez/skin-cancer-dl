import os

class DatasetSeparator():

    def __init__(self):
        self.createDataSetFolder("trainset")
        self.createDataSetFolder("testset")
        malignant_count = 1300 #len(os.listdir(os.getenv("DATASET_PATH") + "malignant"))
        m_testset_count = int((malignant_count * 20) / 100)
        m_trainset_count = malignant_count - m_testset_count
        i = 1
        for i in range(0, m_testset_count):
            m_file = os.listdir(os.getenv("DATASET_PATH") + "malignant")[0]
            m_origin = os.getenv("DATASET_PATH") + "malignant/" + m_file
            m_destiny = os.getenv("DATASET_PATH") + "testset/malignant/" + m_file
            os.rename(m_origin, m_destiny)
            b_file = os.listdir(os.getenv("DATASET_PATH") + "benign")[0]
            b_origin = os.getenv("DATASET_PATH") + "benign/" + b_file
            b_destiny = os.getenv("DATASET_PATH") + "testset/benign/" + b_file
            os.rename(b_origin, b_destiny)
        print("moved " + str(i) + " files to testset")
        for i in range(0, m_trainset_count):
            m_file = os.listdir(os.getenv("DATASET_PATH") + "malignant")[0]
            m_origin = os.getenv("DATASET_PATH") + "malignant/" + m_file
            m_destiny = os.getenv("DATASET_PATH") + "trainset/malignant/" + m_file
            os.rename(m_origin, m_destiny)
            b_file = os.listdir(os.getenv("DATASET_PATH") + "benign")[0]
            b_origin = os.getenv("DATASET_PATH") + "benign/" + b_file
            b_destiny = os.getenv("DATASET_PATH") + "trainset/benign/" + b_file
            os.rename(b_origin, b_destiny)
        print("moved " + str(i) + " files to trainset")

    def createDataSetFolder(self, foldername):
        if not os.path.exists(os.getenv("DATASET_PATH") + foldername):
            os.mkdir(os.getenv("DATASET_PATH") + foldername)
            print(os.getenv("DATASET_PATH") + foldername + " created.")
            os.mkdir(os.getenv("DATASET_PATH") + foldername + "/benign")
            print(os.getenv("DATASET_PATH") + foldername + "/benign created.")
            os.mkdir(os.getenv("DATASET_PATH") + foldername + "/malignant")
            print(os.getenv("DATASET_PATH") + foldername + "/malignant created.")

if __name__ == '__main__':
    DatasetSeparator()