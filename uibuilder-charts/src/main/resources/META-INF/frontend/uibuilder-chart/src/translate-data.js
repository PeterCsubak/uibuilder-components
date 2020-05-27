import {PolymerElement} from "@polymer/polymer/polymer-element.js";

export class TranslateData extends PolymerElement {

    static get properties() {
        return {
            map: {
                type: String
            },
            reduce: {
                type: String
            }
        }
    }

    _getPropertiesObject() {
        return {
            translateData: {
                map: this.map,
                reduce: this.reduce
            }
        }
    }

    static get is() {
        return 'translate-data';
    }

}

customElements.define(TranslateData.is, TranslateData);