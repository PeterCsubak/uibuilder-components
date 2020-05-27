import {DataSet} from "./data-set";

export class PielikeDataSet extends DataSet {

    static get properties() {
        return {
            ...DataSet.properties,
            borderAlign: {
                type: String,
                value: 'center' //values: 'center', 'inner'
            },
            weight: {
                type: Number,
                value: 1
            }
        }
    }

    toChartObject() {
        return {
            data: this.data,
            ...this.getPropertyValues(PielikeDataSet.properties)
        };
    }

    static get is() {
        return 'pielike-data-set';
    }
}

customElements.define(PielikeDataSet.is, PielikeDataSet);
