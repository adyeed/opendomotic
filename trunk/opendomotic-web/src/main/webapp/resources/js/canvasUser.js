window.onload = initUser;

function initUser() {
    connect();
}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    $.getJSON(getUrl('device/toggle?name='+imagePressed.name), null, null);
}
