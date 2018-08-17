package com.example.sendmessage;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static com.example.sendmessage.MainActivity.firstRow;

/**
 * Created by 帅郑 on 2018/8/9.
 */

public class ImportDataFromExcel {
    private static List<Person> people;
    public static List<Person> ImportExcelData(String path) {
        people = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            //遍历excel文件的每行每列
            for (int i = 0; i < rows; i++) {
                //遍历行
                List<String> li = new ArrayList<>();
                for (int j = 0; j < columns; j++) {
                    Cell cell = sheet.getCell(j, i);
                    String result = cell.getContents();
                    if(i == 0) {
                        firstRow.add(result);
                    }else {
                        li.add(result);
                    }
                }
                if (li.size() > 0) {
                    people.add(new Person(li));
                }
                li = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return people;
    }
}
