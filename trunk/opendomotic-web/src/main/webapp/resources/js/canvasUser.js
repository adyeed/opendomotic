window.onload = initUser;

function initUser() {
    connect();
}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    deviceName = imagePressed.name;
    $.getJSON(getUrl('device/toggle?name='+deviceName), null, function(data) {
        newValue = data;
        updateDeviceValue(deviceName, newValue);
        draw();
    }); 
}
