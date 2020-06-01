<template>
<div>
     <div class="terminalContent">
        <v-tabs v-model="tab" class="terminalTabs"
            height="35"
            background-color="rgb(9, 22, 29)"
            dark>
            <v-tabs-slider color="#0275d8"></v-tabs-slider>
            <v-tab>
                Terminal
            </v-tab>
        </v-tabs>
    </div>
    <div>
        <div id='terminal-container'></div>
    </div>

</div>
</template>

<script>
 import './../../../../../node_modules/xterm/css/xterm.css'
 import {EquoTerminal} from './terminal.class.js' 

var socket = new WebSocket("ws://127.0.0.1:40000");

var shellPort = 0;

socket.addEventListener('open', function () {
    let msg = JSON.stringify({
                action : "addShell"
        })
    socket.send(msg,function (event) {
        console.log("callback de creacion de shell ",event);
    });
});

socket.addEventListener('message', function (event) {
    //console.log('svr msg: ', event.data);
    try {
        var msg = JSON.parse(event.data);
        if(msg.action === "addShell"){
        shellPort = msg.port;
    }
    } catch (error) { //response is not an Object, just a String
    }

});

export default {
    name: 'equo-shell',
    data(){ return {
        tab: null
            }
    },
    mounted:function(){
        setTimeout(() => { 
        let parentId = "terminal-container";
            window.terminalRemote = new EquoTerminal({
                role: "client",
                parentId: parentId,
                host: "127.0.0.1",
                port: shellPort
        });
         }, 250);
    },
    destroyed: function(){
        let msg = JSON.stringify({
            action: "closeAllShells"
        });
        socket.send(msg);   
    }
}
</script>
<style>
.terminalContent{
    display: flex;
    overflow: hidden;
    flex-wrap: unset;
    flex: 1 1 auto;
}

.shell{
    background-color: rgb(20, 22, 29);
    height: 270px;
    width: 500px;
}

</style>