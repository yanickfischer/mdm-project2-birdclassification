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

---

## ⚙️ Funktionsweise

1. **Training:**
   - Das Modell wird auf einem reduzierten Subset (20 Klassen) des CUB-Datasets trainiert.
   - Die Trainingsgenauigkeit und Validierungsgenauigkeit werden nach dem Training automatisch in einer JSON-Datei (`training-result.json`) gespeichert.

2. **Inference (Vorhersage):**
   - Der Benutzer lädt ein Vogelbild im Browser hoch.
   - Das Bild wird per REST-API an das Spring Boot Backend gesendet.
   - Das Modell analysiert das Bild und liefert die wahrscheinlichste Vogelart zurück.

3. **Frontend:**
   - Zeigt die Top-Klassen mit Prozentwerten als Fortschrittsbalken.
   - Zeigt zusätzlich Projektinformationen und aktuelle Modellgenauigkeit an (dynamisch aus JSON-Datei).

---

## 📁 Projektstruktur