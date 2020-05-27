import { PolymerElement } from '@polymer/polymer/polymer-element.js';

export class DataSet extends PolymerElement {

    static get properties() {
        return {
            value: {
                type: String
            },
            backgroundColor: {
                type: String
            }, //TODO color is tricky, it can be string, array or function, also it can pattern or gradient too,
            // so it has to be done in a more configurable manner
            borderColor: {
                type: String
            },
            borderWidth: {
                type: Number
            },
            hoverBackgroundColor: {
                type: String
            },
            hoverBorderColor: {
                type: String
            },
            hoverBorderWidth: {
                type: Number
            },
            label: {
                type: String,
                value: ''
            },
            xAxisId: {
                type: String
            },
            yAxisId: {
                type: String
            }
        };
    }

    _getPropertiesObject() {
        return {value: this.value};
    }

    /**
     * @protected
     * @param propertyDeclarations {object}
     * @return {object}
     */
    getPropertyValues(propertyDeclarations) {
        let res = {};

        for (let key in this) {
            if (key === 'value')
                continue;
            if (propertyDeclarations[key] && this[key]) {
                if (key === 'yAxisId') {
                    res['yAxisID'] = this[key];
                } else if (key === 'xAxisId') {
                    res['xAxisID'] = this[key];
                } else {
                    res[key] = this[key];
                }
            }
        }

        return res;
    }

    /**
     * @protected
     */
    toChartObject() {
        let props = this.getPropertyValues(DataSet.properties);
        return {
            data: this.data,
            ...props
        }
    }

    /**
     * @param data {array}
     */
    setData(data) {
        this.data = data;
    }

    static get is() {
        return 'data-set';
    }

}

customElements.define(DataSet.is, DataSet);