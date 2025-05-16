# 🐦 Bird Classification – MDM Project 2 @ ZHAW

Ein Machine-Learning-Projekt zur automatischen Klassifizierung von Vogelarten anhand von Bildern.  
Entwickelt im Rahmen des Moduls *Model Deployment & Maintenance (MDM)* an der ZHAW.

**Autor:** Yanick Fischer  
**E-Mail:** fischya3@students.zhaw.ch  
**GitHub:** [Projekt-Repository](https://github.com/yanickfischer/mdm-project2-birdclassification)

---

## 🔍 Projektübersicht

Ziel dieses Projekts ist es, ein vollständiges Machine-Learning-System zu entwickeln, das:

- ein vortrainiertes Deep-Learning-Modell zur **Bildklassifikation** nutzt
- das bekannte **Caltech-UCSD Birds 200-2011 Dataset** verwendet
- das Modell mit **Deep Java Library (DJL)** trainiert und deployed
- eine **moderne Spring Boot Web-Anwendung** mit REST-API bietet
- ein **benutzerfreundliches Web-Frontend** mit Upload-Funktion enthält
- vollständig in **Docker** containerisiert und auf **Azure App Service** deploybar ist

---

## 🧠 Technologien

- **Backend:** Java 21, Spring Boot, DJL (Deep Java Library)
- **Frontend:** HTML, JavaScript, Bootstrap
- **ML-Modell:** ResNet50 (pretrained, fine-tuned)
- **Daten:** [Caltech-UCSD Birds 200-2011 Dataset](https://www.vision.caltech.edu/datasets/cub_200_2011/)
- **Build & Deployment:** Maven, Docker, Azure, GitHub Actions (geplant)
- **Training & Evaluation:** 20 Epochen, Transformation mit Resize, CenterCrop und Normalize
- **Modellquelle:** ResNet50 aus dem Model Zoo der Deep Java Library (DJL)

---

## ⚙️ Funktionsweise

1. **Training:**
   - Das Modell wird auf einem reduzierten Subset (20 Klassen) des CUB-Datasets (200 Klassen total) trainiert
   - Die Trainingsgenauigkeit und Validierungsgenauigkeit werden nach dem Training automatisch in einer JSON-Datei (`training-result.json`) gespeichert
   - Das Model kann ganz einfach auf die totalen 200 Klassen erweitert werden, benötigt aber einiges an Compute (lokal auf Laptopo cirka 30h)

2. **Inference (Vorhersage):**
   - Der Benutzer lädt ein Vogelbild im Browser hoch
   - Das Bild wird per REST-API an das Spring Boot Backend gesendet
   - Das Modell analysiert das Bild und liefert die wahrscheinlichste Vogelart zurück

3. **Frontend:**
   - Zeigt die Top-Klassen mit Prozentwerten als Fortschrittsbalken
   - Zeigt zusätzlich Projektinformationen und aktuelle Modellgenauigkeit an (dynamisch aus JSON-Datei)

4. **Deployment:**
   - Die Anwendung wird als Docker-Image gebaut und zu Docker Hub gepusht:
     ```bash
     docker buildx build --platform linux/amd64 -t yanickpfischer/mdm-birdclassification:latest --push .
     ```
   - Danach wird sie auf Azure App Service als Linux-Container bereitgestellt:
     ```bash
     az group create --name mdm-p2-bird-rg --location westeurope
     az appservice plan create --name mdm-p2-bird-plan --resource-group mdm-p2-bird-rg --sku F1 --is-linux
     az webapp create --resource-group mdm-p2-bird-rg --plan mdm-p2-bird-plan --name mdm-p2-bird-app --deployment-container-image-name yanickpfischer/mdm-birdclassification:latest
     az webapp config appsettings set --resource-group mdm-p2-bird-rg --name mdm-p2-bird-app --settings WEBSITES_PORT=8080
     ```

   - Die App ist öffentlich erreichbar unter: [https://mdm-p2-bird-app.azurewebsites.net](https://mdm-p2-bird-app.azurewebsites.net)

---

## 📁 Projektstruktur

Das Projekt folgt einer klassischen Full-Stack-Architektur mit Java Spring Boot Backend, einem Web-Frontend sowie ML-Komponenten (Training & Inference) auf Basis der DJL.

- `src/main/java/ch/zhaw/mdm/djl/birds/playground/`
  - `Training.java` – Trainingslogik mit DJL
  - `Inference.java` – Modellinferenz aus REST-Anfragen
  - `ClassificationController.java` – REST-Endpunkt für Datei-Upload und Prediction
  - `Models.java` – Laden und Konfiguration des Modells
- `src/main/resources/models/` – Enthält das trainierte Modell (`.params`) sowie die `synset.txt` mit den Labelnamen
- `src/main/resources/static/` – HTML, JavaScript und CSS für das interaktive Frontend
- `training-results.json` – Ausgabe des Trainings (Train/Val Accuracy & Loss)
- `Dockerfile` – Containerisierung für lokales Deployment und Azure Web App
- `README.md` – Projektdokumentation (diese Datei)
