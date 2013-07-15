window.onload = initUser;

function initUser() {
    initCanvas(null, mouseUpDevice, 2, "../resources/images/planta.jpg");
    connect();
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    $.getJSON(getUrl('device/toggle?name='+imagePressed.name), null, null);
}
