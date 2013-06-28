function ImageShape(id, x, y, name, src0, src1) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.name = name;
    this.value = null;

    //TO-DO: array de Image-Value
    this.image0 = new Image();
    this.image0.alt = name;
    this.image0.src = src0;

    if (src1 !== undefined && src1 !== '') {
        this.image1 = new Image();
        this.image1.alt = name;
        this.image1.src = src1;
    } else {
        this.image1 = null;
    }

    this.getRight = function() {
        return this.x + this.getImage().width;
    };

    this.getBottom = function() {
        return this.y + this.getImage().height;
    };
    
    this.getImage = function() {
        if (this.image1 !== null && this.value === 1)
            return this.image1;
        return this.image0;
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