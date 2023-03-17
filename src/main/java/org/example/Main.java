package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author 15553
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        int[][] container = getValue();

        recy(container);

        for (int a = 0; a < container.length; a++) {
            for (int b = 0; b < container[a].length; b++) {
                System.out.print(container[a][b]+",");
            }
            System.out.println();
        }

    }

    private static boolean recy(int[][] container) {
        for (int a = 0; a < container.length; a++) {
            for (int b = 0; b < container[a].length; b++) {
                if (container[a][b] != 0) {
                    continue;
                }
                for (int i = 1; i <= 9; i++) {
                    boolean valid = isValid(container, a, b, i);
                    if (valid) {
                        container[a][b] = i;
                        if (recy(container)) {
                            return true;
                        }
                        container[a][b] = 0;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(int[][] container, int x, int y, int currentNum) {

        //分大组0,1,2，只要*3+1的话可以得出大组初始坐标
        int xGroup = (x / 3) * 3;
        int yGroup = (y / 3) * 3;

        for (int xStart = xGroup; xStart < xGroup + 3; xStart++) {
            for (int yStart = yGroup; yStart < yGroup + 3; yStart++) {
                if (currentNum == container[xStart][yStart]) {
                    return false;
                }
            }
        }

        for (int xBegin = 0; xBegin < 9; xBegin++) {
            if (y == xBegin) {
                continue;
            }
            if (container[x][xBegin] == currentNum) {
                return false;
            }
        }

        for (int yBegin = 0; yBegin < 9; yBegin++) {
            if (x == yBegin) {
                continue;
            }
            if (container[yBegin][y] == currentNum) {
                return false;
            }
        }

        return true;
    }

    public static int[][] getValue() throws IOException {

        int[][] contain = new int[9][9];
        String path = "E:\\Solo.xlsx";
        InputStream is = new FileInputStream(path);
        Workbook wb = new XSSFWorkbook(is);

        Sheet sheet = wb.getSheetAt(0);

        Row row;
        Cell cell;

        Iterator<Row> rows = sheet.rowIterator();
        //跳过第一行
        rows.next();

        int rowIndex = 0;
        while (rows.hasNext()) {
            row = rows.next();

            Iterator<Cell> cells = row.cellIterator();
            int cellIndex = 0;
            while (cells.hasNext()) {
                cell = cells.next();

                int cellType = cell.getCellType();

                if (cellType == 3) {
                    contain[rowIndex][cellIndex] = 0;
                } else {
                    contain[rowIndex][cellIndex] = (int) cell.getNumericCellValue();
                }
                cellIndex++;
            }
            rowIndex++;
        }
        return contain;
    }
}