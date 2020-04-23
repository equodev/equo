 new Vue({
            el: "#app",
            vuetify: new Vuetify(),
            data: {
              title: "Equo plain App",
              toolitems: [
              { icon: 'fas fa-search fa-2x', tooltip: 'Search', eventHandler: "SearchHandler"},
              { icon: 'fas fa-copy fa-2x', tooltip: 'Copy', eventHandler: "CopyHandler" },
              { icon: 'fas fa-save fa-2x', tooltip: 'Save', eventHandler: "SaveHandler"},
              { icon: 'fas fa-play fa-2x', tooltip: 'Run',  eventHandler: "RunHandler" },
              { icon: 'fas fa-bug fa-2x', tooltip: 'Debug', eventHandler: "DebugHandler" }
              ]
            },
              methods: {
                        clickEvent: function (button) {
                          console.log(button);
                          equo.send(button);
                        }
              }
        })