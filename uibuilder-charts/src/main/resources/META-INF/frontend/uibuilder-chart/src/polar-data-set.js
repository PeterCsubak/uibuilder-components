import {DataSet} from "./data-set";

export class PolarDataSet extends DataSet {

    static get properties() {
        return {
            ...DataSet.properties,
            borderAlign: {
                type: String,
                value: 'center' //values: 'center', 'inner'
            }
        }
    }

    toChartObject() {
        return {
            data: this.data,
            ...this.getPropertyValues(PolarDataSet.properties)
        };
    }

    static get is() {
        return 'polar-data-set';
    }
}

customElements.define(PolarDataSet.is, PolarDataSet);
