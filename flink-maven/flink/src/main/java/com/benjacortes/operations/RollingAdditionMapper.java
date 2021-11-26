package com.benjacortes.operations;

import com.benjacortes.models.ClickEvent;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;

public class RollingAdditionMapper extends RichMapFunction<ClickEvent, ClickEvent> {

    private static final long serialVersionUID = 1180234853172462378L;

    private transient ValueState<Integer> currentTotalCount;

    @Override
    public ClickEvent map(ClickEvent event) throws Exception {
        Integer totalCount = currentTotalCount.value();

        if (totalCount == null) {
            totalCount = 0;
        }
        totalCount += event.getFrequency();

        currentTotalCount.update(totalCount);

        return new ClickEvent(event.getSessionId(), event.getPageId(), totalCount);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        currentTotalCount =
                getRuntimeContext()
                        .getState(new ValueStateDescriptor<>("currentTotalCount", Integer.class));
    }
}
