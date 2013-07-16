//window.onload = initUser;

function initUser() {

}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
    connect();
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    $.getJSON(getUrl('device/toggle?name='+imagePressed.name), null, null);
}
