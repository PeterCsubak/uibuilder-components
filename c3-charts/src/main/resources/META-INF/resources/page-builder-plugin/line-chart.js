AppDsg.PageBuilder.PluginRegistry.register({
    polymerImport: '/c3-chart/line-chart.html',

    apply: function ({
                         BlockManager,
                         DomComponents
                     }) {
        BlockManager.add('line-chart', {
            category: 'Charts',
            attributes: {
                style: 'order: 0'
            },
            label: `<div class="gjs-block-label">Line chart</div>`,
            content: '<line-chart></line-chart>',
        });

        const {
            model,
            view
        } = DomComponents.getType('default');
        DomComponents.addType('line-chart', {
            removable: true,
            draggable: true,
            droppable: true,
            copyable: true,
            model: model.extend({
                defaults: {
                    ...model.prototype.defaults,
                    properties: [
                        ...AppDsg.PageBuilder.Traits.base,
                        {
                            path: 'hide-points',
                            type: 'checkbox'
                        },
                        {path: 'points-radius'},
                        {path: 'points-expand-radius'},
                        {
                            path: 'enable-zoom',
                            type: 'checkbox'
                        },
                        {
                            path: 'enable-subchart',
                            type: 'checkbox'
                        },
                        {
                            name: 'Connect null values',
                            path: 'connect-null',
                            type: 'checkbox'
                        },
                    ],
                },

                init() {

                },
            }, {
                isComponent: function (el) {
                    if (el.tagName === 'LINE-CHART')
                        return {type: 'line-chart'};
                },
            }),
            view: view.extend({
                init() {
                    this.onAttachShadow(shadowRoot => {
                        this.el.data = {
                            columns: [
                                ['data1', 30, 200, 100, 400, 150, 250, 110, 300],
                                ['data2', 50, 20, null, 40, 15, 25, null, 10]
                            ],
                            names: {
                                data1: 'Preview data 1',
                                data2: 'Preview data 2'
                            }
                        }
                    });
                },
            })
        });
    }
});