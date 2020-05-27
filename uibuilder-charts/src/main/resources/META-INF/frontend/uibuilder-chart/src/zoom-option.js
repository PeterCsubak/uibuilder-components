import {PolymerElement} from "@polymer/polymer/polymer-element.js";

export class ZoomOption extends PolymerElement {

    static get properties() {
        return {
            panEnabled: {
                type: Boolean
            },
            panMode: {
                type: String //values: x, y, xy
            },
            panRangeMinX: {
                type: Number
            },
            panRangeMaxX: {
                type: Number
            },
            panRangeMinY: {
                type: Number
            },
            panRangeMaxY: {
                type: Number
            },
            panSpeed: {
                type: Number
            },
            panThreshold: {
                type: Number
            },

            zoomEnabled: {
                type: Boolean
            },
            zoomDrag: {
                type: Boolean
            },
            zoomMode: {
                type: String //values: x, y, xy
            },
            zoomRangeMinX: {
                type: Number
            },
            zoomRangeMaxX: {
                type: Number
            },
            zoomRangeMinY: {
                type: Number
            },
            zoomRangeMaxY: {
                type: Number
            },
            zoomSpeed: {
                type: Number
            },
            zoomThreshold: {
                type: Number
            },
            zoomSensitivity: {
                type: Number
            }
        };
    }

    isPlugin() {
        return true;
    }

    toChartObject() {
        return {
            zoom: {
                pan: this._collectOptions('pan'),
                zoom: this._collectOptions('zoom')
            }
        }
    }

    _collectOptions(type) {
        let rangeMin = {};
        let rangeMax = {};
        let res = {};
        for (let key in this) {
            if (this.hasOwnProperty(key) && key.startsWith(type) && this[key]) {
                let objectKey = key.substring(type.length);
                objectKey = objectKey[0].toLowerCase() + objectKey.substring(1);
                if (objectKey.startsWith('rangeMin')) {
                    rangeMin[objectKey[objectKey.length - 1].toLowerCase()] = this[key];
                } else if (objectKey.startsWith('rangeMax')) {
                    rangeMax[objectKey[objectKey.length - 1].toLowerCase()] = this[key];
                } else {
                    res[objectKey] = this[key];
                }
            }
        }

        if (Object.keys(rangeMin).length !== 0) {
            res.rangeMin = rangeMin;
        }

        if (Object.keys(rangeMax).length !== 0) {
            res.rangeMax = rangeMax;
        }

        return res;
    }

    static get is() {
        return 'zoom-option';
    }

}

customElements.define(ZoomOption.is, ZoomOption);