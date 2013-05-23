var SHAPE_HEIGHT = 30;
var SHAPE_VERTICAL_DIV_SPACE = 10;

var SHAPE_MAQUINA_TOP = 20;
var SHAPE_MAQUINA_LEFT = 10;
var SHAPE_MAQUINA_WIDTH = 150;
var SHAPE_MAQUINA_RIGHT = SHAPE_MAQUINA_LEFT + SHAPE_MAQUINA_WIDTH;
var SHAPE_MAQUINA_COLOR1 = "white";
var SHAPE_MAQUINA_COLOR2 = "gray";
var SHAPE_MAQUINA_FONT_COLOR = "black";

function Shape(indexMaquina, left, width, descricao, color1, color2, fontColor) {   
    this.x = left;
    this.y = SHAPE_MAQUINA_TOP + (SHAPE_HEIGHT + SHAPE_VERTICAL_DIV_SPACE) * indexMaquina;
    this.width = width;
    this.height = SHAPE_HEIGHT;
    this.descricao = descricao;
    this.color1 = color1;
    this.color2 = color2;
    this.fontColor = fontColor;
    
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
}