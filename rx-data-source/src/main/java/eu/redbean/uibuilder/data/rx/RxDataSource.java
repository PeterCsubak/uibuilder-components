package eu.redbean.uibuilder.data.rx;

import io.devbench.uibuilder.api.parse.BindingContext;
import io.devbench.uibuilder.data.api.datasource.DataSource;
import io.devbench.uibuilder.data.api.datasource.FetchRequest;
import io.devbench.uibuilder.data.api.filter.FilterExpression;
import io.devbench.uibuilder.data.api.filter.FilterExpressionFactory;
import io.devbench.uibuilder.data.api.order.OrderExpression;
import io.devbench.uibuilder.data.api.order.OrderExpressionFactory;
import io.reactivex.rxjava3.core.Observable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class RxDataSource<ELEMENT,
        ORDER_EXPRESSION_TYPE extends OrderExpression,
        FILTER_EXPRESSION_TYPE extends FilterExpression<?>>
        implements DataSource<Observable<ELEMENT>, ORDER_EXPRESSION_TYPE, FILTER_EXPRESSION_TYPE, FetchRequest> {


    @Override
    public FilterExpressionFactory<FILTER_EXPRESSION_TYPE> getFilterExpressionFactory() {
        return null;
    }

    @Override
    public OrderExpressionFactory<ORDER_EXPRESSION_TYPE> getOrderExpressionFactory() {
        return null;
    }

    @Override
    public void registerFilter(FILTER_EXPRESSION_TYPE expression) {

    }

    @Override
    public void registerOrder(ORDER_EXPRESSION_TYPE expression) {

    }

    @Override
    public long fetchSize(@Nullable FetchRequest request) {
        return 0;
    }

    @Override
    public boolean hasChildren(FILTER_EXPRESSION_TYPE request) {
        return false;
    }

    @Override
    public Observable<ELEMENT> fetchData(@Nullable FetchRequest request) {
        return null;
    }

    @Override
    public Collection<String> getBindings() {
        return null;
    }
}
