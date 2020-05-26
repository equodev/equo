module.exports = function createShell(port){

					const Terminal = require("./terminal.class.js").Terminal;
					let terminalServer = new Terminal({
					    role: "server",
					    shell: (process.platform === "win32") ? "cmd.exe" : "bash",
					    port: port
					});

//					terminalServer.onclosed = (code, signal) => {
//					    console.log("Terminal closed - "+code+", "+signal);
//					    app.quit();
//					};
					terminalServer.onopened = () => {
					    console.log("Connected to remote");
					};
					terminalServer.onresized = (cols, rows) => {
					    console.log("Resized terminal to "+cols+"x"+rows);
					};
					terminalServer.ondisconnected = () => {
					    console.log("Remote disconnected");
					};

					console.log("Shell Server has been created");

					return terminalServer;
				}




