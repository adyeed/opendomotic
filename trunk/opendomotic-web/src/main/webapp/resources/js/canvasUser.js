window.onload = initUser;

function initUser() {
    initCanvas(null, mouseUpDevice);
    connect();
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    $.getJSON(getUrl('device/toggle?name='+imagePressed.name), null, null);
}
