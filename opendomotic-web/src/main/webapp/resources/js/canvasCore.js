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

function initCanvas(canDrag, onMouseUpDevice, idEnvironment, imageSrc) {   
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
    
    imagePlanta.src = imageSrc;
    imagePlanta.onload = function() {
        draw();
    };
    
    arrayImage = new Array(); //para limpar em caso de ajax update
    $.getJSON(getUrl('device/list?idEnvironment='+idEnvironment), null, function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index];                
                arrayImage[countImage++] = new ImageShape(d.id, d.x, d.y, d.name, d.src0, d.src1);
            }
        }
        updateValues();
    });
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

function updateValues() {
    $.getJSON(getUrl('device/value'), null, function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index]; //d.name; d.value;
                for (var i in arrayImage) {
                    if (arrayImage[i].name === d.name) {
                        arrayImage[i].value = d.value;
                    }
                }
            }
        }
        loading = false;
        draw();
    });    
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
    
    setTimeout(function() {updateValues();}, 100);
}
