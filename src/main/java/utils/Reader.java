package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import dataModels.RawDatasource;

public class Reader {
    private String path;

    public Reader(String path) {
        this.path = path;
    }

    public ArrayList<RawDatasource> fromExcel() throws IOException {
        FileInputStream file = new FileInputStream(new File(this.path));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        ArrayList<RawDatasource> data = new ArrayList<>();

        Integer i = 0;
        for (Row row : sheet) {

            if (i != 0) {

                RawDatasource rowData = new RawDatasource(row.getCell(16).toString(), row.getCell(0).toString(),
                        row.getCell(1).toString(), row.getCell(2).toString(), row.getCell(3).toString(),
                        row.getCell(4).toString(), row.getCell(5).toString(), row.getCell(6).toString(),
                        row.getCell(7).toString(), row.getCell(8).toString(), row.getCell(9).toString(),
                        row.getCell(10).toString(), row.getCell(11).toString(), row.getCell(12).toString(),
                        row.getCell(13).toString(), row.getCell(14).toString(), row.getCell(15).toString());

                data.add(rowData);
            }
            i++;
        }

        return data;

    }
}

