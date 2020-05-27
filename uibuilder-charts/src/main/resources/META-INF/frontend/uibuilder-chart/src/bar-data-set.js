import {DataSet} from "./data-set";

export class BarDataSet extends DataSet {

    static get properties() {
        return {
            ...DataSet.properties,
            borderSkipped: {
                type: String,
                value: 'bottom'
            },
            order: {
                type: Number,
                value: 0
            },
            barPercentage: {
                type: Number,
                value: 0.9
            },
            categoryPercentage: {
                type: Number,
                value: 0.8
            },
            barThickness: {
                type: String
            },
            maxBarThickness: {
                type: Number
            },
            minBarLength: {
                type: Number
            }
        }
    }

    toChartObject() {
        return {
            data: this.data,
            ...this.getPropertyValues(BarDataSet.properties),
            type: 'bar'
        };
    }

    static get is() {
        return 'bar-data-set';
    }
}

customElements.define(BarDataSet.is, BarDataSet);
