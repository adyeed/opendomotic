window.onload = initUser;

function initUser() {
    initCanvas(null, mouseUpDevice);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    $.getJSON(getUrl('device/toggle?name='+imagePressed.name), null, null);
}
