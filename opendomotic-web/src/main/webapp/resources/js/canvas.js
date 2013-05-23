var SITUACAO_PLANEJADO = 0;
var SITUACAO_PROGRAMADO = 1;
var SITUACAO_INICIADO = 2;
var SITUACAO_FINALIZADO = 3;

var canvas, context;
var shapesProg = new Array();
var shapePressed = null;
var xMouseDown, yMouseDown;
var xPan = 0;

var imagePlanta = new Image();
var arrayImage = new Array();
var i = 0;

window.onload = init;

function init() {
    canvas = document.getElementById("canvasTeste");
    canvas.onmousedown = mouseDown;
    canvas.onmousemove = mouseMove;
    canvas.onmouseup = mouseUp;
    context = canvas.getContext("2d");

    canvas.width = 1000;
    canvas.height = 950;

    /*shapesProg[0] = new Shape(
            0, //maq 
            100, //width
            100, //height
            "123",
            "yellow",
            "yellow",
            "black");

    shapesProg[1] = new Shape(
            0, //maq 
            200, //width
            200, //height
            "456",
            "yellow",
            "yellow",
            "black");*/


    //quartos
    arrayImage[i++] = new ImageShape(820, 170, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(710, 480, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(590, 750, "./resources/images/lampada.png");
    
    //sala
    arrayImage[i++] = new ImageShape(300, 400, "./resources/images/lampada.png");
    arrayImage[i++] = new ImageShape(300, 750, "./resources/images/lampada.png");
    
    //sacada
    arrayImage[i++] = new ImageShape(150, 150, "./resources/images/termometro.png");
    arrayImage[i++] = new ImageShape(250, 150, "./resources/images/umidade.png");

    imagePlanta.src = "./resources/images/planta.jpg";
    imagePlanta.onload = function() {
        draw(); //demora para carregar a imagem. Verificar se precisa para as outras
    };

    //draw();
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
    //var lingrad = context.createLinearGradient(0, canvas.height, 1000, canvas.height);
    //lingrad.addColorStop(0, "silver");
    //lingrad.addColorStop(1, "white");
    //context.fillStyle = lingrad;
    //context.fillRect(0, 0, 1000, canvas.height); 

    //clear canvas
    //context.clearRect(-1, -1, canvas.width+1, canvas.height+1); 

    context.drawImage(imagePlanta, 0, 0);

    for (var i in shapesProg)
        drawShape(shapesProg[i]);

    if (shapePressed !== null)
        drawShape(shapePressed);

    for (var i in arrayImage) {
        arrayImage[i].draw(context);
    }

    //para desenhar por cima
    if (imagePressed !== null) {
        imagePressed.draw(context);
    }

    //drawRegua();
}

function drawShape(shape) {
    //fundo
    var lingrad = context.createLinearGradient(shape.x, shape.y, shape.x, shape.getBottom());
    lingrad.addColorStop(0, shape.color1);
    lingrad.addColorStop(1, shape.color2);
    context.fillStyle = lingrad;
    context.fillRect(shape.x, shape.y, shape.width, shape.height);

    //if (shape.width > 5 && shapePressed === null) {
    //save() allows us to save the canvas context before defining the clipping region so that we can return to the default state later on
    context.save();
    context.rect(shape.x, shape.y, shape.width, shape.height);
    context.clip(); //para texto não ultrapassar a área do shape

    //texto
    context.fillStyle = shape.fontColor;
    context.font = "16px Arial";
    context.fillText(shape.descricao, shape.x + 4, shape.getBottom() - 9);

    //restores the canvas context to its original state before we defined the clipping region
    context.restore();
    //}

    //borda
    context.strokeStyle = "rgb(50,50,50)";
    context.strokeRect(shape.x, shape.y, shape.width, shape.height);
}

//------------------------------------------------------------------------------

function mouseDown(e) {
    var x = e.layerX;//e.pageX - canvas.offsetLeft;
    var y = e.layerY;//e.pageY - canvas.offsetTop;

    for (var i in shapesProg) {
        if (shapesProg[i].isIn(x, y)) {
            shapePressed = shapesProg[i];
            xMouseDown = x - shapePressed.x;
            yMouseDown = y - shapePressed.y;
            break;
        }
    }

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
    if (shapePressed !== null) {
        shapePressed.x = e.layerX - xMouseDown;
        shapePressed.y = e.layerY - yMouseDown;
        draw();
    }

    if (imagePressed !== null) {
        imagePressed.x = e.layerX - xMouseDown;
        imagePressed.y = e.layerY - yMouseDown;
        draw();
    }
}

function mouseUp(e) {
    shapePressed = null;
    imagePressed = null;
    draw();
}

function translate(x, y) {
    xPan -= x;
    context.translate(x, y);
    draw();
}

function scale(valor) {
    context.scale(valor, valor);
    draw();
}

function saveContext() {
    context.save();
    draw();
}

function restoreContext() {
    context.restore();
    draw();
}




//------------------------------------------------------------------------------------



/*
 function loadMaqs() {
 var ajaxUrl = '/gestum/services/maquina';
 //var ajaxUrl = 'maquina.json';
 $.getJSON(ajaxUrl, null, function(data) {
 maqsRest = data;
 var maxBottom = 0;
 for (var i in data) {
 shapesMaq[i] = new Shape(i, 
 SHAPE_MAQUINA_LEFT, 
 SHAPE_MAQUINA_WIDTH, 
 data[i].nome, 
 SHAPE_MAQUINA_COLOR1, 
 SHAPE_MAQUINA_COLOR2, 
 SHAPE_MAQUINA_FONT_COLOR);      
 maxBottom = shapesMaq[i].getBottom();     
 }
 canvas.height = maxBottom;
 draw();
 loadProgs(data);        
 });
 }
 
 function loadProgs(maqsRest) {
 var ajaxUrl = '/gestum/services/gantt?inicio=' + document.getElementById("form:inicio_input").value;
 //var ajaxUrl = 'gantt.json';
 $.getJSON(ajaxUrl, null, function(data) {
 var maxRight = 1000;
 for (var i in data) {         
 shapesProg[i] = new Shape(
 getIndexMaquina(maqsRest, data[i].maquina), 
 data[i].left, 
 data[i].width, 
 data[i].descricao, 
 getShapeColor1(data[i].situacao), 
 getShapeColor2(data[i].situacao), 
 getShapeFontColor(data[i].situacao));
 
 if (shapesProg[i].getRight() > maxRight)
 maxRight = shapesProg[i].getRight();
 }        
 canvas.width = Math.min(30000, maxRight);
 draw();
 });
 }
 
 function getShapeColor1(situacao) {
 switch (situacao) {
 case SITUACAO_PLANEJADO: return "blue";
 case SITUACAO_INICIADO: return "lime";
 case SITUACAO_FINALIZADO: return "green";
 default: return "yellow"; 
 }      
 }
 
 function getShapeColor2(situacao) {
 switch (situacao) {
 case SITUACAO_PLANEJADO: return "navy";
 case SITUACAO_INICIADO: return "green";
 case SITUACAO_FINALIZADO: return "black";
 default: return "rgb(128,128,0)"; 
 }      
 }
 
 function getShapeFontColor(situacao) {
 switch (situacao) {
 case SITUACAO_PLANEJADO: return "white";
 case SITUACAO_INICIADO: return "black";
 case SITUACAO_FINALIZADO: return "white";
 default: return "black"; 
 }      
 }
 
 function getIndexMaquina(maqsRest, idMaquina) {
 for (var i in maqsRest)
 if (maqsRest[i].idMaquina === idMaquina)
 return i;
 return -1;
 }
 
 function drawRegua() {
 //fundo
 var width = SHAPE_MAQUINA_RIGHT + 24*60*2;
 var height = 18;
 var lingrad = context.createLinearGradient(0, 0, 0, height);
 lingrad.addColorStop(0, "white");
 lingrad.addColorStop(1, "silver");
 context.fillStyle = lingrad;
 context.clearRect(0, 0, width, height);
 context.fillRect(0, 0, width, height);
 
 //texto
 context.fillStyle = "black";
 context.font = "12px Arial";
 
 var hora;
 for (var i=0; i<48; i++) {
 hora = i % 24 + ":00";
 if (hora.length === 4)
 hora = "0" + hora;
 context.fillText(hora, SHAPE_MAQUINA_RIGHT + i*60, 12);
 }
 }*/
