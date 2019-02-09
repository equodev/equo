<script>
            function start_receiving_messages(vm){
                equo.on(nameSpace+"_icons",values =>{
                    vm.$data.parts =values;
                    console.log('values= ', values)
                });
                            
            }
        </script>
        
        <script language="JavaScript">
            function getUrlParameter(sParam) {
                var sPageURL = decodeURIComponent(window.location.search.substring(1)),sURLVariables = sPageURL.split('&'),sParameterName,i;
                for (i = 0; i < sURLVariables.length; i++) {
                    sParameterName = sURLVariables[i].split('=');
            
                    if (sParameterName[0] === sParam) {
                        return sParameterName[1] === undefined ? true : sParameterName[1];
                    }
                }
            }
        </script>
        <script>
            var nameSpace= getUrlParameter('nameSpace').toString()
        </script>
        <script>
            new Vue({
                el: '#app',
                data: {
                    active: null,
                    parts: []
                },
                methods: {
                    send_onclick: function (accion){
                        equo.send(nameSpace+'_iconClicked', { accion});
                    },
                    
                },
                created: function() {
                
                    let vm = this;
                    start_receiving_messages(vm);
                }

            });

        </script>
