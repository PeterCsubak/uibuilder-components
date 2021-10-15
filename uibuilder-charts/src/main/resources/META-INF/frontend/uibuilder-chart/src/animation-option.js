import {PolymerElement} from "@polymer/polymer/polymer-element.js";

export class AnimationOption extends PolymerElement{

    static get properties() {
        return {
            duration: {
                type: Number,
                value: 1000
            },
            easing: {
                type: String,
                value: 'easeOutQuart' // values: 'linear', 'easeInQuad', 'easeOutQuad', 'easeInOutQuad', 'easeInCubic',
                // 'easeOutCubic', 'easeInOutCubic', 'easeInQuart', 'easeOutQuart', 'easeInOutQuart', 'easeInQuint',
                // 'easeOutQuint', 'easeInOutQuint', 'easeInSine', 'easeOutSine', 'easeInOutSine', 'easeInExpo',
                // 'easeOutExpo', 'easeInOutExpo', 'easeInCirc', 'easeOutCirc', 'easeInOutCirc', 'easeInElastic',
                // 'easeOutElastic', 'easeInOutElastic', 'easeInBack', 'easeOutBack', 'easeInOutBack', 'easeInBounce',
                // 'easeOutBounce', 'easeInOutBounce'
            }
        }
    }

    toChartObject() {
        return {
            duration: this.duration,
            easing: this.easing
        }
    }

    static get is() {
        return 'animation-option';
    }

}

customElements.define(AnimationOption.is, AnimationOption);