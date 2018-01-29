function findBySpecificField(endpoint, inputFieldId, outputFieldId){

    var url;
    const fieldValue = document.getElementById(inputFieldId).value;

    if (fieldValue !== '') {
        url = '/' + endpoint + '/' + fieldValue;
    } else {
        url = '/' + endpoint + '/';
    }

    $('#' + outputFieldId).load(url);
}
