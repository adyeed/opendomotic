window.onload = initUser;

function initUser() {
    connect();
}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    switch (devicePressed.type) {            
        case DeviceType.VALUE:
            newValue = prompt(devicePressed.name, devicePressed.value);
            if (newValue !== null) {
                $.getJSON(getUrl('rest/device/set?name=' + devicePressed.name + '&value='+newValue), null, null);
            }            
            break;
        
        case DeviceType.SWITCH_CONFIRM:
            switched = devicePressed.value === '1' || devicePressed.value === 1;
            if (!confirm((switched ? 'Desligar ' : 'Ligar ') + devicePressed.name + '?'))
                break;
        
        case DeviceType.SWITCH:
            $.getJSON(getUrl('rest/device/switch?name=' + devicePressed.name), null, null);
            break;
        
        default: //DeviceType.SENSOR:
            alert(devicePressed.name + ' = ' + devicePressed.value);
            break;
    }
    
}
