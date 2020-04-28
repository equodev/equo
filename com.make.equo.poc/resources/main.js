 new Vue({
            el: "#app",
            vuetify: new Vuetify(),
            data: {
              title: "Equo plain App",
              toolitems: [
              { icon: 'fas fa-search', tooltip: 'Search', eventHandler: "SearchHandler"},
              { icon: 'fas fa-copy', tooltip: 'Copy', eventHandler: "CopyHandler" },
              { icon: 'fas fa-save', tooltip: 'Save', eventHandler: "SaveHandler"},
              { icon: 'fas fa-play', tooltip: 'Run',  eventHandler: "RunHandler" },
              { icon: 'fas fa-bug', tooltip: 'Debug', eventHandler: "DebugHandler" }
              ]
            },
              methods: {
                        clickEvent: function (button) {
                          console.log(button);
                          equo.send(button);
                        }
              }
        })