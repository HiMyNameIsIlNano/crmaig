function openOverlay(loadingDivId, uploadButtonId, uploadFormId) {
    document.getElementById(uploadButtonId).disabled = true;
    document.getElementById(uploadFormId).style.display = 'none';
    document.getElementById(loadingDivId).style.display = 'block';
}