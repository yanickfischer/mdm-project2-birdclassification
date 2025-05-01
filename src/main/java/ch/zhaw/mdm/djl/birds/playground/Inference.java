package ch.zhaw.mdm.djl.birds.playground;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

public class Inference {

    Predictor<Image, Classifications> predictor;

    public Inference() {
        try {
            Model model = Models.getModel();
            Path modelDir = Paths.get("models");
            model.load(modelDir, Models.MODEL_NAME);
    
            // define a translator for pre and post processing
            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                    .addTransform(new ToTensor())
                    .optApplySoftmax(true)
                    .build();
            predictor = model.newPredictor(translator);
    
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren des Modells. Wurde der Ordner 'models/' im Docker-Image eingebunden?", e);
        }
    }

    public Classifications predict(byte[] image) throws ModelException, TranslateException, IOException {
        InputStream is = new ByteArrayInputStream(image);
        BufferedImage bi = ImageIO.read(is);
        Image img = ImageFactory.getInstance().fromImage(bi);

        if (predictor == null) {
            throw new IllegalStateException("Predictor ist null – Modell wurde nicht geladen.");
        }

        Classifications predictResult = this.predictor.predict(img);
        return predictResult;
    }
}