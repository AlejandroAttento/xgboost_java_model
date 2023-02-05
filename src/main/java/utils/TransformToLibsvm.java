package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;
import dataModels.RawDatasource;
import org.springframework.util.ReflectionUtils;

public class TransformToLibsvm {
    public void transform(ArrayList<RawDatasource> data, String targetPath) throws IOException {

        CSVWriter lsvm_writer = new CSVWriter(new FileWriter(targetPath),' ', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");

        for (RawDatasource row : data) {

            final String[] libsvmRow = new String[17];
            final Integer[] j = {0};

            ReflectionUtils.doWithFields(row.getClass(), field -> {

                    if (field.getName().equals("beanClass")) {
                        libsvmRow[j[0]] = (field.get(row).toString());
                    }
                    else {
                        libsvmRow[j[0]] = j[0] + ":" + field.get(row).toString();
                    }

                    j[0]++;

                });

            lsvm_writer.writeNext(libsvmRow);

        }

    }
}
