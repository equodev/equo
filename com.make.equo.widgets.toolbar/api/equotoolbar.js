window.equo = window.equo || {};

(function (equo) {

	equo.createToolbar = function(color, prueba){

		let toolbar = document.createElement('equotoolbarwc-toolbar');

		if (typeof color !== undefined){
			toolbar.setAttribute('color',color);
		} 

		let toolitem = document.createElement('equotoolbarwc-toolitem');


		toolitem.setAttribute('icon','search');

		toolitem.setAttribute("eventhandler",prueba.name);

		toolitem.eventhandler = prueba;

		toolbar.appendChild(toolitem);

		document.body.appendChild(toolbar);
	};

}(equo));