<html>
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script src="https://code.highcharts.com/highcharts.js"></script>

  <style>
    table, th, td {
      border: 1px solid black;
      border-collapse: collapse;
    }
  </style>

</head>
<body>

    <div class="container">
      <ul class="nav nav-tabs">
        <li class="active"> <a data-toggle="tab" href="#tab_main">Main</a></li>
        <li><a a data-toggle="tab" href="#tab_chart">Chart</a></li>
      </ul>
      <div class="tab-content">
        <div id="tab_main" class="tab-pane fade in active">
          <g:form name="urlInputForm" url="[controller:'page', action:'parse']">
            <h3>Page to parse: </h3>
            <g:textField name="pageUrl"/>
            <g:actionSubmit value="Go" action="parse"/>
          </g:form>

          <table style="width:100%">
            <g:each var="mapElement" in="${outputMap}">
              <g:if test="${minValue == mapElement.value}">
                <g:set var="textColor" value="red" />
              </g:if>
              <g:elseif test="${maxValue == mapElement.value}">
                <g:set var="textColor" value="blue" />
              </g:elseif>
              <g:else>
                <g:set var="textColor" value="" />
              </g:else>
                  <td>
                    <span style="background-color: ${textColor}">${mapElement.key} </span>
                  </td>
                  <td>
                    <span style="background-color: ${textColor}">${mapElement.value} </span>
                  </td>
                </tr>
            </g:each>
          </table>
         </div>
         <div id="tab_chart" class="tab-pane fade">
          <h3> Chart here </h3>
          <div id="chart_container" style="width:100%; height:400px;"></div>
         </div>
       </div>
    </div>

  <script>
    $(document).ready(function(){
      $(".nav-tabs a").click(function(){
        $(this).tab('show');
      });
      $('.nav-tabs a').on('shown.bs.tab', function(event){
        var x = $(event.target).text();
        var y = $(event.relatedTarget).text();



        var myChart = $('#chart_container').highcharts()
        while(myChart.series.length > 0){
          chart.series[0].remove(true);
        }
        var pageURL = $(location).attr("protocol") + "//" + $(location).attr("hostname") +
          ":" + $(location).attr("port");
        $.getJSON(pageURL + '/page/urls', function(arr){
          myChart.addSeries({name:'Thread Count', data: arr.arrays.arrThreadCounts}, false);
          myChart.addSeries({name:'Total characters', data: arr.arrays.arrCharCounts}, false);
          myChart.addSeries({name:'Processing time', data: arr.arrays.arrProcTimes}, false);
          myChart.xAxis[0].update({categories: arr.arrays.arrUrls})

        });
        myChart.redraw()
      });
    });
  </script>

  <script>
    document.addEventListener('DOMContentLoaded', function () {
        var myChart = Highcharts.chart('chart_container', {
            chart: {
                type: 'column'
            },
            yAxis: {
                    type: 'logarithmic',
                    minorTickInterval: 0.1,
                },
            title: {
                text: 'Page processing'
            },
        });
    });
  </script>
</body>
</html>
