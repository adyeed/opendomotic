var canvas, context;
var xMouseDown, yMouseDown;

var environmentImage = new Image();

var deviceArray = new Array();
var deviceCount = 0;
var devicePressed = null;
var deviceTooltip = null;

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
    canvas.height = 500;
    context = canvas.getContext("2d"); 
    context.font = "bold 26px verdana";
    drawLoading();
    
    $.getJSON(getUrl('environment?id='+idEnvironment), function(data) {
        for (var environment in data) {
            environmentImage.src = data[environment].fileName;
            environmentImage.onload = function() {
                canvas.width = environmentImage.width;
                canvas.height = environmentImage.height;
                context.font = "bold 26px verdana";
                context.shadowColor = 'white';
                context.shadowBlur = 5;
                draw();
            };

            //para limpar em caso de ajax update:
            deviceArray = new Array(); 
            deviceCount = 0;
            
            list = data[environment].listDevicePositionRest; //returns array only when > 1
            if (list instanceof Array) {
                for (var index in list) {
                    addDevice(list[index]);                    
                }
            } else if (list !== undefined) {
                addDevice(list);
            }                
        }
        updateDeviceValues();
    });
}

function addDevice(p) {
    deviceArray[deviceCount++] = new Device(p.id, p.x, p.y, p.name, p.switchable, p.imageDefault, p.imageSwitch);
}

function draw() {
    //clear canvas
    context.clearRect(0, 0, canvas.width, canvas.height); 

    //fundo
    context.drawImage(environmentImage, 0, 0);

    if (loading) {
        drawLoading();
    }

    //draw devices
    for (var i in deviceArray) {
        device = deviceArray[i];
        device.draw(context);
        if (device.value !== null) {
            context.fillText(device.value, device.x, device.getBottom());
        }
    }

    //draw pressed device over all
    if (devicePressed !== null) {
        devicePressed.draw(context);
    }
}

function drawLoading() {
    context.fillText("carregando...", 10, 30);
}

function updateDeviceValues() {
    drawLoading();
    $.getJSON(getUrl('device/value'), function(data) {
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
    for (var i in deviceArray) {
        if (deviceArray[i].name === name) {
            deviceArray[i].value = value;
            //break; não posso dar break pq talvez tenha outras imagens para o mesmo device
        }
    }
}

function checkTooltip(x, y) {
    image = getImage(x, y);
    if (image !== null) {
        if (deviceTooltip === null) {
            context.save();
            context.beginPath();
            textDimensions = context.measureText(image.name);  
            context.rect(x, y, textDimensions.width+10, 30);
            context.globalAlpha = 0.5;           
            context.fillStyle = '99CCFF';
            context.fill();
            context.lineWidth = 1;
            context.strokeStyle = 'black';
            context.stroke();
            context.closePath();
            
            context.globalAlpha = 1;
            context.textBaseline="top";
            context.fillStyle = 'black';
            context.fillText(image.name, x+5, y);
            
            context.restore();
        }
        deviceTooltip = image;
        
    } else if (deviceTooltip !== null) {
        deviceTooltip = null;
        draw();
    }
}

function getImage(x, y) {
    for (var i in deviceArray) {
        if (deviceArray[i].isIn(x, y)) {
            return deviceArray[i];
        }
    }
    return null;
}

//------------------------------------------------------------------------------

function mouseDown(e) {
    var x = e.layerX;//e.pageX - canvas.offsetLeft;
    var y = e.layerY;//e.pageY - canvas.offsetTop;

    image = getImage(x, y);
    if (image !== null) {
        devicePressed = image;
        xMouseDown = x - image.x;
        yMouseDown = y - image.y;
        
        if (deviceTooltip !== null) {
            deviceTooltip = null;
            draw();
        }
        
        context.fillText("aguarde...", image.x, image.y);
    }
}

function mouseMove(e) {
    if (canDrag && devicePressed !== null) {
        devicePressed.x = e.layerX - xMouseDown;
        devicePressed.y = e.layerY - yMouseDown;
        draw();
    } else if (devicePressed === null) {
        setTimeout(
            function() {
                checkTooltip(e.layerX, e.layerY);
            },  
            500);
    }
}

function mouseUp(e) {   
    if (devicePressed !== null && onMouseUpDevice !== null) {
        onMouseUpDevice();
    }
    devicePressed = null;
}
