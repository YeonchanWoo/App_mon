;(function($,sr){
    // debouncing function from John Hann
    // http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
    var debounce = function (func, threshold, execAsap) {
        var timeout;

        return function debounced () {
            var obj = this, args = arguments;
            function delayed () {
                if (!execAsap)
                    func.apply(obj, args);
                timeout = null;
            }

            if (timeout)
                clearTimeout(timeout);
            else if (execAsap)
                func.apply(obj, args);

            timeout = setTimeout(delayed, threshold || 100);
        };
    };

    // smartresize
    jQuery.fn[sr] = function(fn){  return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr); };

})(jQuery,'smartresize')

;(function($,win){
    /**
     * <pre>
     * 대시보드
     * </pre>
     */
    var CURRENT_URL = window.location.href.split('#')[0].split('?')[0],
        $BODY = $('body'),
        $MENU_TOGGLE = $('#menu_toggle'),
        $SIDEBAR_MENU = $('#sidebar-menu'),
        $SIDEBAR_FOOTER = $('.sidebar-footer'),
        $LEFT_COL = $('.left_col'),
        $RIGHT_COL = $('.right_col'),
        $NAV_MENU = $('.nav_menu'),
        $FOOTER = $('footer');

    var dashboard = function(){
        var cls = this;

        {
            // init
            cls.init = function(){
                cls.sidebar();
                cls.DateTimePicker.init();
                if('/dashboard/realtime' == window.location.pathname){
                    cls.GrapeRealtime.init();
                    cls.TableRealtime.init();
                }else if('/dashboard/statistics' == window.location.pathname){
                    cls.GaugeTotalCount.init();
                    cls.GrapeIosAndroid.init();
                    cls.PieChartOS.init();
                    cls.PieChartVersion.init();
                    cls.PieChartDevice.init();
                }
            }

            // ajax
            cls.ajaxCall = function(type, url, JsonParam, sCallback, eCallback, bCallback, cCallback){

                $.ajax({
                    type : type,
                    url : url,
                    data : JsonParam,
                    cache : false,
                    async : false,
                    success: function(data) {
                        if($.isFunction(sCallback)){
                            sCallback(data);
                        }

                    },
                    beforeSend: function(){
                        if($.isFunction(bCallback)){
                            bCallback();
                        }
                    },
                    complete: function(){
                        if($.isFunction(cCallback)){
                            cCallback();
                        }
                    },
                    error: function(data){
                        console.log(data);
                        if($.isFunction(eCallback)){
                            eCallback();
                        }
                    }
                });
            }

            // sidebar
            cls.sidebar = function() {

                var setContentHeight = function () {
                    // reset height
                    $RIGHT_COL.css('min-height', $(window).height());

                    var bodyHeight = $BODY.outerHeight(),
                        footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
                        leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
                        contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

                    // normalize content
                    contentHeight -= $NAV_MENU.height() + footerHeight;

                    $RIGHT_COL.css('min-height', contentHeight);
                };

                $SIDEBAR_MENU.find('a').on('click', function(ev) {
                    console.log('clicked - sidebar_menu');
                    var $li = $(this).parent();

                    if ($li.is('.active')) {
                        $li.removeClass('active active-sm');
                        $('ul:first', $li).slideUp(function() {
                            setContentHeight();
                        });
                    } else {
                        // prevent closing menu if we are on child menu
                        if (!$li.parent().is('.child_menu')) {
                            $SIDEBAR_MENU.find('li').removeClass('active active-sm');
                            $SIDEBAR_MENU.find('li ul').slideUp();
                        }else
                        {
                            if ( $BODY.is( ".nav-sm" ) )
                            {
                                $SIDEBAR_MENU.find( "li" ).removeClass( "active active-sm" );
                                $SIDEBAR_MENU.find( "li ul" ).slideUp();
                            }
                        }
                        $li.addClass('active');

                        $('ul:first', $li).slideDown(function() {
                            setContentHeight();
                        });
                    }
                });

                // toggle small or large menu
                $MENU_TOGGLE.on('click', function() {
                    console.log('clicked - menu toggle');

                    if ($BODY.hasClass('nav-md')) {
                        $SIDEBAR_MENU.find('li.active ul').hide();
                        $SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
                    } else {
                        $SIDEBAR_MENU.find('li.active-sm ul').show();
                        $SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
                    }

                    $BODY.toggleClass('nav-md nav-sm');

                    setContentHeight();

                    $('.dataTable').each ( function () { $(this).dataTable().fnDraw(); });
                });

                // check active menu
                $SIDEBAR_MENU.find('a[href="' + CURRENT_URL + '"]').parent('li').addClass('current-page');

                $SIDEBAR_MENU.find('a').filter(function () {
                    return this.href == CURRENT_URL;
                }).parent('li').addClass('current-page').parents('ul').slideDown(function() {
                    setContentHeight();
                }).parent().addClass('active');

                // recompute content when resizing
                $(window).smartresize(function(){
                    setContentHeight();
                });

                setContentHeight();

                // fixed sidebar
                if ($.fn.mCustomScrollbar) {
                    $('.menu_fixed').mCustomScrollbar({
                        autoHideScrollbar: true,
                        theme: 'minimal',
                        mouseWheel:{ preventDefault: true }
                    });
                }
            };

            // chart option
            cls.chartOption = {
                color: ["#26B99A", "#34495E", "#BDC3C7", "#3498DB", "#9B59B6", "#8abb6f", "#759c6a", "#bfd3b7"],
                title: {
                    itemGap: 8,
                    textStyle: {
                        fontWeight: "normal",
                        color: "#408829"
                    }
                },
                dataRange: {
                    color: ["#1f610a", "#97b58d"]
                },
                toolbox: {
                    color: ["#408829", "#408829", "#408829", "#408829"]
                },
                tooltip: {
                    backgroundColor: "rgba(0,0,0,0.5)",
                    axisPointer: {
                        type: "line",
                        lineStyle: {
                            color: "#408829",
                            type: "dashed"
                        },
                        crossStyle: {
                            color: "#408829"
                        },
                        shadowStyle: {
                            color: "rgba(200,200,200,0.3)"
                        }
                    }
                },
                dataZoom: {
                    dataBackgroundColor: "#eee",
                    fillerColor: "rgba(64,136,41,0.2)",
                    handleColor: "#408829"
                },
                grid: {
                    borderWidth: 0
                },
                categoryAxis: {
                    axisLine: {
                        lineStyle: {
                            color: "#408829"
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: ["#eee"]
                        }
                    }
                },
                valueAxis: {
                    axisLine: {
                        lineStyle: {
                            color: "#408829"
                        }
                    },
                    splitArea: {
                        show: !0,
                        areaStyle: {
                            color: ["rgba(250,250,250,0.1)", "rgba(200,200,200,0.1)"]
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: ["#eee"]
                        }
                    }
                },
                timeline: {
                    lineStyle: {
                        color: "#408829"
                    },
                    controlStyle: {
                        normal: {
                            color: "#408829"
                        },
                        emphasis: {
                            color: "#408829"
                        }
                    }
                },
                k: {
                    itemStyle: {
                        normal: {
                            color: "#68a54a",
                            color0: "#a9cba2",
                            lineStyle: {
                                width: 1,
                                color: "#408829",
                                color0: "#86b379"
                            }
                        }
                    }
                },
                map: {
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                color: "#ddd"
                            },
                            label: {
                                textStyle: {
                                    color: "#c12e34"
                                }
                            }
                        },
                        emphasis: {
                            areaStyle: {
                                color: "#99d2dd"
                            },
                            label: {
                                textStyle: {
                                    color: "#c12e34"
                                }
                            }
                        }
                    }
                },
                force: {
                    itemStyle: {
                        normal: {
                            linkStyle: {
                                strokeColor: "#408829"
                            }
                        }
                    }
                },
                chord: {
                    padding: 4,
                    itemStyle: {
                        normal: {
                            lineStyle: {
                                width: 1,
                                color: "rgba(128, 128, 128, 0.5)"
                            },
                            chordStyle: {
                                lineStyle: {
                                    width: 1,
                                    color: "rgba(128, 128, 128, 0.5)"
                                }
                            }
                        },
                        emphasis: {
                            lineStyle: {
                                width: 1,
                                color: "rgba(128, 128, 128, 0.5)"
                            },
                            chordStyle: {
                                lineStyle: {
                                    width: 1,
                                    color: "rgba(128, 128, 128, 0.5)"
                                }
                            }
                        }
                    }
                },
                gauge: {
                    startAngle: 225,
                    endAngle: -45,
                    axisLine: {
                        show: !0,
                        lineStyle: {
                            color: [
                                [.2, "#86b379"],
                                [.8, "#68a54a"],
                                [1, "#408829"]
                            ],
                            width: 8
                        }
                    },
                    axisTick: {
                        splitNumber: 10,
                        length: 12,
                        lineStyle: {
                            color: "auto"
                        }
                    },
                    axisLabel: {
                        textStyle: {
                            color: "auto"
                        }
                    },
                    splitLine: {
                        length: 18,
                        lineStyle: {
                            color: "auto"
                        }
                    },
                    pointer: {
                        length: "90%",
                        color: "auto"
                    },
                    title: {
                        textStyle: {
                            color: "#333"
                        }
                    },
                    detail: {
                        textStyle: {
                            color: "auto"
                        }
                    }
                },
                textStyle: {
                    fontFamily: "Arial, Verdana, sans-serif"
                }
            };

            // date format
            cls.gd = function(str){
                var year = str.substring(0, 4);
                var month = str.substring(5, 7);
                var day = str.substring(8, 10);
                var hour = parseInt(str.substring(11, 13)) + 9;
                if(hour > 24){
                    hour = hour - 24;
                    if(hour < 10){
                        hour = "0"+hour;
                    }
                }
                var minute = str.substring(14, 16);
                var second = str.substring(17, 19);
                // console.log("year : "  + year + "month : "  + month +"day : "  + day +"hour : "  + hour +"minute : "  + minute +"second : "  + second);
                return hour + ":"+ minute +":" + second;
            }

        }
        return cls;
    }

    /**
     * <pre>
     * 캘린더
     * </pre>
     */
    var DateTimePicker = function(){
        var cls = this;

        cls.init = function(){
            $('#date_timepicker_1').datetimepicker({
                format : 'YYYY-MM-DD HH:mm:ss'
            });
            $('#date_timepicker_2').datetimepicker({
                format : 'YYYY-MM-DD HH:mm:ss'
                ,useCurrent: false //Important! See issue #1075
            });
            $("#date_timepicker_1").on("dp.change", function (e) {
                $('#date_timepicker_2').data("DateTimePicker").minDate(e.date);
            });
            $("#date_timepicker_2").on("dp.change", function (e) {
                $('#date_timepicker_1').data("DateTimePicker").maxDate(e.date);
            });
            $( "#btn_grape_realtime" ).click(function() {
                var param = {};
                param.startDate = cls.formatDate( $('#date_timepicker_1').data("DateTimePicker").date() );
                param.endDate =  cls.formatDate( $('#date_timepicker_2').data("DateTimePicker").date() );
                GrapeRealtime().getData(param);
                TableRealtime().getData(param);
            });

            $( "#btn_grape_statistics" ).click(function() {
                var param = {};
                param.startDate = cls.formatDate( $('#date_timepicker_1').data("DateTimePicker").date() );
                param.endDate =  cls.formatDate( $('#date_timepicker_2').data("DateTimePicker").date() );
                GaugeTotalCount().getData(param);
                GrapeIosAndroid().getData(param);
                PieChartOS().getData(param);
                PieChartVersion().getData(param);
                PieChartDevice().getData(param);
            });

            $('#chk_refresh').change(function() {
                alert('시작');
                if($(this).prop('checked')){{
                }}
            })

        }

        cls.chkedDate = function(){
            var param = {};
            param.startDate = cls.formatDate( $('#datetimepicker6').data("DateTimePicker").date() );
            param.endDate =  cls.formatDate( $('#datetimepicker7').data("DateTimePicker").date() );
            GrapeRealtime().getData(param);
            TableRealtime().getData(param);
        }

        cls.formatDate = function(date){
            var d = new Date(date),
                year = d.getFullYear(),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                hour = '' + d.getHours(),
                minute = '' + d.getMinutes(),
                second = '' + d.getSeconds();
            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;
            if (hour.length < 2) hour = '0' + hour;
            if (minute.length < 2) minute = '0' + minute;
            if (second.length < 2) second = '0' + second;
            return [year, month, day, hour, minute, second].join('');
        }

        return cls;
    }

    /**
     * <pre>
     * 실시간 그래프
     * </pre>
     */
    var GrapeRealtime = function(){
        var cls = this;
        cls.APIUrl = "/component/grape/realtime";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(xAxis_data, series_data){
            var chart = echarts.init(document.getElementById("grape_realtime"), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    trigger: "axis"
                },
                legend: {
                    x: 220,
                    y: 40,
                    data: ["App Crash"]
                },
                toolbox: {
                    show: !0,
                    feature: {
                        magicType: {
                            show: !0,
                            title: {
                                line: "Line",
                                bar: "Bar",
                            },
                            type: ["line", "bar"]
                        },
                        restore: {
                            show: !0,
                            title: "Restore"
                        },
                        saveAsImage: {
                            show: !0,
                            title: "Save Image"
                        }
                    }
                },
                calculable: !0,
                xAxis: [{
                    type: "category",
                    boundaryGap: !1,
                    data : xAxis_data
                }],
                yAxis: [{
                    type: "value"
                }],
                series: [{
                    name: "App Crash",
                    type: "line",
                    smooth: !0,
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                type: "default"
                            }
                        }
                    },
                    data : series_data
                }]
            })
        }

        cls.getData = function(param){
            var sCallback = function(data){
                var xAxis_data = [];
                var series_data = [];
                for ( var i = 0, len = data.length; i < len; ++i) {
                    var buket = data[i];
                    xAxis_data.push(dashboard().gd(buket.keyAsString));
                    series_data.push(buket.docCount);
                }
                cls.getGrape(xAxis_data, series_data);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    /**
     * <pre>
     * 실시간 에러현황
     * </pre>
     */
    var TableRealtime = function(){
        var cls = this;
        cls.APIUrl = "/component/table/realtime";

        cls.init = function(){
            cls.getData();
        }

        cls.getData = function(param){
            var sCallback = function(data){
                $('#table_realtime').html(data);
                cls.init_DataTables();
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        cls.init_DataTables = function() {
            if( typeof ($.fn.DataTable) === 'undefined'){ return; }
            var handleDataTableButtons = function() {
                if ($("#datatable-buttons").length) {
                    $("#datatable-buttons").DataTable({
                        dom: "Bfrtip",
                        buttons: [
                            {
                                extend: "copy",
                                className: "btn-sm"
                            },
                            {
                                extend: "csv",
                                className: "btn-sm"
                            },
                            {
                                extend: "excel",
                                className: "btn-sm"
                            },
                            {
                                extend: "pdfHtml5",
                                className: "btn-sm"
                            },
                            {
                                extend: "print",
                                className: "btn-sm"
                            },
                        ],
                        responsive: true
                    });
                }
            };

            TableManageButtons = function() {
                "use strict";
                return {
                    init: function() {
                        handleDataTableButtons();
                    }
                };
            }();

            $('#datatable').dataTable();

            $('#datatable-keytable').DataTable({
                keys: true
            });

            $('#datatable-responsive').DataTable();

            $('#datatable-scroller').DataTable({
                ajax: "js/datatables/json/scroller-demo.json",
                deferRender: true,
                scrollY: 380,
                scrollCollapse: true,
                scroller: true
            });

            $('#datatable-fixed-header').DataTable({
                fixedHeader: true
            });

            var $datatable = $('#datatable-checkbox');

            $datatable.dataTable({
                'order': [[ 1, 'asc' ]],
                'columnDefs': [
                    { orderable: false, targets: [0] }
                ]
            });
            $datatable.on('draw.dt', function() {
                $('checkbox input').iCheck({
                    checkboxClass: 'icheckbox_flat-green'
                });
            });

            TableManageButtons.init();

        };
        return cls;
    }

    /**
     * <pre>
     * 전체 발생 건수
     * </pre>
     */
    var GaugeTotalCount = function(){
        var cls = this;
        cls.APIUrl = "/component/gauge/count";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(result_data){
            var chart = echarts.init(document.getElementById('gauge_total_count'), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    formatter: "{a} <br/>{b} : {c}%"
                },
                toolbox: {
                    show: true,
                    feature: {
                        restore: {
                            show: true,
                            title: "Restore"
                        },
                        saveAsImage: {
                            show: true,
                            title: "Save Image"
                        }
                    }
                },
                series: [{
                    name: 'Performance',
                    type: 'gauge',
                    center: ['50%', '50%'],
                    startAngle: 140,
                    endAngle: -140,
                    min: 0,
                    max: 100,
                    precision: 0,
                    splitNumber: 10,
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: [
                                [0.2, 'lightgreen'],
                                [0.4, 'orange'],
                                [0.8, 'skyblue'],
                                [1, '#ff4500']
                            ],
                            width: 30
                        }
                    },
                    axisTick: {
                        show: true,
                        splitNumber: 5,
                        length: 8,
                        lineStyle: {
                            color: '#eee',
                            width: 1,
                            type: 'solid'
                        }
                    },
                    axisLabel: {
                        show: true,
                        formatter: function(v) {
                            switch (v + '') {
                                case '10':
                                    return '';
                                case '500':
                                    return '';
                                case '1000':
                                    return '';
                                case '5000':
                                    return '5000건 이상';
                                default:
                                    return '';
                            }
                        },
                        textStyle: {
                            color: '#333'
                        }
                    },
                    splitLine: {
                        show: true,
                        length: 30,
                        lineStyle: {
                            color: '#eee',
                            width: 2,
                            type: 'solid'
                        }
                    },
                    pointer: {
                        length: '80%',
                        width: 8,
                        color: 'auto'
                    },
                    title: {
                        show: true,
                        offsetCenter: ['-65%', -10],
                        textStyle: {
                            color: '#333',
                            fontSize: 15
                        }
                    },
                    detail: {
                        show: true,
                        backgroundColor: 'rgba(0,0,0,0)',
                        borderWidth: 0,
                        borderColor: '#ccc',
                        width: 100,
                        height: 40,
                        offsetCenter: ['-60%', 10],
                        formatter: '{value}건',
                        textStyle: {
                            color: 'auto',
                            fontSize: 30
                        }
                    },
                    data: [{
                        value: result_data,
                        name: '발생건수'
                    }]
                }]
            });
        }

        cls.getData = function(param){
            var sCallback = function(data){
                cls.getGrape(data.docCount);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    /**
     * <pre>
     * IOS / Android별 현황
     * </pre>
     */
    var GrapeIosAndroid = function(){
        var cls = this;
        cls.APIUrl = "/component/grape/IosAndroid";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(xAxis_data, series_data_ios, series_data_android){
            var chart = echarts.init(document.getElementById('grape_ios_android'), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['iOS', 'Android']
                },
                toolbox: {
                    show: false
                },
                calculable: false,
                xAxis: [{
                    type: 'category',
                    data: xAxis_data
                }],
                yAxis: [{
                    type: 'value'
                }],
                series: [{
                    name: 'iOS',
                    type: 'bar',
                    data: series_data_ios,
                }, {
                    name: 'Android',
                    type: 'bar',
                    data: series_data_android,
                }]
            });
        }

        cls.getData = function(param){
            var sCallback = function(data){
                var xAxis_data = [];
                var series_data_ios = [];
                var series_data_android = [];
                for (var date in data) {
                    xAxis_data.push(dashboard().gd(date));
                    for ( var i = 0, len = data[date].length; i < len; ++i) {
                        var key = data[date][i].key;
                        var docCount = data[date][i].docCount;
                        if('Android' == key){
                            series_data_android.push(docCount);
                        }else{
                            series_data_ios.push(docCount);
                        }
                    }
                }
                cls.getGrape(xAxis_data, series_data_ios, series_data_android);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    /**
     * <pre>
     * OS별 현황
     * </pre>
     */
    var PieChartOS = function(){
        var cls = this;
        cls.APIUrl = "/component/piechart/os";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(result_data){
            var chart = echarts.init(document.getElementById('piechart_os'), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: ['iOS', 'Android']
                },
                toolbox: {
                    show: true,
                    feature: {
                        magicType: {
                            show: true,
                            type: ['pie', 'funnel'],
                            option: {
                                funnel: {
                                    x: '25%',
                                    width: '50%',
                                    funnelAlign: 'left',
                                    max: 1548
                                }
                            }
                        },
                        restore: {
                            show: true,
                            title: "Restore"
                        },
                        saveAsImage: {
                            show: true,
                            title: "Save Image"
                        }
                    }
                },
                calculable: true,
                series: [{
                    name: 'OS별',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '48%'],
                    data: [{
                        value: result_data[1],
                        name: 'iOS'
                    }, {
                        value: result_data[0],
                        name: 'Android'
                    }]
                }]
            });
        }

        cls.getData = function(param){
            var sCallback = function(data){
                var result_data = [];
                for ( var i = 0, len = data.length; i < len; ++i) {
                    result_data.push(data[i].docCount);
                }
                cls.getGrape(result_data);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    /**
     * <pre>
     * 버전별 현황
     * </pre>
     */
    var PieChartVersion = function(){
        var cls = this;
        cls.APIUrl = "/component/piechart/version";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(legend_data, result_data){
            var chart = echarts.init(document.getElementById('piechart_version'), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: legend_data
                },
                toolbox: {
                    show: true,
                    feature: {
                        magicType: {
                            show: true,
                            type: ['pie', 'funnel']
                        },
                        restore: {
                            show: true,
                            title: "Restore"
                        },
                        saveAsImage: {
                            show: true,
                            title: "Save Image"
                        }
                    }
                },
                calculable: true,
                series: [{
                    name: 'Area Mode',
                    type: 'pie',
                    radius: [25, 90],
                    center: ['50%', 170],
                    roseType: 'area',
                    x: '50%',
                    max: 40,
                    sort: 'ascending',
                    data : result_data
                }]
            });
        }

        cls.getData = function(param){
            var sCallback = function(data){
                var legend_data = [];
                var result_data = [];
                for ( var i = 0, len = data.length; i < len; ++i) {
                    var result = {};
                    result.name = data[i].key;
                    result.value = data[i].docCount;
                    result_data.push(result);
                    legend_data.push(data[i].key);
                }
                cls.getGrape(legend_data, result_data);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    /**
     * <pre>
     * 기기별 현황
     * </pre>
     */
    var PieChartDevice = function(){
        var cls = this;
        cls.APIUrl = "/component/piechart/device";

        cls.init = function(){
            cls.getData();
        }

        cls.getGrape = function(legend_data, result_data){
            var chart = echarts.init(document.getElementById('piechart_device'), dashboard().chartOption);
            chart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                calculable: true,
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: legend_data
                },
                toolbox: {
                    show: true,
                    feature: {
                        magicType: {
                            show: true,
                            type: ['pie', 'funnel'],
                            option: {
                                funnel: {
                                    x: '25%',
                                    width: '50%',
                                    funnelAlign: 'center',
                                    max: 1548
                                }
                            }
                        },
                        restore: {
                            show: true,
                            title: "Restore"
                        },
                        saveAsImage: {
                            show: true,
                            title: "Save Image"
                        }
                    }
                },
                series: [{
                    name: 'Access to the resource',
                    type: 'pie',
                    radius: ['35%', '55%'],
                    itemStyle: {
                        normal: {
                            label: {
                                show: true
                            },
                            labelLine: {
                                show: true
                            }
                        },
                        emphasis: {
                            label: {
                                show: true,
                                position: 'center',
                                textStyle: {
                                    fontSize: '14',
                                    fontWeight: 'normal'
                                }
                            }
                        }
                    },
                    data: result_data
                }]
            });
        }

        cls.getData = function(param){
            var sCallback = function(data){
                var legend_data = [];
                var result_data = [];
                for ( var i = 0, len = data.length; i < len; ++i) {
                    var result = {};
                    result.name = data[i].key;
                    result.value = data[i].docCount;
                    result_data.push(result);
                    legend_data.push(data[i].key);
                }
                cls.getGrape(legend_data, result_data);
            }
            dashboard().ajaxCall('get', cls.APIUrl, param, sCallback, null, null, null);
        }

        return cls;
    }

    dashboard.prototype.DateTimePicker = new DateTimePicker();
    dashboard.prototype.GrapeRealtime = new GrapeRealtime();
    dashboard.prototype.TableRealtime = new TableRealtime();

    dashboard.prototype.GaugeTotalCount = new GaugeTotalCount();
    dashboard.prototype.GrapeIosAndroid = new GrapeIosAndroid();
    dashboard.prototype.PieChartOS = new PieChartOS();
    dashboard.prototype.PieChartVersion = new PieChartVersion();
    dashboard.prototype.PieChartDevice = new PieChartDevice();

    window.dashboard = window.dashboard || new dashboard();
    window.dashboard.init();

})(jQuery, window);
