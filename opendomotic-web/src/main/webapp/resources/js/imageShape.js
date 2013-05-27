function ImageShape(x, y, src) {
    this.image = new Image();
    this.image.src = src;

    this.x = x;
    this.y = y;
    this.width = 64;
    this.height = 64;

    this.getRight = function() {
        return this.x + this.width;
    };

    this.getBottom = function() {
        return this.y + this.height;
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