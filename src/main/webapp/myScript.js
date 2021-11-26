var check = function() {
    if (document.getElementById('pass').value ==
        document.getElementById('pass-repeat').value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = "<fmt:message key='label.match'/>";
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = "<fmt:message key='label.notmatch'/>";
    }
}
function onlyNumberKey(evt) {

    var ASCIICode = (evt.which) ? evt.which : evt.keyCode
    if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
        return false;
    return true;
}