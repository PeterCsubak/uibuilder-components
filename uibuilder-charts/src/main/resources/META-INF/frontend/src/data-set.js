import { PolymerElement } from '@polymer/polymer/polymer-element.js';

export class DataSet extends PolymerElement {

    static get properties() {
        return {
            value: {
                type: String
            }
        };
    }

    _getPropertiesObject() {
        return {value: this.value};
    }

    /**
     * @protected
     * @abstract
     */
    _convertToChartObject() {
    }

    /**
     * @param data {array}
     * @abstract
     */
    setData(data) {
    }

    static get is() {
        return 'data-set';
    }

}

customElements.define(DataSet.is, DataSet);