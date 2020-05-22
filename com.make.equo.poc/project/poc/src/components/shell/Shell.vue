<template>
<div>
    <div class="terminalContent">
        <div class="addButton">
            <v-btn v-on:click="addShell" color="#0275d8" height="35"  icon dark >
                <font-awesome-icon :icon="['fas','plus']"/>
            </v-btn>
            <v-btn v-on:click="connectShell" color="yellow" height="35"  icon dark >
                <font-awesome-icon :icon="['fas','plus']"/>
            </v-btn>
        </div>    
        <v-tabs v-model="tab" class="terminalTabs"
            height="35"
            background-color="rgb(9, 22, 29)"
            dark>
            <v-tabs-slider color="#0275d8"></v-tabs-slider>
            <v-tab v-for="n in activeShells" :key="n">
                TERMINAL {{n}}
            </v-tab>
        </v-tabs>

</div>
        <div>
            <v-tabs-items v-model="tab">
                    <v-tab-item v-for="n in activeShells" :key="n">
                        <div :id="'terminal-container-'+n"></div>
                    </v-tab-item>
            </v-tabs-items>
        </div>

</div>
</template>

<script>
 import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
 import { library } from '@fortawesome/fontawesome-svg-core'
 import { faPlus } from '@fortawesome/free-solid-svg-icons'
 import {FitAddon} from 'xterm-addon-fit'
 import {AttachAddon} from 'xterm-addon-attach'
 import './../../../../../node_modules/xterm/css/xterm.css'
 import {Terminal} from 'xterm'

library.add(
    faPlus
);

 var socket = new WebSocket("ws://127.0.0.1:3000");

socket.addEventListener('open', function () {
    //socket.send('connection requested');
});

socket.addEventListener('message', function (event) {
    console.log('svr msg: ', event.data);
});


class EquoTerminal {
    constructor (opts) {
        if (opts.role === "client") {
            if (!opts.parentId) throw "Missing options";
            this.xTerm = new Terminal({
                rows:15
            });

            let sockHost = opts.host || "127.0.0.1";
            let sockPort = opts.port || 3000;

            this.socket = new WebSocket("ws://"+sockHost+":"+sockPort);
            this.xTerm.loadAddon(new AttachAddon(this.socket));
            this.xTerm.loadAddon(new FitAddon());

            this.sendSizeToServer = () => {
                let cols = this.term.cols.toString();
                let rows = this.term.rows.toString();
                while (cols.length < 3) {
                    cols = "0"+cols;
                }
                while (rows.length < 3) {
                    rows = "0"+rows;
                }
                this.socket.send("ESCAPED|-- RESIZE:"+cols+";"+rows);
            };

            // this.term = new Terminal({
            //     cols: 80,
            //     rows: 24
            // });
            this.xTerm.open(document.getElementById(opts.parentId), true);
            this.xTerm.write('... ')

            this.socket.onopen = () => {
                this.xTerm.attach(this.socket);
            };
            this.socket.onerror = (e) => {throw e};

            this.fit = () => {
                this.xTerm.fit();
                setTimeout(() => {
                    this.sendSizeToServer();
                }, 50);
            }

            this.resize = (cols, rows) => {
                this.xTerm.resize(cols, rows);
                this.sendSizeToServer();
            }
        } 
    }
}
 
export default {
    name: 'equo-shell',
    components: {FontAwesomeIcon},
    data(){ return {
            activeShells : 0,
            shellPort : 0,
            tab: null,
            }
    },
    methods:{
        addShell(){
            this.activeShells++;
            this.shellPort++;

        },
        connectShell(){
            let msg = JSON.stringify({
                action : "addShell",
                port: 3000 + this.shellPort
            })
            socket.send(msg);

            let parentId = "terminal-container-"+this.activeShells;
            window.terminalRemote = new EquoTerminal({
                role: "client",
                parentId: parentId,
                host: "127.0.0.1",
                port: 3000+ this.shellPort
            });

        }
    },

    mounted: function () {
    // var term = new Terminal();;
    // term.open(document.getElementById('terminal-container'));
    // term.write('Hello from \x1B[1;3;31mxterm.js\x1B[0m $ ') 
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

.terminalTabs{

}
.addButton{
    background-color:rgb(9, 22, 29);
    height: 35px;
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