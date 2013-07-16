//window.onload = initAdmin;

function initAdmin() {
    
}

function drawCanvas(idEnvironment) {
    initCanvas(true, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {  
    $.getJSON(getUrl(
            'device/move?id='+imagePressed.id+
            '&x='+imagePressed.x+
            '&y='+imagePressed.y), null, null);
}
