function displayLoadingSpinner(loadingDivId, uploadButtonId, containerToHideId, formToPostId) {
    document.getElementById(uploadButtonId).disabled = true;
    document.getElementById(containerToHideId).style.display = 'none';
    document.getElementById(loadingDivId).style.display = 'block';
    document.getElementById(formToPostId).submit();
}