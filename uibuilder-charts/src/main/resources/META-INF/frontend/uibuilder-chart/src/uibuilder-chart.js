import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import {ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import {DataSource} from "@vaadin/flow-frontend/uibuilder/data/data-source.js";
import {ItemDataSource} from "@vaadin/flow-frontend/uibuilder/data/item-data-source.js";
import {Chart} from 'chart.js';
import 'chartjs-plugin-colorschemes';
import 'chartjs-plugin-zoom';
import {ChartLabels} from "./chart-labels";
import {DataSet} from "./data-set";
import {BarDataSet} from "./bar-data-set";
import {LineDataSet} from "./line-data-set";
import {RadarDataSet} from "./radar-data-set";
import {PielikeDataSet} from "./pielike-data-set";
import {PolarDataSet} from "./polar-data-set";
import {ColorScheme} from "./color-scheme";
import {TranslateData} from "./translate-data";
import {ChartOptions} from "./chart-options";

export class UibuilderChart extends ThemableMixin(PolymerElement) {

    static get template() {
        return html`
        <style>
            :host {
                display: flex;
            }
            
            #chartContainer {
                position: relative;
                flex: 1;
            }
        </style>
        
        <slot></slot>
        
        <div id="chartContainer">
            <canvas id="chartCanvas"></canvas>
        </div>
        `;
    }

    static get properties() {
        return {
            baseType: {
                type: String,
                value: 'bar' // values: 'bar', 'horizontalBar', 'line', 'radar', 'pie', 'doughnut', 'polarArea'
            }
        };
    }

    _uibuilderReady() {
        this._sendConfigToBackend();
    }

    _findDataSource() {
        let ds = this.querySelector(DataSource.is);
        if (!ds) {
            ds = this.querySelector(ItemDataSource.is);
        }
        return ds;
    }

    _sendConfigToBackend() {
        const ds = this._findDataSource();

        if (!ds) {
            throw new Error(`${DataSource.is} is missing from ${this.tagName} with id:'${this.id}'`);
        } else {
            if (ds instanceof DataSource) {
                this._dataSource = {
                    id: ds.datasourceId,
                    name: ds.name,
                    defaultQuery: ds.defaultQuery
                };
            } else {
                this._dataSource = {
                    id: ds.getAttribute('datasource-id'),
                    name: ds.name,
                    defaultQuery: ds.defaultQuery
                };
            }
        }

        this._labels = Array.from(this.children)
            .find(it => it instanceof ChartLabels);

        this._dataSets = Array.from(this.children)
            .filter(it => it instanceof DataSet);

        this._options = Array.from(this.children)
            .find(it => it instanceof ChartOptions);

        let dataSets = UibuilderChart._getPropertiesOf(this._dataSets, it => it instanceof DataSet);

        if (!this._labels)
            throw new Error(`The labels are specified incorrectly for chart with id: ${this.id}`);

        let labels = this._labels._getPropertiesObject();
        let options = undefined;

        if (this._options)
            options = this._options._getPropertiesObject();

        this.dispatchEvent(new CustomEvent('chart-properties-ready', {
            detail: {
                dataSource: this._dataSource,
                labels: labels,
                dataSets: dataSets,
                ...(options && {options: options})
            }
        }));
    }

    _resetChart(data) {
        if (this.__chart) {
            this.__chart.destroy();
            this.__chart = null;
        }

        this.__chart = new Chart(this.$.chartCanvas.getContext('2d'), {
            type: this.baseType,
            data: this.__convertToChartData(data),
            ...(this._options && {options: this._options.toChartObject()})
        });
    }

    __convertToChartData(data) {
        if (!data || data.length === 0) {
            return {labels: [], datasets: []};
        }
        const chartDataSets = this._convertToDataSets(data);
        return {
            labels: data[0],
            datasets: chartDataSets
        };
    }

    _update(data) {
        if (!this.__chart) {
            this._resetChart(data);
        } else {
            this.__chart.data = this.__convertToChartData(data);
            this.__chart.update();
        }
    }

    __getCachedAnimDuration() {
        if (!this.__animDurationCache) {
            if (this._options && this._options.toChartObject) {
                const chartObj = this._options.toChartObject();
                if (chartObj.animation && chartObj.animation.duration) {
                    this.__animDurationCache = chartObj.animation.duration;
                } else {
                    this.__animDurationCache = 800;
                }
            } else {
                this.__animDurationCache = 800;
            }
        }
        return this.__animDurationCache;
    }

    _pushNewData(data, removeOld) {
        if (this.__chart) {
            const chartData = this.__convertToChartData(data);

            const shift = removeOld
                && (this.baseType === 'bar' || this.baseTyoe === 'line')
                && this.__chart.data.labels.length > chartData.labels.length;

            const animDuration = this.__getCachedAnimDuration();
            const currentLength = this.__chart.data.labels[0].length;
            const currentLabelMin = this.__chart.data.labels[0];
            const currentLabelMax = this.__chart.data.labels[this.__chart.data.labels.length - 1];
            const targetLabelMin = this.__chart.data.labels[chartData.labels.length];
            const targetLabelMax = chartData.labels[chartData.labels.length - 1];

            this.__chart.options.scales.xAxes[0].ticks.min = currentLabelMin;
            this.__chart.options.scales.xAxes[0].ticks.max = currentLabelMax;

            for (let i = 0; i < chartData.labels.length; i++) {
                this.__chart.data.labels.push(chartData.labels[i]);
                if (removeOld && !shift)
                    this.__chart.data.labels.shift();

                for (let j = 0; j < chartData.datasets.length; j++) {
                    this.__chart.data.datasets[j].data.push(chartData.datasets[j].data[i]);
                    if (removeOld && !shift)
                        this.__chart.data.datasets[j].data.shift();
                }
            }

            this.__chart.update();

            if (shift) {
                setTimeout(() => {
                    this.__chart.options.scales.xAxes[0].ticks.min = targetLabelMin;
                    this.__chart.options.scales.xAxes[0].ticks.max = targetLabelMax;
                    this.__chart.update();
                    setTimeout(() => {
                        for (let i = 0; i < chartData.labels.length; i++) {
                            this.__chart.data.labels.shift();
                            this.__chart.data.datasets.forEach((ds) => ds.data.shift());
                        }
                        this.__chart.update();
                    }, animDuration);
                }, 10);
            }

            if (!shift) {
                setTimeout(() => {
                    this.__chart.options.scales.xAxes[0].ticks.max = targetLabelMax;
                    this.__chart.update();
                }, animDuration)
            }
        }
    }

    _convertToDataSets(data) {
        if (data.length - 1 !== this._dataSets.length) {
            throw new Error('Got data array with different size as dataset size');
        }

        for (let i = 1; i < data.length; i++) {
            this._dataSets[i - 1].setData(data[i]);
        }

        return this._dataSets.map(it => it.toChartObject());
    }

    static _getPropertiesOf(array, typeFilter) {
        if (array) {
            return array
                .filter(typeFilter)
                .map((it) => it._getPropertiesObject());
        }
        return false;
    }


    static get is() {
        return 'uibuilder-chart';
    }

}

customElements.define(UibuilderChart.is, UibuilderChart);

// TODO add support for complex data structures (line x,y and bubble x,y,r)