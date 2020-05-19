import {DataSet} from "./data-set";

export class BarDataSet extends DataSet {

    static get properties() {
        return {
            ...DataSet.properties(),
            backgroundColor: {
                type: String,
                value: 'rgba(0, 0, 0, 0.1)'
            },
            borderColor: {
                type: String,
                value: 'rgba(0, 0, 0, 0.1)'
            },
            borderSkipped: {
                type: String,
                value: 'bottom'
            },
            borderWidth: {
                type: Number,
                value: 0
            },
            hoverBackgroundColor: {
                type: String
            },
            hoverBorderColor: {
                type: String
            },
            hoverBorderWidth: {
                type: Number,
                value: 1
            },
            label: {
                type: String,
                value: ''
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

    _convertToChartObject() {
        res = {};

        for (key in this) {
            if (key === 'value')
                continue;
            if (BarDataSet.properties[key] && this[key]) {
                res[key] = this[key];
            }
        }

        return {
            data: this.data,
            ...res
        };
    }

    setData(data) {
        this.data = data;
    }
}