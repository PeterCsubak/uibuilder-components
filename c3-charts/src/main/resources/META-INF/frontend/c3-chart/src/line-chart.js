import '../libs/d3.min';
import * as c3 from '../libs/c3';
import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import {ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin';


export class LineChart extends ThemableMixin(PolymerElement) {

    static get template() {
        return html`
            <style>
                :host {
                    display: block;
                    width: 100%;
                    height: 100%;
                }

                #chartContainer {
                    display: block;
                    width: 100%;
                    height: 100%;
                }

                /*-- Chart --*/
                .c3 svg {
                    font: 10px sans-serif;
                    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
                }

                .c3 path, .c3 line {
                    fill: none;
                    stroke: #000;
                }

                .c3 text {
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    user-select: none;
                }

                .c3-legend-item-tile,
                .c3-xgrid-focus,
                .c3-ygrid,
                .c3-event-rect,
                .c3-bars path {
                    shape-rendering: crispEdges;
                }

                .c3-chart-arc path {
                    stroke: #fff;
                }

                .c3-chart-arc rect {
                    stroke: white;
                    stroke-width: 1;
                }

                .c3-chart-arc text {
                    fill: #fff;
                    font-size: 13px;
                }

                /*-- Axis --*/
                /*-- Grid --*/
                .c3-grid line {
                    stroke: #aaa;
                }

                .c3-grid text {
                    fill: #aaa;
                }

                .c3-xgrid, .c3-ygrid {
                    /*noinspection CssInvalidPropertyValue*/
                    stroke-dasharray: 3 3;
                }

                /*-- Text on Chart --*/
                .c3-text.c3-empty {
                    fill: #808080;
                    font-size: 2em;
                }

                /*-- Line --*/
                .c3-line {
                    stroke-width: 1px;
                }

                /*-- Point --*/
                .c3-circle {
                    fill: currentColor;
                }

                .c3-circle._expanded_ {
                    stroke-width: 1px;
                    stroke: white;
                }

                .c3-selected-circle {
                    fill: white;
                    stroke-width: 2px;
                }

                /*-- Bar --*/
                .c3-bar {
                    stroke-width: 0;
                }

                .c3-bar._expanded_ {
                    fill-opacity: 1;
                    fill-opacity: 0.75;
                }

                /*-- Focus --*/
                .c3-target.c3-focused {
                    opacity: 1;
                }

                .c3-target.c3-focused path.c3-line, .c3-target.c3-focused path.c3-step {
                    stroke-width: 2px;
                }

                .c3-target.c3-defocused {
                    opacity: 0.3 !important;
                }

                /*-- Region --*/
                .c3-region {
                    fill: steelblue;
                    fill-opacity: 0.1;
                }

                .c3-region text {
                    fill-opacity: 1;
                }

                /*-- Brush --*/
                .c3-brush .extent {
                    fill-opacity: 0.1;
                }

                /*-- Select - Drag --*/
                /*-- Legend --*/
                .c3-legend-item {
                    font-size: 12px;
                }

                .c3-legend-item-hidden {
                    opacity: 0.15;
                }

                .c3-legend-background {
                    opacity: 0.75;
                    fill: white;
                    stroke: lightgray;
                    stroke-width: 1;
                }

                /*-- Title --*/
                .c3-title {
                    font: 14px sans-serif;
                }

                /*-- Tooltip --*/
                .c3-tooltip-container {
                    z-index: 10;
                }

                .c3-tooltip {
                    border-collapse: collapse;
                    border-spacing: 0;
                    background-color: #fff;
                    empty-cells: show;
                    -webkit-box-shadow: 7px 7px 12px -9px #777777;
                    -moz-box-shadow: 7px 7px 12px -9px #777777;
                    box-shadow: 7px 7px 12px -9px #777777;
                    opacity: 0.9;
                }

                .c3-tooltip tr {
                    border: 1px solid #CCC;
                }

                .c3-tooltip th {
                    background-color: #aaa;
                    font-size: 14px;
                    padding: 2px 5px;
                    text-align: left;
                    color: #FFF;
                }

                .c3-tooltip td {
                    font-size: 13px;
                    padding: 3px 6px;
                    background-color: #fff;
                    border-left: 1px dotted #999;
                }

                .c3-tooltip td > span {
                    display: inline-block;
                    width: 10px;
                    height: 10px;
                    margin-right: 6px;
                }

                .c3-tooltip .value {
                    text-align: right;
                }

                /*-- Area --*/
                .c3-area {
                    stroke-width: 0;
                    opacity: 0.2;
                }

                /*-- Arc --*/
                .c3-chart-arcs-title {
                    dominant-baseline: middle;
                    font-size: 1.3em;
                }

                .c3-chart-arcs .c3-chart-arcs-background {
                    fill: #e0e0e0;
                    stroke: #FFF;
                }

                .c3-chart-arcs .c3-chart-arcs-gauge-unit {
                    fill: #000;
                    font-size: 16px;
                }

                .c3-chart-arcs .c3-chart-arcs-gauge-max {
                    fill: #777;
                }

                .c3-chart-arcs .c3-chart-arcs-gauge-min {
                    fill: #777;
                }

                .c3-chart-arc .c3-gauge-value {
                    fill: #000;
                    /*  font-size: 28px !important;*/
                }

                .c3-chart-arc.c3-target g path {
                    opacity: 1;
                }

                .c3-chart-arc.c3-target.c3-focused g path {
                    opacity: 1;
                }

                /*-- Zoom --*/
                .c3-drag-zoom.enabled {
                    pointer-events: all !important;
                    visibility: visible;
                }

                .c3-drag-zoom.disabled {
                    pointer-events: none !important;
                    visibility: hidden;
                }

                .c3-drag-zoom .extent {
                    fill-opacity: 0.1;
                }
            </style>
            <div id="chartContainer"></div>
        `;
    }

    static get is() {
        return 'line-chart';
    }

    static get properties() {
        return {
            data: {
                type: Object,
                value: null,
                notify: true,
                observer: "_onDataChanged"
            },
            axis: {
                type: Object,
                value: {}
            },
            xGridLines: {
                type: Array,
                value: []
            },
            hidePoints: {
                type: Boolean,
                value: false,
                notify: true,
                observer: "_onVisualsChanged"
            },
            pointsRadius: {
                type: Number,
                value: 2.5,
                notify: true,
                observer: "_onVisualsChanged"
            },
            pointsExpandRadius: {
                type: Number,
                value: 1.75,
                notify: true,
                observer: "_onVisualsChanged"
            },
            enableZoom: {
                type: Boolean,
                value: false,
                notify: true,
                observer: "_enableZoomChanged"
            },
            enableSubchart: {
                type: Boolean,
                value: false,
                notify: true,
                observer: "_enableSubchartChanged"
            },
            connectNull: {
                type: Boolean,
                value: false,
                notify: true,
                observer: "_onVisualsChanged"
            },

            __boundChart: {
                type: Object
            },

            __configs: {
                type: Object
            }
        };
    }

    ready() {
        super.ready();
        this.__build_chart();
    }

    __build_chart() {
        if (this.data) {
            if (this.__boundChart) {
                this.__boundChart = this.__boundChart.destroy();
            }

            this.__configs = {
                bindto: this.$.chartContainer,
                data: this.data,
                axis: this.axis,
                grid: {
                    x: {
                        lines: this.xGridLines
                    }
                },
                line: {
                    connectNull: this.connectNull
                },
                point: {
                    show: () => !this.hidePoints,
                    r: () => this.pointsRadius,
                    focus: {
                        expand: {
                            enabled: true,
                            r: () => this.pointsExpandRadius
                        }
                    }
                },
                zoom: {
                    enabled: this.enableZoom
                },
                subchart: {
                    show: this.enableSubchart
                }
            };

            this.__boundChart = c3.generate(this.__configs);
        }
    }

    __destroy_and_rebuild(args) {
        if (this.__boundChart)
            this.__boundChart = this.__boundChart.destroy();
        this.data = args.data;
        this.axis = args.axis;
        this.xGridLines = args.xGridLines;
        this.__build_chart();
    }

    _onDataChanged(newValue) {
        if (this.__boundChart) {
            let chart = this.__boundChart;
            chart.unload({
                done: function () {
                    chart.load(newValue)
                }
            });
        } else {
            this.__build_chart();
        }
    }

    flow(flowData) {
        if (this.__boundChart)
            this.__boundChart.flow(flowData);
    }

    removeAllXGrids() {
        if (this.__boundChart)
            this.__boundChart.xgrids.remove();
    }

    removeXGrid(values) {
        if (this.__boundChart)
            this.__boundChart.xgrids.remove(values);
    }

    addXGrids(values) {
        if (this.__boundChart)
            this.__boundChart.xgrids.add(values);
    }

    changeXGrids(values) {
        if (this.__boundChart)
            this.__boundChart.xgrids(values);
    }

    changeDataNames(dataNames) {
        if (this.__boundChart)
            this.__boundChart.data.names(dataNames);
    }

    changeDataColors(dataColors) {
        if (this.__boundChart)
            this.__boundChart.data.colors(dataColors);
    }

    _onVisualsChanged() {
        if (this.__boundChart)
            this.__boundChart.flush();
    }

    _enableZoomChanged(enabled) {
        if (this.__boundChart)
            this.__boundChart.zoom.enable(enabled);
    }

    _enableSubchartChanged(enabled) {
        if (this.__boundChart && this.__boundChart.subchart)
            if (enabled) {
                this.__boundChart.subchart.show();
            } else {
                this.__boundChart.subchart.hide();
            }
    }

    hideData(dataIds, withLegend = false) {
        if (this.__boundChart)
            this.__boundChart.hide(dataIds, {withLegend: withLegend});
    }

    showData(dataIds, withLegend = false) {
        if (this.__boundChart)
            this.__boundChart.show(dataIds, {withLegend: withLegend});
    }

}

customElements.define(LineChart.is, LineChart);