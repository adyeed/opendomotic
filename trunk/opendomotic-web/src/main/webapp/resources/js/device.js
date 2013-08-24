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
        if (this.imageSwitch !== null && this.value === 1)
            return this.imageSwitch;
        return this.imageDefault;
    };

    this.isIn = function(x, y) {
        return this.x <= x
                && this.getRight() >= x
                && this.y <= y
                && this.getBottom() >= y;
    };

    this.draw = function(context) {
        context.drawImage(this.getImage(), this.x, this.y);
    };

}