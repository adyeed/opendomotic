function ImageShape(id, x, y, name, src) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.name = name;
    this.value = null;

    this.image = new Image();
    this.image.alt = name;
    this.image.src = src;

    this.getRight = function() {
        return this.x + this.image.width;
    };

    this.getBottom = function() {
        return this.y + this.image.height;
    };

    this.isIn = function(x, y) {
        return this.x <= x
                && this.getRight() >= x
                && this.y <= y
                && this.getBottom() >= y;
    };

    this.draw = function(context) {
        context.drawImage(this.image, this.x, this.y);
    };
}