//window.onload = initAdmin;

function initAdmin() {
    initCanvas(true, mouseUpDevice);
}

function drawCanvas(idEnvironment, imageSrc) {
    initCanvas(true, mouseUpDevice, idEnvironment, "../resources/images/"+imageSrc);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {  
    $.getJSON(getUrl(
            'device/move?id='+imagePressed.id+
            '&x='+imagePressed.x+
            '&y='+imagePressed.y), null, null);
}
