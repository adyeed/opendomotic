var ws = null;

function connect() {
    url = 'ws://' + window.location.host + '/opendomotic-web-0.0.1/websocket';
    if ('WebSocket' in window) {
        ws = new WebSocket(url);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(url);
    } else {
        log('WebSocket is not supported by this browser.');
        return;
    }
    ws.onopen = function () {
        log('Info: Conexao aberta.');
    };
    ws.onmessage = function (event) {
        log('Recebido: ' + event.data);
    };
    ws.onclose = function () {
        log('Info: Conexao fechada.');
    };
}

function send() {
    if (ws !== null) {
        var message = document.getElementById('message').value;
        log('Enviado: ' + message);
        ws.send(message);
    } else {
        log('WebSocket connection not established, please connect.');
    }
}

function log(message) {
    document.getElementById('log').innerHTML = message;
}