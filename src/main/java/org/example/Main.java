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
 * @author 2162175121
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        //创建二维数组
        int[][] container = getValue();
        //处理数据
        recy(container);
        //展示数据
        for (int a = 0; a < container.length; a++) {
            for (int b = 0; b < container[a].length; b++) {
                System.out.print(container[a][b]+",");
            }
            System.out.println();
        }

    }

    private static boolean recy(int[][] container) {
        //循环遍历二维数组
        for (int a = 0; a < container.length; a++) {
            for (int b = 0; b < container[a].length; b++) {
                //找出非0的并跳过，因为非0代表框内存在数字
                if (container[a][b] != 0) {
                    continue;
                }
                //0的框内试1-9
                for (int i = 1; i <= 9; i++) {
                    //判断是否可行
                    boolean valid = isValid(container, a, b, i);
                    //如果填进去没问题（可行）
                    if (valid) {
                        //二位数组坐标里填进去
                        container[a][b] = i;
                        //下一步便是再次执行这个方法去搞下一个空位，也就是后面填0的
                        if (recy(container)) {
                            //下面这个true原则上来说不会进去，因为总有方法进行
                            return true;
                        }
                        //这儿也是一般不会进去，除非碰到了一个空1-9都不行，这时候就会走下面的
                        container[a][b] = 0;
                    }
                }
                //返回false，这时候有意思了，这个方法返回false,上面的if (recy(container)) 就会走不下去，就这会停止这一层方法并返回上一层方法继续试1-9，这样反推上去，总会找到
                return false;
            }
        }
        //等到这儿就说明该填的都填完了，收工
        return true;
    }

    /**
     * 这是一个条件判断的方法，看不解释
     * @param container
     * @param x
     * @param y
     * @param currentNum
     * @return
     */
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

    /**
     * 这是读取二维表的方法，看不解释
     * @return
     * @throws IOException
     */
    public static int[][] getValue() throws IOException {

        int[][] contain = new int[9][9];
        //改成你自己的本地路径，或者做数据流也不是不行
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