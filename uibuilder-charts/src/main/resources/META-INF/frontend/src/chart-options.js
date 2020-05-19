import { PolymerElement } from '@polymer/polymer/polymer-element.js';

export class ChartOptions extends PolymerElement {

    toChartObject() {
        return {}; // TODO implement later with details, especially dont forget to add axes support to datasets too
    }

    _getPropertiesObject() {
        return {}; // TODO traverse the contained objects and return a single object with relevant info (if any is relevant to backend)
    }

    static get is() {
        return 'chart-options';
    }

}


customElements.define(ChartOptions.is, ChartOptions);