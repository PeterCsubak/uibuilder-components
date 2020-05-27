import {PolymerElement} from "@polymer/polymer/polymer-element.js";

export class ColorScheme extends PolymerElement {

    static get properties() {
        return {
            scheme: {
                type: String,
                value: 'brewer.Paired12'
            },
            fillAlpha: {
                type: Number,
                value: 0.5
            },
            reverse: {
                type: Boolean,
                value: false
            },
            override: {
                type: Boolean,
                value: false
            },
            //TODO add custom option for these
            customPalette: {
                type: Array
            },
            extendPalette: {
                type: Array
            }
        };
    }

    isPlugin() {
        return true;
    }

    toChartObject() {
        return {
            colorschemes: {
                scheme: this.scheme,
                fillAlpha: this.fillAlpha,
                reverse: this.reverse,
                override: this.override
                //TODO custom options
            }
        }
    }

    static get is() {
        return 'color-scheme';
    }

}

customElements.define(ColorScheme.is, ColorScheme);