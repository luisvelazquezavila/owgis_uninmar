<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    /* CSS (El mismo que tenías) */
    #forceTimelineContainer {
        position: absolute; bottom: 130px !important; left: 20px; right: 20px; 
        z-index: 999999 !important; background: rgba(0,0,0,0.85); padding: 10px 20px; 
        border-radius: 8px; display: none; align-items: center; justify-content: space-between; 
        color: white; border: 1px solid #444; height: 60px; box-shadow: 0 4px 10px rgba(0,0,0,0.5);
    }
    #btnToggleTreeContainer {
        position: absolute; top: 100px; right: 20px; z-index: 999999 !important;
        background: #fff; padding: 8px 12px; border-radius: 4px; cursor: pointer;
        box-shadow: 0 2px 5px rgba(0,0,0,0.3); font-weight: bold; display: none;
    }
    #forceTimeTreeContainer {
        display: none; z-index: 999999 !important; position: absolute; top: 150px; right: 20px;
        background: #222; color: white; width: 280px; border-radius: 8px; padding: 10px;
        box-shadow: 0 4px 8px rgba(0,0,0,0.8); border: 1px solid #444; max-height: 400px; overflow-y: auto;
    }
    .tree-year { cursor: pointer; font-weight: bold; padding: 8px 0; display: block; border-bottom: 1px solid #444; }
    .tree-year:hover { color: #4da6ff; }
    .tree-month { cursor: pointer; padding-left: 15px; display: block; font-weight: bold; margin: 3px 0; }
    .tree-month:hover { color: #4da6ff; }
    .tree-day { cursor: pointer; padding-left: 30px; display: block; font-size: 0.9em; margin: 2px 0; }
    .tree-day:hover { color: #4da6ff; background: #333; }
</style>

<!-- BARRA DE TIEMPO -->
<div id="forceTimelineContainer">
    <button id="manualPlayBtn" class="btn btn-success btn-sm" style="margin-right:15px; min-width:60px;">▶ Play</button>
    <span id="manualTimeDisplay" style="font-weight:bold; min-width:200px; text-align:center;">Cargando fechas...</span>
    <input type="range" id="manualTimeSlider" min="0" max="100" value="0" style="width:70%; margin:0 15px; cursor:pointer;" />
    <button id="manualStopBtn" class="btn btn-danger btn-sm" style="margin-left:15px; min-width:60px;">■ Stop</button>
</div>

<!-- BOTÓN PARA ABRIR EL ÁRBOL -->
<div id="btnToggleTreeContainer" onclick="toggleTreeWindow()">📅 Ver fechas</div>

<!-- ÁRBOL JERÁRQUICO -->
<div id="forceTimeTreeContainer">
    <div style="display:flex; justify-content:space-between; border-bottom: 1px solid #444; padding-bottom: 5px; margin-bottom: 10px;">
        <span>Selecciona fecha (Año > Mes > Día)</span>
        <span onclick="document.getElementById('forceTimeTreeContainer').style.display='none'" style="cursor:pointer; padding:0 5px;">✕</span>
    </div>
    <div id="manualTreeContainer"></div>
</div>

<!-- JAVASCRIPT DINÁMICO CON RESPALDO -->
<script>
    jQuery(document).ready(function() {
        jQuery('#forceTimelineContainer').css('display', 'flex');
        setTimeout(function() {
            var datesArray = [];

            // 1. Intentamos usar las fechas que trajo el Java (serverDates)
            if (typeof serverDates !== 'undefined' && serverDates.length > 0) {
                console.log("OWGIS: Usando fechas dinámicas del servidor: " + serverDates.length);
                datesArray = serverDates.map(function(dateStr) {
                    return new Date(dateStr);
                });
            } else {
                // 2. Si Java falló, generamos las fechas manualmente (Plan B)
                console.warn("OWGIS: Java no trajo fechas. Generando 700 fechas de respaldo.");
                var totalBackup = 700;
                var startDate = new Date(2015, 8, 14); 
                for (var i = 0; i < totalBackup; i++) {
                    var d = new Date(startDate);
                    d.setDate(d.getDate() + (i * 5)); 
                    datesArray.push(d);
                }
            }

            if (datesArray.length === 0) {
                console.error("OWGIS: No se pudieron obtener fechas.");
                return;
            }

            console.log("OWGIS: Cargando " + datesArray.length + " fechas.");
            jQuery('#forceTimelineContainer').css('display', 'flex');
            jQuery('#btnToggleTreeContainer').css('display', 'block');

            var totalDates = datesArray.length;
            var slider = document.getElementById('manualTimeSlider');
            var playBtn = document.getElementById('manualPlayBtn');
            var stopBtn = document.getElementById('manualStopBtn');
            var display = document.getElementById('manualTimeDisplay');
            var treeContainer = document.getElementById('manualTreeContainer');
            
            var isPlaying = false;
            var playInterval = null;
            slider.max = totalDates - 1;

            function updateDate(index) {
                var currentDate = datesArray[index];
                var isoDate = currentDate.toISOString(); 
                var dateStr = isoDate.split('T')[0];
                display.innerHTML = dateStr;

                try {
                    if (typeof owgis !== 'undefined' && typeof owgis.layers !== 'undefined' && typeof owgis.layers.updateMainLayerParam === 'function') {
                        owgis.layers.updateMainLayerParam('TIME', isoDate);
                        console.log("✅ Fecha actualizada: " + isoDate);
                    }
                } catch(e) { }

                // 1. Limpiamos el color de fondo de TODOS los días (sin excepción)
                jQuery('.tree-day').css('background', 'transparent').css('color', 'white');
                
                // 2. Resaltamos el día que acabamos de seleccionar
                var dayEl = document.getElementById('day-' + index);
                if(dayEl) {
                    jQuery(dayEl).css('background', '#4da6ff').css('color', '#000');
                }
            }

            // Construir árbol jerárquico
            var grouped = {};
            datesArray.forEach(function(date, idx) {
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();
                if (!grouped[year]) grouped[year] = {};
                if (!grouped[year][month]) grouped[year][month] = [];
                grouped[year][month].push({ day: day, idx: idx });
            });

            var htmlTree = '';
            for (var y in grouped) {
                htmlTree += '<span class="tree-year" onclick="toggleTree(\'year-' + y + '\')">▶ ' + y + '</span>';
                htmlTree += '<div id="year-' + y + '" style="display:none; padding-left:10px;">';
                for (var m in grouped[y]) {
                    var monthName = new Date(y, m - 1, 1).toLocaleString('default', { month: 'long' });
                    htmlTree += '<span class="tree-month" onclick="toggleTree(\'month-' + y + '-' + m + '\')">▶ ' + monthName + '</span>';
                    htmlTree += '<div id="month-' + y + '-' + m + '" style="display:none; padding-left:15px;">';
                    grouped[y][m].forEach(function(d) {
                        htmlTree += '<span class="tree-day" id="day-' + d.idx + '" onclick="selectDate(' + d.idx + ')">• ' + d.day + '</span><br>';
                    });
                    htmlTree += '</div>';
                }
                htmlTree += '</div>';
            }
            treeContainer.innerHTML = htmlTree;

            // Eventos del slider y botones
            slider.addEventListener('input', function() {
                var val = parseInt(this.value);
                updateDate(val);
                if (isPlaying) {
                    clearInterval(playInterval);
                    isPlaying = false;
                    playBtn.innerHTML = '▶ Play';
                }
            });

            playBtn.addEventListener('click', function(event) {
                event.preventDefault();
                if (isPlaying) {
                    clearInterval(playInterval);
                    isPlaying = false;
                    playBtn.innerHTML = '▶ Play';
                } else {
                    isPlaying = true;
                    playBtn.innerHTML = '|| Pause';
                    playInterval = setInterval(function() {
                        try {
                            var currentVal = parseInt(slider.value);
                            var nextVal = (currentVal + 1) % totalDates;
                            slider.value = nextVal;
                            updateDate(nextVal);
                        } catch(err) { }
                    }, 500);
                }
            });

            stopBtn.addEventListener('click', function(event) {
                event.preventDefault();
                clearInterval(playInterval);
                isPlaying = false;
                playBtn.innerHTML = '▶ Play';
                slider.value = 0;
                updateDate(0);
            });

            updateDate(0);

            window.toggleTree = function(id) {
                var el = document.getElementById(id);
                var parent = el.parentElement;
                if (parent) {
                    var children = parent.children;
                    for (var i = 0; i < children.length; i++) {
                        var child = children[i];
                        if (child.id && child.id.startsWith(id.split('-')[0] + '-')) {
                            if (child.id !== id && child.style.display === 'block') {
                                child.style.display = 'none';
                                var siblingSpan = child.previousElementSibling;
                                if (siblingSpan) {
                                    siblingSpan.innerHTML = siblingSpan.innerHTML.replace('▼', '▶');
                                }
                            }
                        }
                    }
                }
                if (el.style.display === 'block') {
                    el.style.display = 'none';
                    el.previousElementSibling.innerHTML = el.previousElementSibling.innerHTML.replace('▼', '▶');
                } else {
                    el.style.display = 'block';
                    el.previousElementSibling.innerHTML = el.previousElementSibling.innerHTML.replace('▶', '▼');
                }
            };

            window.toggleTreeWindow = function() {
                var tree = document.getElementById('forceTimeTreeContainer');
                tree.style.display = (tree.style.display === 'none') ? 'block' : 'none';
            };
            
            window.selectDate = function(idx) {
                slider.value = idx;
                updateDate(idx);
                // Eliminamos la línea de cierre para que el menú se quede abierto
            };

        }, 1000); 
    });
</script>