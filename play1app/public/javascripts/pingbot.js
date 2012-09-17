// var timezone_offset = (new Date()).getTimezoneOffset() * 60000;

var graph_options = {
    xaxis: {
        mode: "time",
        // unfortunately timezone does not work (no mater if timezone-js is included or not)
        tickFormatter: function (val, axis) {
            var d = new Date(val);
            return d.getHours() + ':' + ((d.getMinutes() < 10) ? '0'+ d.getMinutes() : d.getMinutes());
        }
    },
    series: {
        points: { show: true, radius: 1 },
        bars:   { show: true }
    },
    grid: {
        hoverable: true
    },
    labels: ['Response Time (ms)', 'Failed'],  // TODO: depends on yaxis
    colors: ['green', 'red']
};

function getAndPlotData(canvasEl, resourceId, starttime, endtime, yaxis) {
    $.ajax({
        url: '/Application/ajaxPoints',
        data: {
            'resourceId': resourceId,
            'starttime':  starttime,
            'endtime':    endtime,
            'y':          yaxis
        },
        method: 'GET',
        dataType: 'json',
        success: function(series) {
            $.plot(canvasEl, [series.ok, series.failed], graph_options);
        }
    });
}


$(function () {
    var previousPoint = null;
    // ~~ Display current value on mouse over
    $(".canvas").bind("plothover", function(event, pos, item) {
        if (item) {
            if (previousPoint != item.dataIndex) {
                previousPoint = item.dataIndex;

                $("#tooltip").remove();
                var x = new Date(item.datapoint[0]),
                    y = item.datapoint[1].toFixed(0);

                showTooltip(item.pageX, item.pageY,
                    x + "<br/><b>" + y + "</b>");     // TODO: add specific unit?
            }
        }
        else {
            $("#tooltip").remove();
            previousPoint = null;
        }
    });
});

function showTooltip(x, y, contents) {
    $('<div id="tooltip">' + contents + '</div>').css( {
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        border: '1px solid #fdd',
        padding: '2px',
        color: '#333',
        'background-color': '#fee',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}