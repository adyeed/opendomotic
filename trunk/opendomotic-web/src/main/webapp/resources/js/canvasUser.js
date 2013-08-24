window.onload = initUser;

function initUser() {
    connect();
}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    if (devicePressed.switchable) {
        deviceName = devicePressed.name;
        $.getJSON(getUrl('device/switch?name='+deviceName), null, function(data) {
            newValue = data;
            updateDeviceValue(deviceName, newValue);
            draw();
        }); 
    } else {
        alert(devicePressed.name + ' = ' + devicePressed.value);
    }
}
