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

/**
 * Created by 帅郑 on 2018/8/9.
 */

public class ImportDataFromExcel {
    private static List<person> people;
    public static List<person> ImportExcelData(String path) {
        people = new ArrayList<>();
        Workbook workbook = null;
        try {
            Log.i("666",people.toString());
            workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            Log.i("666",people.toString());
            //遍历excel文件的每行每列
            for (int i = 0; i < rows; i++) {
                //遍历行
                List<String> li = new ArrayList<>();
                for (int j = 0; j < columns; j++) {
                    Cell cell = sheet.getCell(j, i);
                    String result = cell.getContents();
                    if (i != 0) {
                        li.add(result);
                        Log.i("666","2");
                    }
                }
                if (li.size() > 0) {
                    people.add(new person(li.get(0), li.get(1), li.get(2), li.get(3)));
                }
                li = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("666",people.toString());
        } catch (BiffException e) {
            e.printStackTrace();
            Log.i("666",people.toString());
        }
        Log.i("666",people.toString());
        return people;
    }
}
