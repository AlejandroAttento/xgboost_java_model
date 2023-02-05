package utils;

import dataModels.RawDatasource;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class DataChecks {
    private ArrayList<RawDatasource> data;

    public DataChecks(ArrayList<RawDatasource> data) {
        this.data = data;
    }

    public ArrayList<RawDatasource> nullChecks() {

        AtomicReference<Integer> i = new AtomicReference<>(0);
        for (RawDatasource row : this.data) {

            AtomicReference<Boolean> nullIndicator = new AtomicReference<>(false);
            ReflectionUtils.doWithFields(row.getClass(), field -> {

                    if (field.get(row) == null) {
                        nullIndicator.set(true);
                    }

                    if (nullIndicator.get()) {
                        this.data.remove(i);
                        i.getAndSet(i.get() - 1);
                    }

                }
            );
            i.getAndSet(i.get() + 1);

        }

        return this.data;

    }

}
