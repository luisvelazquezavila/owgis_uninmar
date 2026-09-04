```jsp
<div class="transDraggableWindow menuHidden toolTip container-fluid"
     id="CalendarsAndStopContainer"
     title="<fmt:message key='help.tooltip.calender'/>">

    <div class="row">
        <div class="col-xs-6 text-center title" id="cal-start-title">
            <span class="invShadow">
                <fmt:message key="ncwms.cal.start" />
            </span><br>
        </div>

        <div class="col-xs-4 text-center title" id="cal-end-title">
            <span class="invShadow">
                <fmt:message key="ncwms.cal.end" />
            </span><br>
        </div>

        <div class="col-xs-2 text-right">
            <a class="btn btn-default btn-xs"
               href="#"
               onclick="owgis.layouts.draggable.minimizeWindow('calendarsMinimize', 'CalendarsAndStopContainer')">
                <span class="glyphicon glyphicon-resize-small"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-6" id="cal-start"></div>
        <div class="col-xs-6" id="cal-end"></div>
    </div>

    <div class="row">
        <div class="col-xs-6" style="text-align: center">
            <select class="form-control input-sm"
                    id="startTimeCalendar"
                    name="startTimeCalendar"
                    onchange="owgis.ncwms.calendars.updateStartHour()">
            </select>
        </div>

        <div class="col-xs-6" style="text-align: center">
            <select class="form-control input-sm"
                    id="endTimeCalendar"
                    name="endTimeCalendar"
                    onchange="owgis.ncwms.calendars.updateEndHour()">
            </select>
        </div>
    </div>

    <!-- Selector jerárquico de fechas ERDDAP -->
    <div class="row" id="erddap-date-tree-container" style="margin-top: 10px; overflow-y: auto; flex: 1;">
        <div class="col-xs-12">
            <div class="title text-center">
                <span class="invShadow">Fechas disponibles</span>
            </div>

            <div id="erddap-date-tree">
                <!-- El árbol Año ? Mes ? Día se generará aquí -->
            </div>
        </div>
    </div>

</div>
```
