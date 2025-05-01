const dropArea = document.getElementById('drop-area');
const imageInput = document.getElementById('imageUpload');
const preview = document.getElementById('preview');
const answerPart = document.getElementById('answerPart');
const answer = document.getElementById('answer');
const loadingPart = document.getElementById('loadingPart');

dropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropArea.classList.add('bg-warning');
});
dropArea.addEventListener('dragleave', () => dropArea.classList.remove('bg-warning'));
dropArea.addEventListener('drop', (e) => {
    e.preventDefault();
    dropArea.classList.remove('bg-warning');
    checkFiles(e.dataTransfer.files);
});
imageInput.addEventListener('change', () => checkFiles(imageInput.files));

function checkFiles(files) {
    if (files.length !== 1) {
        alert("Bitte genau eine Datei hochladen.");
        return;
    }

    const file = files[0];
    if ((file.size / 1024 / 1024) > 10) {
        alert("Datei zu groß (max. 10Mb)");
        return;
    }

    preview.src = URL.createObjectURL(file);
    loadingPart.style.display = "block";
    answerPart.style.visibility = "hidden";

    const formData = new FormData();
    formData.append("image", file);

    fetch('/analyze', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {

        setTimeout(() => {
            const classNames = formatClassNames(data);
            answer.innerHTML = classNames;
            loadingPart.style.display = "none";
            answerPart.style.visibility = "visible";
        }, 3000);
    })
    .catch(error => {
        console.error(error);
        loadingPart.style.display = "none";
    });
}

function formatClassNames(data) {
    let html = "";
    data.forEach(item => {
        const prob = item.probability * 100;
        if (prob < 0.01) return; // filtert 0% aus

        let colorClass = 'bg-danger';
        if (prob >= 70) colorClass = 'bg-success';
        else if (prob >= 20) colorClass = 'bg-warning';

        html += `
            <div><strong>${item.className}</strong>: ${prob.toFixed(2)}%</div>
            <div class="progress mb-2">
                <div class="progress-bar ${colorClass}" role="progressbar" style="width: ${prob.toFixed(2)}%;"
                    aria-valuenow="${prob.toFixed(2)}" aria-valuemin="0" aria-valuemax="100">${prob.toFixed(2)}%</div>
            </div>
        `;
    });
    return html;
}

// Dynamische Platzhalteranzeige für Accuracy-Werte
window.addEventListener('DOMContentLoaded', () => {
    const trainAccElement = document.getElementById('train-acc');
    const valAccElement = document.getElementById('val-acc');

    if (trainAccElement) trainAccElement.innerText = "TBD";
    if (valAccElement) valAccElement.innerText = "TBD";
});