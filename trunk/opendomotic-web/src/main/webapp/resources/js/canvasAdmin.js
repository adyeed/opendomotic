var canvas, context;
var xMouseDown, yMouseDown;
var xPan = 0;

var imagePlanta = new Image();
var arrayImage = new Array();
var countImage = 0;

window.onload = init;

function init() {
    canvas = document.getElementById("canvasAdmin");
    canvas.onmousedown = mouseDown;
    canvas.onmousemove = mouseMove;
    canvas.onmouseup = mouseUp;
    canvas.width = 1000;
    canvas.height = 950;
    context = canvas.getContext("2d");    

    imagePlanta.src = "./resources/images/planta.jpg";
    imagePlanta.onload = function() {
        draw(); //demora para carregar a imagem.
    };

    var ajaxUrl = '/opendomotic-web-0.0.1/device';
    $.getJSON(ajaxUrl, null, function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index];
                arrayImage[countImage++] = new ImageShape(d.x, d.y, d.src);
            }
        }
        if (countImage > 0) {
            arrayImage[countImage-1].onload = function() {
                draw(); //demora para carregar a imagem.
            };
        }
    });
}

function addLuz() {
    arrayImage[countImage++] = new ImageShape(0, 0, "./resources/images/lampada.png");
    draw();
}

function addTermometro() {
    arrayImage[countImage++] = new ImageShape(0, 0, "./resources/images/termometro.png");
    draw();
}

function addUmidade() {
    arrayImage[countImage++] = new ImageShape(0, 0, "./resources/images/umidade.png");
    draw();
}

function draw() {
    //clear canvas
    context.clearRect(0, 0, canvas.width, canvas.height); 

    //fundo
    context.drawImage(imagePlanta, 0, 0);

    //desenha devices
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
