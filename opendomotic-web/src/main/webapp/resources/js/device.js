function Device(id, x, y, name, switchable, imageDefault, imageSwitch) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.name = name;
    this.switchable = switchable;
    this.value = null;   

    this.imageDefault = new Image();
    this.imageDefault.alt = name;
    this.imageDefault.src = imageDefault;
    
    if (imageSwitch !== undefined && imageSwitch !== '') {
        this.imageSwitch = new Image();
        this.imageSwitch.alt = name;
        this.imageSwitch.src = imageSwitch;
    } else {
        this.imageSwitch = null;
    }

    this.getRight = function() {
        return this.x + this.getImage().width;
    };

    this.getBottom = function() {
        return this.y + this.getImage().height;
    };
    
    this.getImage = function() {
        if (this.imageSwitch !== null && (this.value === 1 || this.value === '1')) {
            return this.imageSwitch;
        }
        return this.imageDefault;
    };

    this.isIn = function(x, y) {
        return this.x <= x
                && this.getRight() >= x
                && this.y <= y
                && this.getBottom() >= y;
    };

    this.draw = function(context, isPressed) {       
        context.save();
        
        if (isPressed) {
            context.shadowColor = 'yellow';
            context.shadowBlur = 20;
        }
             
        try {
            context.drawImage(this.getImage(), this.x, this.y);
        } catch(err) {
            //image not found:
            this.getImage().width = 64;
            this.getImage().height = 64;
            
            //draw rect:
            context.beginPath();
            context.lineWidth = 1;
            context.strokeStyle = 'black';
            context.rect(this.x, this.y, 64, 64);
            context.stroke();        
        }
                
        context.lineWidth = 1; 
        context.strokeStyle = 'white';
        
        text = this.value;        
        if (text !== null) { //is null when loading
            text = text.toString();
            
            if ((text !== 'null') && this.switchable) { //is 'null' string when has comunication error   
                text = (this.value === 1 || this.value === '1') ? 'on' : 'off';
            }
            
            lines = text.split('\\r\\n');
            for (var i in lines) {
                context.strokeText(lines[i], this.x, i * context.fontSize + this.getBottom());
                context.fillText(lines[i], this.x, i * context.fontSize + this.getBottom());         
            };                 
        }

        context.restore();
    };

}