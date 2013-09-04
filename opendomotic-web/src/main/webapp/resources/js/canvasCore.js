var canvas, context;
var xMouseDown, yMouseDown;

var environmentImage = new Image();

var deviceArray = new Array();
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
    
    $.getJSON(getUrl('rest/environment/get?id='+idEnvironment), function(data) {
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
            
            list = data[environment].listDevicePositionRest; //returns array only when > 1
            if (list instanceof Array) {
                for (var index in list) {
                    addDevice(list[index]);                    
                }
            } else if (list !== undefined) {
                addDevice(list);
            }                
            
            if (deviceArray.length > 0) {
                deviceArray[deviceArray.length-1].imageDefault.onload = function() {
                    draw();
                };
            }
        }
        updateDeviceValues();
    });
}

function addDevice(p) {
    deviceArray.push(new Device(p.id, p.x, p.y, p.name, p.switchable, p.imageDefault, p.imageSwitch));
}

function draw() {
    context.clearRect(0, 0, canvas.width, canvas.height); 
    context.drawImage(environmentImage, 0, 0);

    if (loading) {
        drawLoading();
    }

    drawDevices();

    //draw pressed device over all:
    if (devicePressed !== null) {
        devicePressed.draw(context);
    }
}

function drawLoading() {
    context.fillText("carregando...", 10, 30);
}

function drawDevices() {
    context.save();
    context.lineWidth = 1; 
    context.strokeStyle = 'white';
    for (var i in deviceArray) {
        device = deviceArray[i];
        device.draw(context);
        if (device.value !== null) {
            context.strokeText(device.value, device.x, device.getBottom());
            context.fillText(device.value, device.x, device.getBottom());              
        }
    }
    context.restore();
}

function updateDeviceValues() {
    drawLoading();
    $.getJSON(getUrl('rest/device/value'), function(data) {
        for (var device in data) {
            for (var index in data[device]) {
                d = data[device][index]; //d.name; d.value;
                updateDeviceValue(d.name, d.value, false);
            }
        }
        loading = false;
        draw();
    });    
}

function updateDeviceValue(name, value, canDraw) {
    for (var i in deviceArray) {
        if (deviceArray[i].name === name) {
            deviceArray[i].value = value;
            //break; não posso dar break pq talvez tenha outras imagens para o mesmo device
        }
    }
    if (canDraw) {
        draw();
    }
}

function checkTooltip(x, y) {
    device = getDeviceByXY(x, y);
    if (device !== null) {
        if (deviceTooltip === null) {
            //background:
            context.save();
            context.beginPath();
            textDimensions = context.measureText(device.name);  
            context.rect(x, y, textDimensions.width+10, 30);
            context.globalAlpha = 0.5;           
            context.fillStyle = '#99CCFF';
            context.fill();
            
            //border:
            context.lineWidth = 1;
            context.strokeStyle = 'black';
            context.stroke();
            context.closePath();
            
            //text:
            context.globalAlpha = 1;
            context.textBaseline="top";
            context.fillStyle = 'black';
            context.textBaseline = 'bottom';
            context.fillText(device.name, x+5, y+30);
            
            context.restore();
        }
        deviceTooltip = device;
        
    } else if (deviceTooltip !== null) {
        deviceTooltip = null;
        draw();
    }
}

function getDeviceByXY(x, y) {
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

    device = getDeviceByXY(x, y);
    if (device !== null) {
        devicePressed = device;
        xMouseDown = x - device.x;
        yMouseDown = y - device.y;
        
        if (deviceTooltip !== null) {
            deviceTooltip = null;
            draw();
        }
        
        context.fillText("aguarde...", device.x, device.y);
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
