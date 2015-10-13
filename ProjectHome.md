## Open source home automation with Java EE, Raspberry Pi and Arduino ##
### Automação residencial open source com Java EE, Raspberry Pi e Arduino ###

**Server**: Apache TomEE 1.5.2 on Raspberry Pi, PC or Mac. Also runs on Glassfish, but is heavy for the Raspberry Pi.<br>
<b>Clients</b>: Arduino or another hardware communicating via ethernet (HTTP), serial (~ModBus) or I²C. Another protocol implementation can be easily added and instantiated by the server with Java Reflection.<br>

Questions? Please send me an e-mail: jaques.claudino at gmail.com<br>
<br>
<hr />

<b>Server Demo:</b>
<ol><li>Download and unzip: <a href='https://dl.dropboxusercontent.com/u/41795706/opendomotic/demo/server/apache-tomee-jaxrs-1.5.2-opendomotic-0.0.2.zip'>https://dl.dropboxusercontent.com/u/41795706/opendomotic/demo/server/apache-tomee-jaxrs-1.5.2-opendomotic-0.0.2.zip</a>
</li><li>Run apache-tomee-jaxrs-1.5.2/bin/startup.bat or startup.sh;<br>
</li><li>Wait TomEE startup and enter <a href='http://localhost:8080/opendomotic-web-0.0.2'>http://localhost:8080/opendomotic-web-0.0.2</a>
</li><li>On the authentication request, enter user "admin" and password "admin";<br>
</li><li>The devices values will be "null" because can't communicate with the client. So, the next step is build or configure your client.</li></ol>

<hr />

<b>Client Demo:</b> (Arduino with W5100 Ethernet Shield)<br>
<ol><li>Download and unzip: <a href='https://dl.dropboxusercontent.com/u/41795706/opendomotic/demo/client/ArduinoDemoW5100.zip'>https://dl.dropboxusercontent.com/u/41795706/opendomotic/demo/client/ArduinoDemoW5100.zip</a>
</li><li>Open the project on Arduino IDE or NetBeans;<br>
</li><li>On the source, change the IP address of your Arduino client (ex 192.168.10.10) and the TomEE server (ex 192.168.10.5). Also, on opendomotic-web device screen, update the new IP address;<br>
</li><li>Upload the sketch to Arduino.</li></ol>

<hr />

<b>Another examples:</b> (Android and Arduino projects)<br>
<a href='https://www.dropbox.com/sh/4tcyes80icdu0i9/AABNyk_FeW_jzitTUB9csxYUa/opendomotic/examples'>https://www.dropbox.com/sh/4tcyes80icdu0i9/AABNyk_FeW_jzitTUB9csxYUa/opendomotic/examples</a>

<hr />

<a href='http://www.youtube.com/watch?feature=player_embedded&v=tkOjie6b8YA' target='_blank'><img src='http://img.youtube.com/vi/tkOjie6b8YA/0.jpg' width='425' height=344 /></a><br>
<br>
<hr />

<b>Screenshots:</b>

The home screen uses HTML5 Canvas to show devices and their values. Switchable devices can be turned on or off by a click. The Java EE application notifies the changed values ​​using HTML5 WebSocket:<br>
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140322-opendomotic-principal.png' />
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140406-opendomotic-raspberry.png' />
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140406-opendomotic-barton.png' />
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20130920-opendomotic-home.png' />

Tasks can be added to turn on or off devices:<br>
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140406-opendomotic-job.png' />

Use drag-and-drop to position the device over the environment. More environments can be added:<br>
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20131118-opendomotic-environment.png' />

Devices can show the history graph:<br>
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20131116-opendomotic-history.png' />

The Java EE application uses reflection to instantiate the devices. Implementations with HTTP, Serial or I²C are provided. Another device can be easily implemented and added:<br>
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140406-opendomotic-config1.png' />
<img src='https://dl.dropboxusercontent.com/u/41795706/opendomotic/screenshot/20140406-opendomotic-config2.png' />