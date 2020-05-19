import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import {ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import {DataSource} from "@vaadin/flow-frontend/uibuilder/data/data-source.js";
import {Chart} from 'chart.js';
import {ChartLabels} from "./chart-labels";
import {DataSet} from "./data-set";
import {ChartOptions} from "./chart-options";

export class UibuilderChart extends ThemableMixin(PolymerElement) {

    static get template() {
        return html`
        <style>
            :host {
                width: 100%;
                height: 100%;
            }
        </style>
        
        <slot></slot>
        
        <canvas id="chartCanvas"></canvas>
        `;
    }

    static get properties() {
        return {
            baseType: {
                type: String,
                value: 'bar'
            }
        };
    }

    _uibuilderReady() {
        this._sendConfigToBackend();
    }

    _sendConfigToBackend() {
        const ds = this.querySelector(DataSource.is);

        if (!ds) {
            throw new Error(`${DataSource.is} is missing from ${this.tagName} with id:'${this.id}'`);
        } else {
            this._dataSource = {
                id: ds.datasourceId,
                name: ds.name,
                defaultQuery: ds.defaultQuery
            };
        }

        const propertyHolders = this.children
            .filter(it => it instanceof ChartLabels
                || it instanceof DataSet
                || it instanceof ChartOptions);

        this._labels = UibuilderChart._getPropertiesOf(propertyHolders, it => it instanceof ChartLabels);
        this._dataSets = UibuilderChart._getPropertiesOf(propertyHolders, it => it instanceof DataSet);
        this._options = UibuilderChart._getPropertiesOf(propertyHolders, it => it instanceof ChartOptions);

        if (!this._labels && length(this._labels) !== 1)
            throw new Error(`The labels are specified incorrectly for chart with id: ${this.id}`);
        else
            this._labels = this._labels[0];

        if (this._options && length(this._options) > 0)
            this._options = this._options[0];
        else
            this._options = undefined;

        this.dispatchEvent(new CustomEvent('chart-properties-ready', {
            detail: {
                labels: this._labels,
                dataSets: this._dataSets,
                ...(this._options && {options: this._options})
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
        if (!data || length(data) === 0) {
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

    _convertToDataSets(data) {
        if (length(data) - 1 !== length(this._dataSets)) {
            throw new Error('Got data array with different size as dataset size');
        }

        for (let i = 1; i < length(data); i++) {
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