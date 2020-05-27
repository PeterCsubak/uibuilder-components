import {DataSet} from "./data-set";

export class RadarDataSet extends DataSet {

    static get properties() {
        return {
            ...DataSet.properties,
            borderCapStyle: {
                type: String,
                value: 'butt'
            },
            borderDash: {
                type: Array,
                value: []
            },
            borderDashOffset: {
                type: Number,
                value: 0.0
            },
            borderJoinStyle: {
                type: String,
                value: 'miter'
            },
            fill: {
                type: String,
                value: 'origin' // values: 1, 2, 3... +1, -1, +2, -2... start, end, origin, (or falsy e.g.: '')
            },
            hoverBorderCapStyle: {
                type: String
            },
            hoverBorderDash: {
                type: Array
            },
            hoverBorderDashOffset: {
                type: Number
            },
            hoverBorderJoinStyle: {
                type: String
            },
            lineTension: {
                type: Number,
                value: 0.4
            },
            order: {
                type: Number,
                value: 0
            },
            pointBackgroundColor: {
                type: String,
                value: 'rgba(0, 0, 0, 0.1)'
            },
            pointBorderColor: {
                type: String,
                value: 'rgba(0, 0, 0, 0.1)'
            },
            pointBorderWidth: {
                type: Number,
                value: 1
            },
            pointHitRadius: {
                type: Number,
                value: 1
            },
            pointHoverBackgroundColor: {
                type: String
            },
            pointHoverBorderColor: {
                type: String
            },
            pointHoverBorderWidth: {
                type: Number,
                value: 1
            },
            pointHoverRadius: {
                type: Number,
                value: 4
            },
            pointRadius: {
                type: Number,
                value: 3
            },
            pointRotation: {
                type: Number,
                value: 0
            },
            pointStyle: {
                type: String,
                value: 'circle' // values: 'circle', 'cross', 'crossRot', 'dash', 'line', 'rect', 'rectRounded',
                // 'rectRot', 'star', 'triangle' TODO: figure out how to support image values
            },
            spanGaps: {
                type: Boolean
            }
        }
    }

    toChartObject() {
        return {
            data: this.data,
            ...this.getPropertyValues(RadarDataSet.properties),
            type: 'radar'
        };
    }

    static get is() {
        return 'radar-data-set';
    }
}

customElements.define(RadarDataSet.is, RadarDataSet);
