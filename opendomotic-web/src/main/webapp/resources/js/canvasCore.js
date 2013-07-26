var canvas, context;
var xMouseDown, yMouseDown;

var imagePlanta = new Image();
var arrayImage = new Array();
var countImage = 0;
var canDrag = false;
var onMouseUpDevice = null;
var loading = true;

function getUrl(path) {
    return '/opendomotic-web-0.0.1/' + path;
}

function initCanvas(canDrag, onMouseUpDevice, idEnvironment) {   
    this.canDrag = canDrag;
    this.onMouseUpDevice = onMouseUpDevice;
    
    canvas = document.getElementById("canvas");    
    canvas.onmousedown = mouseDown;
    canvas.onmousemove = mouseMove;
    canvas.onmouseup = mouseUp;
    canvas.width = 1000;
    canvas.height = 950;
    context = canvas.getContext("2d");    
    context.font = "bold 26px verdana";
    drawLoading();
    
    $.getJSON(getUrl('environment?id='+idEnvironment), null, function(data) {
        for (var environment in data) {
            imagePlanta.src = data[environment].fileName;
            imagePlanta.onload = function() {
                draw();
            };

            //para limpar em caso de ajax update:
            arrayImage = new Array(); 
            countImage = 0;
            
            list = data[environment].listDevicePositionRest; //returns array only when > 1
            if (list instanceof Array) {
                for (var index in list) {
                    addDevicePosition(list[index]);                    
                }
            } else if (list !== undefined) {
                addDevicePosition(list);
            }                
        }
        updateDeviceValues();
    });
}

function addDevicePosition(p) {
    arrayImage[countImage++] = new ImageShape(p.id, p.x, p.y, p.name, p.src0, p.src1);
}

function draw() {
    //clear canvas
    context.clearRect(0, 0, canvas.width, canvas.height); 

    //fundo
    context.drawImage(imagePlanta, 0, 0);

    if (loading) {
        drawLoading();
    }

    //desenha devices
    for (var i in arrayImage) {
        arrayImage[i].draw(context);
        if (arrayImage[i].value !== null) {
            context.fillText(arrayImage[i].value, arrayImage[i].x, arrayImage[i].getBottom());
        }
    }

    //para desenhar por cima
    if (imagePressed !== null) {
        imagePressed.draw(context);
    }
}

function drawLoading() {
    context.fillText("carregando...", 10, 30);
}

function updateDeviceValues() {
    drawLoading();
    $.getJSON(getUrl('device/value'), null, function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index]; //d.name; d.value;
                updateDeviceValue(d.name, d.value);
            }
        }
        loading = false;
        draw();
    });    
}

function updateDeviceValue(name, value) {
    for (var i in arrayImage) {
        if (arrayImage[i].name === name) {
            arrayImage[i].value = value;
            //break; não posso dar break pq talvez tenha outras imagens para o mesmo device
        }
    }
}

//------------------------------------------------------------------------------

function mouseDown(e) {
    var x = e.layerX;//e.pageX - canvas.offsetLeft;
    var y = e.layerY;//e.pageY - canvas.offsetTop;

    for (var i in arrayImage) {
        if (arrayImage[i].isIn(x, y)) {
            imagePressed = arrayImage[i];
            xMouseDown = x - imagePressed.x;
            yMouseDown = y - imagePressed.y;
            context.fillText("aguarde...", imagePressed.x, imagePressed.y);
            break;
        }
    }
}

function mouseMove(e) {
    if (canDrag && imagePressed !== null) {
        imagePressed.x = e.layerX - xMouseDown;
        imagePressed.y = e.layerY - yMouseDown;
        draw();
    }
}

function mouseUp(e) {   
    if (imagePressed !== null && onMouseUpDevice !== null) {
        onMouseUpDevice();
    }
    imagePressed = null;
    
    //já é notificado por websocket, entao nao precisa disso:
    //setTimeout(function() {updateDeviceValues();}, 500);
}
