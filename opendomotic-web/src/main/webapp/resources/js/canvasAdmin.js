var canvas, context;
var xMouseDown, yMouseDown;
var xPan = 0;

var imagePlanta = new Image();
var arrayImage = new Array();
var i = 0;

window.onload = init;

function init() {
    canvas = document.getElementById("canvasAdmin");
    canvas.onmousedown = mouseDown;
    canvas.onmousemove = mouseMove;
    canvas.onmouseup = mouseUp;
    context = canvas.getContext("2d");

    canvas.width = 1000;
    canvas.height = 950;

    //quartos
    /*arrayImage[i++] = new ImageShape(820, 170, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(710, 480, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(590, 750, "./resources/images/lampada.png");
    
    //sala
    arrayImage[i++] = new ImageShape(300, 400, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(300, 750, "./resources/images/lampada.png");
    
    //sacada
    arrayImage[i++] = new ImageShape(150, 150, "./resources/images/termometro.png");
    arrayImage[i++] = new ImageShape(250, 150, "./resources/images/umidade.png");*/

    imagePlanta.src = "./resources/images/planta.jpg";
    imagePlanta.onload = function() {
        draw(); //demora para carregar a imagem. Verificar se precisa para as outras
    };

    var ajaxUrl = '/opendomotic-web-0.0.1/device';
    $.getJSON(ajaxUrl, null, function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index];
                arrayImage[i++] = new ImageShape(d.x, d.y, d.src);
            }
        }
        draw();
    });
}

function addLuz() {
    arrayImage[i++] = new ImageShape(0, 0, "./resources/images/lampada.png");
    draw();
}

function addTermometro() {
    arrayImage[i++] = new ImageShape(0, 0, "./resources/images/termometro.png");
    draw();
}

function addUmidade() {
    arrayImage[i++] = new ImageShape(0, 0, "./resources/images/umidade.png");
    draw();
}

function draw() {
    //clear canvas
    context.clearRect(0, 0, canvas.width, canvas.height); 

    //fundo
    context.drawImage(imagePlanta, 0, 0);

    for (var i in arrayImage) {
        arrayImage[i].draw(context);
    }

    //para desenhar por cima
    if (imagePressed !== null) {
        imagePressed.draw(context);
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
            break;
        }
    }
}

function mouseMove(e) {
    if (imagePressed !== null) {
        imagePressed.x = e.layerX - xMouseDown;
        imagePressed.y = e.layerY - yMouseDown;
        draw();
    }
}

function mouseUp(e) {
    imagePressed = null;
    draw();
}
