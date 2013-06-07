window.onload = initAdmin;

function initAdmin() {
    initCanvas(true, mouseUpDevice);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {  
    $.getJSON(getUrl(
            'device/move?id='+imagePressed.id+
            '&x='+imagePressed.x+
            '&y='+imagePressed.y), null, null);
}
