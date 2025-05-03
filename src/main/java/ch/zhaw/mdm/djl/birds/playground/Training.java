package ch.zhaw.mdm.djl.birds.playground;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.metric.Metrics;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingConfig;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;

public final class Training {

    private static final int BATCH_SIZE = 20;

    private static final int EPOCHS = 20;

    public static void main(String[] args) throws IOException, TranslateException {

        Path modelDir = Paths.get("models");

        ImageFolder dataset = initDataset("src/main/resources/data/CUB_20_2011_images");

        RandomAccessDataset[] datasets = dataset.randomSplit(8, 2);

        Loss loss = Loss.softmaxCrossEntropyLoss();
        TrainingConfig config = setupTrainingConfig(loss);

        Model model = Models.getModel();
        Trainer trainer = model.newTrainer(config);

        trainer.setMetrics(new Metrics());

        Shape inputShape = new Shape(1, 3, Models.IMAGE_HEIGHT, Models.IMAGE_HEIGHT);

        trainer.initialize(inputShape);

        EasyTrain.fit(trainer, EPOCHS, datasets[0], datasets[1]);

        TrainingResult result = trainer.getTrainingResult();

        float trainAccuracy = result.getTrainEvaluation("Accuracy");
        float validateAccuracy = result.getValidateEvaluation("Accuracy");

        String json = String.format("{\"trainAccuracy\": %.4f, \"validateAccuracy\": %.4f}",
            trainAccuracy, validateAccuracy);

        Path resultPath = Paths.get("src/main/resources/static/models/training-result.json");
        Files.write(resultPath, json.getBytes(StandardCharsets.UTF_8));

        model.setProperty("Epoch", String.valueOf(EPOCHS));
        model.setProperty(
                "Accuracy", String.format("%.5f", result.getValidateEvaluation("Accuracy")));
        model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));

        model.save(modelDir, Models.MODEL_NAME);

        Models.saveSynset(modelDir, dataset.getSynset());

    }

    private static ImageFolder initDataset(String datasetRoot)
            throws IOException, TranslateException {
        ImageFolder dataset = ImageFolder.builder()

                .setRepositoryPath(Paths.get(datasetRoot))
                .optMaxDepth(10)
                .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                .addTransform(new ToTensor())
                .optLimit(5000)
                .setSampling(BATCH_SIZE, true)
                .build();

        dataset.prepare();
        return dataset;
    }

    private static TrainingConfig setupTrainingConfig(Loss loss) {
        return new DefaultTrainingConfig(loss)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
    }
}
