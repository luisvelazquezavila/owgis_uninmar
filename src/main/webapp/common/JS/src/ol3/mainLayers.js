goog.provide('owgis.layers');

var layer = undefined;// It will have the Ol3 object storing the main layer

/**
 * Defines which one is the main layer
 */
owgis.layers.initMainLayer = function(lay){
    layer = lay;
}

/**
 * Returns the current main layer
 */
owgis.layers.getMainLayer = function(){
    return layer;
}

/**
 * Makes a synchronous request and saves the dates into the "allFrames" array.
 */

owgis.layers.getTimesForDay = function(layer, time, allFrames){
    console.log("OWGIS TIME - getTimesForDay local:", time);

    var parts = time.split("-");

    if (parts.length !== 3) {
        console.warn("OWGIS TIME - Fecha inválida:", time);
        return;
    }

    var year = parseInt(parts[0], 10);
    var month = parseInt(parts[1], 10) - 1;
    var day = parseInt(parts[2], 10);

    var datesWithData = layerDetails.datesWithData;

    if (typeof datesWithData === "undefined") {
        console.warn("OWGIS TIME - datesWithData no existe");
        return;
    }

    if (typeof datesWithData[year] === "undefined") {
        console.warn("OWGIS TIME - Año sin datos:", year);
        return;
    }

    if (typeof datesWithData[year][month] === "undefined") {
        console.warn("OWGIS TIME - Mes sin datos:", month);
        return;
    }

    if (!_.contains(datesWithData[year][month], day)) {
        console.warn("OWGIS TIME - Día sin datos:", time);
        return;
    }

    /*
     * Por ahora usamos la hora conocida de los datos ERDDAP.
     * Posteriormente podemos hacer que Java también entregue
     * las horas exactas.
     */
    allFrames.push(time + "T17:47:00Z");

    console.log(
        "OWGIS TIME - frame agregado:",
        time + "T17:47:00Z"
    );
}

/**
 * Returns the current CQL_FILTER or undefinded
 */
owgis.layers.getCQLFilter= function(){
    return owgis.layers.getMainLayer().getSource().getParams().CQL_FILTER;
}

/**
 * This function obtains the server path of the main layer. 
 * @returns {unresolved}
 */
owgis.layers.getMainLayerServer = function(){
	var mainLayer = owgis.layers.getMainLayer();
	var mainSource = mainLayer.getSource();

	var mainLayerServer = mainSource.getUrls()[0];
	return mainLayerServer;
}

/** Hides or shows one layer of openLayers */
owgis.layers.showLayer = function(layer,show){
    layer.setVisible(show);
}


/**
 * This function replaces one parameter of the main layer and refresh the map
 */
owgis.layers.updateMainLayerParam= function(param,value){
	//Obtain the current parameters of the main layer
    layerParams= owgis.layers.getMainLayer().getSource().getParams();

    eval("layerParams."+param+"=\""+value+"\"");//Modify the desired parameter
	
    owgis.layers.getMainLayer().getSource().updateParams(layerParams);//Updates the layer
}