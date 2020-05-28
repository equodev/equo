
//this.Pty = require("node-pty");

function getShell(port, shells){
	for(let i = 0; i < shells.length;i++){
		console.log(shells[i].getPort());
		if(shells[i].getPort()=== port){
			return shells[i];
		}
	}
	return null;
}

const WebsocketServer = require("ws").Server;
const createShell = require('./shell-model');

var shells = [];

var server = new WebsocketServer({ port: 40000 });
console.log(server._server.address().port);
server.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
 		var msg = JSON.parse(message);
  		if(msg.action === 'addShell'){
  			let newShell = createShell();
  			shells.push(newShell);

  			let response = {};
  			response.action = "addShell";
  			response.port = newShell.getPort();
  			ws.send(JSON.stringify(response));
  		}
  		if(msg.action === 'closeShell'){
  			const port = msg.params.port;
  			const shell = getShell(port,shells);
  			shell.wss.close();
  			let index = shells.indexOf(shell);
			if(index > -1) {
			  shells.splice(index, 1);
			}  			
  		}
  		if(msg.action === 'closeAllShells'){
  			for(let i = 0 ; i < shells.length;i++){
  				shells[i].wss.close();
  			}
  			shells.splice(0,shells.length);
  			console.log("All Shells have been closed.")
  		}

    console.log('received: %s', message);
    
  });
 
  ws.send('connection opened');
});

console.log("server loaded");
