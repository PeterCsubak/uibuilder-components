import { PolymerElement } from '@polymer/polymer/polymer-element.js';

export class ChartLabels extends PolymerElement {

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

    static get is() {
        return 'chart-labels';
    }

}

customElements.define(ChartLabels.is, ChartLabels);