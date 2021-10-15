import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import {AxesOption} from "./axes-option";
import {ZoomOption} from "./zoom-option";
import {AnimationOption} from "./animation-option";

export class ChartOptions extends PolymerElement {

    toChartObject() {
        let scaleOption = this._createScaleOption();
        let animationOption = this._findAnimationOption();
        return {
            ...(animationOption && {animation: animationOption}),
            ...(scaleOption && {scales: scaleOption}),
            plugins: this._collectPlugins()
        }; // TODO implement later with details, especially dont forget to add axes support to datasets too
    }

    _createScaleOption() {
        let xAxes = this._collectAxesOptions('x');
        let yAxes = this._collectAxesOptions('y');
        if ((xAxes && xAxes.length > 0) || (yAxes && yAxes.length > 0)) {
            return {
                ...((xAxes && xAxes.length > 0) && {xAxes: xAxes}),
                ...((yAxes && yAxes.length > 0) && {yAxes: yAxes})
            }
        }
        return false;
    }

    _findAnimationOption() {
        let res = Array.from(this.children)
            .find(it => it instanceof AnimationOption);
        if (res) {
            return res.toChartObject();
        }
        return false;
    }

    _collectAxesOptions(type) {
        return Array.from(this.children)
            .filter(it => it instanceof AxesOption)
            .filter(it => it.type === type)
            .map(it => it.toChartObject())
    }

    _collectPlugins() {
        return Array.from(this.children)
            .filter(it => it.isPlugin && it.toChartObject && it.isPlugin())
            .map(it => it.toChartObject())
            .reduce((object, item) => ({...object, ...item}), {});
    }

    _getPropertiesObject() {
        return this._collectOptionsForBackend();
    }

    _collectOptionsForBackend() {
        return Array.from(this.children)
            .filter(it => it._getPropertiesObject)
            .map(it => it._getPropertiesObject())
            .reduce((obj, item) => ({...obj, ...item}), {});
    }

    static get is() {
        return 'chart-options';
    }

}


customElements.define(ChartOptions.is, ChartOptions);