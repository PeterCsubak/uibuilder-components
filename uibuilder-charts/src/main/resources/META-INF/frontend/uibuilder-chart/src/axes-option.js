import {PolymerElement} from "@polymer/polymer/polymer-element.js";

export class AxesOption extends PolymerElement {

    static get properties() {
        return {
            type: {
                type: String,
                value: 'x' //values: 'x', 'y'
            },
            stacked: {
                type: Boolean,
                value: false
            },
            scaleType: {
                type: String
            },
            axisId: {
                type: String
            },
            position: {
                type: String //values: left, right, top, bottom
            }
        };
    }

    toChartObject() {
        return {
            ...(this.axisId && {id: this.axisId}),
            ...(this.stacked && {stacked: this.stacked}),
            ...(this.scaleType && {type: this.scaleType}),
            ...(this.position && {position: this.position})
        }
    }

    static get is() {
        return 'axes-option';
    }

}

customElements.define(AxesOption.is, AxesOption);