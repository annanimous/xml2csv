        package ru.energodata;

        import org.apache.poi.ss.usermodel.Cell;
        import org.apache.poi.xssf.usermodel.XSSFCell;
        import org.apache.poi.xssf.usermodel.XSSFRow;
        import org.apache.poi.xssf.usermodel.XSSFSheet;
        import org.apache.poi.xssf.usermodel.XSSFWorkbook;
        import ru.energodata.Model.Stroka;

        import java.io.*;
        import java.sql.SQLException;
        import java.util.*;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

        public class Main {
            private static List<Stroka> strokaList;
            private final static String OK = "ОК";
            private static int i = 1;
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: analizator <path to config file> <path to .xlsx file> ");
            System.out.println("Usage: analizator D:\\analizator\\config.properties D:\\analizator\\data\\ocr.xlsx ");
        } else {
            try {
                Config config = new Config();
                config.load(args[0]);
                strokaList = readFromExcel(args[1]);
                compareIndexes(config);
                writeToExcel(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

            private static void compareIndexes(Config config) throws IOException, SQLException, ClassNotFoundException {
                PostgreProcessor processor = new PostgreProcessor();
                processor.initialize(config);
                for (i = 1; i < strokaList.size(); i++ ) {
                    /*class MyRunnable implements Runnable
                    {
                        @Override
                        public void run() {
                            try {*/
                                compareRunnable(i, processor);/*
                            } catch ( SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    ExecutorService tasker = Executors.newFixedThreadPool(8);
                    for(int j = 0; j < 2; j++){ // t is time interval
                        tasker.execute(new MyRunnable());
                    }*/
                }
                processor.deinitialize();
            }
            private static void compareRunnable(int i, PostgreProcessor processor) throws SQLException {

                Stroka stroka = strokaList.get(i);
                String index_from_chdpa = "";
                String original_address = stroka.getOriginal_address();
                String ediWithoutIndex;
                String indexFromExcel = stroka.getIndex();
                String indexFromEDI;
                List<String> regionCodeList;
                List<String> raionCodeList;
                List<String> cityCodeList;
                List<String> subcityCodeList;
                List<String> streetCodeList;
                List<String> buildingCodeList;

                regionCodeList = (!stroka.getRegion().equals("")) ? processor.query("code", "kladr", stroka.getRegion()) : null;
                raionCodeList = (!stroka.getRaion().equals("")) ? processor.query("code", "kladr", stroka.getRaion()) : null;
                cityCodeList = (!stroka.getCity().equals("")) ? processor.query("code", "kladr", stroka.getCity()) : null;
                subcityCodeList = (!stroka.getSubcity().equals("")) ? processor.query("code", "kladr", stroka.getSubcity()) : null;
                streetCodeList = (!stroka.getStreet().equals("")) ? processor.query("code", "street", stroka.getStreet()) : null;
                Set<String> raionCodeSet = new HashSet<>();
                Set<String> cityCodeSet = new HashSet<>();
                Set<String> subcityCodeSet = new HashSet<>();
                Set<String> streetCodeSet = new HashSet<>();

                if (regionCodeList != null) { //Если regionCodeList = null, то в таблице не заполнен регион (и вообще ничего), определить инденкс по ЦХДПА невозможно
                    if (raionCodeList != null) { //Если raionCodeList = null, то в таблице не заполнен район, приравниваем список районов к регионам
                        for (String raion : raionCodeList)
                        {
                            for (String region : regionCodeList)
                            {
                                if (region.substring(0, 4).equals(raion.substring(0, 4))){
                                    raionCodeSet.add(raion);
                                }
                            }
                        }
                        if (raionCodeSet.isEmpty()){for (String raion : raionCodeList)
                        {
                            for (String region : regionCodeList)
                            {
                                if (region.substring(0, 2).equals(raion.substring(0, 2))){
                                    raionCodeSet.add(raion);
                                }
                            }
                        }}
                    }
                    else {
                        //System.out.println(stroka.getId()+ ")в данных адреса нет района");
                        raionCodeSet = new HashSet<>(regionCodeList);
                    }
                    if (cityCodeList != null) { //Если cityCodeList = null, то в таблице не заполнен город, приравниваем список городов к районам
                        for (String raion : raionCodeSet) {
                            for (String city : cityCodeList) {
                                if (city.substring(0, 7).equals(raion.substring(0, 7))) {
                                    cityCodeSet.add(city);
                                }
                            }
                        }
                        if (cityCodeSet.size() == 0)
                        {
                            for (String raion : raionCodeSet) {
                                for (String city : cityCodeList) {
                                    if (city.substring(0, 4).equals(raion.substring(0, 4))) {
                                        cityCodeSet.add(city);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        System.out.println(stroka.getId()+ ")в данных адреса нет города");
                        cityCodeSet = raionCodeSet;
                    }

                    if (subcityCodeList != null) {
                        for (String city : cityCodeSet) {
                            for (String subcity : subcityCodeList) {
                                if (city.substring(0, 10).equals(subcity.substring(0, 10))) {
                                    subcityCodeSet.add(subcity);
                                }
                            }
                        }
                        if (subcityCodeSet.size() == 0){
                            for (String city : cityCodeSet) {
                                for (String subcity : subcityCodeList) {
                                    if (city.substring(0, 7).equals(subcity.substring(0, 7))) {
                                        subcityCodeSet.add(subcity);
                                    }
                                }
                            }}
                        if (subcityCodeSet.size() == 0){
                            for (String city : cityCodeSet) {
                                for (String subcity : subcityCodeList) {
                                    if (city.substring(0, 4).equals(subcity.substring(0, 4))) {
                                        subcityCodeSet.add(subcity);
                                    }
                                }
                            }}
                        if (subcityCodeSet.size() == 0){
                            subcityCodeSet = cityCodeSet;
                        }
                    }
                    else {
                        subcityCodeSet = cityCodeSet;
                    }
                    if (streetCodeList != null) {
                        if (streetCodeList.size() > 0)
                        {
                            for (String subcity : subcityCodeSet)
                            {
                                for (String street : streetCodeList) {
                                    if (street.contains(subcity)) {
                                        streetCodeSet.add(street);
                                        //if (streetCodeSet.size() > 1)                                            {System.out.println("Дубль!");}
                                    }
                                }
                            }
                            if (streetCodeSet.size() == 0){
                                for (String city : cityCodeSet)
                                {
                                    for (String street : streetCodeList) {
                                        if (city.substring(0, 10).equals(street.substring(0, 10))) {
                                            streetCodeSet.add(street);
                                            //if (streetCodeSet.size() > 1)                                            {System.out.println("Дубль!");}
                                        }
                                    }
                                }
                            }
                            if (streetCodeSet.size() == 0){
                                for (String raion : raionCodeSet)
                                {
                                    for (String street : streetCodeList) {
                                        if (raion.substring(0, 7).equals(street.substring(0, 7))) {
                                            streetCodeSet.add(street);
                                            //if (streetCodeSet.size() > 1)                                            {System.out.println("Дубль!");}
                                        }
                                    }
                                }
                            }
                            if (streetCodeSet.size() == 0){
                                for (String raion : raionCodeSet)
                                {
                                    for (String street : streetCodeList) {
                                        if (raion.substring(0, 7).equals(street.substring(0, 7))) {
                                            streetCodeSet.add(street);
                                       {System.out.println("Дубль!");}
                                        }
                                    }
                                }
                            }
                            if (streetCodeSet.size() > 0) {
                                stroka.setAnaliz_street(OK);

                                Set<String> buildingCodeSet = new HashSet<>();


                                for (String street : streetCodeSet) {
                                    buildingCodeList = (!stroka.getBuilding().equals("")) ? processor.getBuilding(stroka.getBuilding(), street) : null;
                                    //TODO: медленно!
                                    //buildingCodeList = (!stroka.getBuilding().equals("")) ? processor.query("index", "street_zip", street) : null;
                                    if (buildingCodeList != null && !buildingCodeList.isEmpty()) {
                                        buildingCodeSet.addAll(buildingCodeList);
                                    }
                                }
                                if (buildingCodeSet.size() == 1){
                                    for (String aBuildingCodeSet : buildingCodeSet) {
                                        index_from_chdpa = aBuildingCodeSet;
                                    }
                                }
                                else{
                                    for (String street : streetCodeSet)
                                    {
                                        index_from_chdpa = processor.getStreetIndex(street);
                                    }
                                }
                            }
                            else if (streetCodeSet.size() == 0) {
                                if (!stroka.getStreet().isEmpty())
                                {
                                    stroka.setAnaliz_street(OK);
                                }
                                index_from_chdpa = "индекс не найден в ЦХДПА";}
                        }
                        else {
                            List<String> tempList = processor.query("index", "kladr", stroka.getCity());
                            if (tempList != null && !tempList.isEmpty()){
                                for (String string : tempList){
                                    if (index_from_chdpa.isEmpty()) {
                                        index_from_chdpa = string;
                                    }
                                    else{
                                        index_from_chdpa = index_from_chdpa + ", " + string;
                                    }
                                    stroka.setAnaliz_street(OK);
                                }
                            }
                            else {
                                index_from_chdpa = "не определен, улица не существует";

                                if (stroka.getRaion().length() > 0) {
                                    stroka.setAnaliz_street(OK);
                                } else {
                                    stroka.setAnaliz_street("улица распознана некорректно");
                                }
                            }

                        }
                    }
                    else {
                        index_from_chdpa = "не определен, улица не распознана";

                        System.out.println(stroka.getId()+ ") В таблице не заполнена улица");

                        if (stroka.getRaion().length() > 0) {
                            stroka.setAnaliz_street(OK);
                        }
                        else{
                            stroka.setAnaliz_street("несуществующая улица");
                        }
                    }

                }
                else
                {
                    index_from_chdpa = "В таблице адреса нет региона";
                    System.out.println(stroka.getId()+ ") В таблице адреса нет региона.");
                }
                stroka.setIndexChdpa(index_from_chdpa);
                strokaList.set(i, stroka);
                Pattern pattern = Pattern.compile("(?<!\\d)\\d{6}(?!\\d)");
                Matcher matcher = pattern.matcher(original_address);
                if (matcher.find())
                {
                    int count = matcher.groupCount();
                    indexFromEDI = matcher.group(0);
                    if (count == 2) {
                        System.out.println(stroka.getId()+ ") В строке 2 разных индекса: " + stroka.getOriginal_address());
                    }
                    ediWithoutIndex = original_address.replace(indexFromEDI, "");
                    if (indexFromExcel.equals(indexFromEDI) && index_from_chdpa.contains(indexFromExcel))
                    {
                        stroka.setAnaliz_index(OK);
                        System.out.println(stroka.getId()+ ") Полное совпадение: " + indexFromEDI + " = " + indexFromExcel + " = ЦХДПА: " + index_from_chdpa);
                    }
                    else if (!indexFromExcel.equals(indexFromEDI) && index_from_chdpa.contains(indexFromExcel))
                    {
                        System.out.println(stroka.getId()+ ") Несовпадение EDI и восстановленного индекса: " + indexFromEDI + " != " + indexFromExcel + " = ЦХДПА: " + index_from_chdpa);
                        stroka.setAnaliz_index(OK);
                    }
                    else if (indexFromExcel.equals(indexFromEDI) && !index_from_chdpa.contains(indexFromExcel))
                    {
                        System.out.println(stroka.getId()+ ") Cовпадение EDI и восстановленного индекса " + indexFromEDI + " = " + indexFromExcel + " != ЦХДПА: " + index_from_chdpa);
                        stroka.setAnaliz_index(OK);
                    }
                    else
                    {
                        stroka.setAnaliz_index("НЕ ОК");
                        System.out.println(stroka.getId()+ ") Несовпадение: " + indexFromEDI + " != " + indexFromExcel + " /// ЦХДПА: " + index_from_chdpa);
                    }
                }
                else
                {
                    if (index_from_chdpa.contains(indexFromExcel))
                    {
                        System.out.println("Индекс не указан в данных EDI. Excel: = " + indexFromExcel + " ЦХДПА: " + index_from_chdpa);
                        stroka.setAnaliz_index(OK);
                    }
                    else {
                        System.out.println("Индекс не указан в данных EDI. ЦХДПА =/= Excel Строка: " + stroka.getOriginal_address());
                        stroka.setAnaliz_index("НЕ ОК");
                    }
                    ediWithoutIndex = original_address;
                }
                if (!stroka.getBuilding().isEmpty() && ediWithoutIndex.contains(stroka.getBuilding().replaceAll("[^0-9]", ""))){
                    stroka.setAnaliz_building(OK);
                }
                else if (stroka.getBuilding().isEmpty() && ediWithoutIndex.contains(".*\\d+.*")){stroka.setAnaliz_building("не распознан номер дома");}
                else if (stroka.getBuilding().isEmpty() && !ediWithoutIndex.contains(".*\\d+.*")){
                    stroka.setAnaliz_building("EDI не содержит номеров");
                    stroka.setFinalAnaliz("Некорректные данные EDI");
                }
                else if (!ediWithoutIndex.contains(stroka.getBuilding().replaceAll("[^0-9]", ""))) {stroka.setAnaliz_building("номер дома не содержится в EDI");}
                if (!stroka.getDrob().isEmpty() && ediWithoutIndex.contains(stroka.getDrob())){
                    stroka.setAnaliz_drob(OK);
                }
                if (!stroka.getKorpus().isEmpty() && ediWithoutIndex.contains(stroka.getKorpus())){
                    stroka.setAnaliz_korpus(OK);
                }
                if (!stroka.getStroenie().isEmpty() && ediWithoutIndex.contains(stroka.getStroenie())){
                    stroka.setAnaliz_stroenie(OK);
                }
                if (!stroka.getKvartira().isEmpty() && ediWithoutIndex.contains(stroka.getKvartira())){
                    stroka.setAnaliz_kvartira(OK);
                }
                if (stroka.getAnaliz_index().equals(OK) && stroka.getAnaliz_street().equals(OK) && stroka.getAnaliz_building().equals(OK)){
                stroka.setFinalAnaliz(OK);}
                else if (!stroka.getFinalAnaliz().equals("Некорректные данные EDI")) stroka.setFinalAnaliz("НЕ ОК");
            }
            private static List<Stroka> readFromExcel(String file) throws IOException{

                List <Stroka> returnList = new ArrayList<>();
                returnList.add(new Stroka("Исходный id", "анализ индекса", "индекс из ЦХДПА", "анализ улицы", "анализ номера дома", "анализ дроби", "анализ корпуса", "анализ строения", "анализ квартиры", "анализ общий анализ качества"));
                XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(file));
                XSSFSheet myExcelSheet = myExcelBook.getSheet("result");
                for (int i = 1; i < myExcelSheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = myExcelSheet.getRow(i);
                    String id = "";
                    String original_address = "";
                    String index = "";
                    String region = "";
                    String subcity = "";
                    String city = "";
                    String raion = "";
                    String street = "";
                    String building = "";
                    String drob = "";
                    String korpus = "";
                    String stroenie = "";
                    String kvartira = "";

                    if (row.getCell(0) != null && row.getCell(0).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        id = row.getCell(0).getStringCellValue();
                    }
                    if (row.getCell(1) != null && row.getCell(1).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        original_address = row.getCell(1).getStringCellValue();
                    }
                    if (row.getCell(3) != null && row.getCell(3).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        index = row.getCell(3).getStringCellValue();
                    }
                    if (row.getCell(4) != null && row.getCell(4).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        region = row.getCell(4).getStringCellValue();
                    }
                    if (row.getCell(5) != null && row.getCell(5).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        subcity = row.getCell(5).getStringCellValue();
                    }
                    if (row.getCell(6) != null && row.getCell(6).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        city = row.getCell(6).getStringCellValue();
                    }
                    if (row.getCell(7) != null && row.getCell(7).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        raion = row.getCell(7).getStringCellValue();
                    }
                    if (row.getCell(8) != null && row.getCell(8).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        street = row.getCell(8).getStringCellValue();
                    }
                    if (row.getCell(9) != null && row.getCell(9).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        building = row.getCell(9).getStringCellValue();
                    }
                    if (row.getCell(10) != null && row.getCell(10).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        drob = row.getCell(10).getStringCellValue();
                    }
                    if (row.getCell(11) != null && row.getCell(11).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        korpus = row.getCell(11).getStringCellValue();
                    }
                    if (row.getCell(12) != null && row.getCell(12).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        stroenie = row.getCell(12).getStringCellValue();
                    }
                    if (row.getCell(13) != null && row.getCell(13).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        kvartira = row.getCell(13).getStringCellValue();
                    }
                    returnList.add(new Stroka(id, original_address, index, region, raion, city, subcity, street, building, drob, korpus, stroenie, kvartira));
                }
                myExcelBook.close();
                return returnList;
            }
            private static void writeToExcel(String file) throws IOException{

                XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(file));
                XSSFSheet myExcelSheet = myExcelBook.getSheet("Результаты");
                for (int i = 0; i < myExcelSheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = myExcelSheet.getRow(i);
                    if (row.getCell(0).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        if (strokaList.get(i).getId().equals(row.getCell(0).getStringCellValue()))
                        {
                            Cell cell18 = row.getCell(18);
                            if (cell18 == null) {
                                cell18 = row.createCell(18);
                            }
                            cell18.setCellType(Cell.CELL_TYPE_STRING);
                            cell18.setCellValue(strokaList.get(i).getAnaliz_index());

                            Cell cell19 = row.getCell(19);
                            if (cell19 == null) {
                                cell19 = row.createCell(19);
                            }
                            cell19.setCellType(Cell.CELL_TYPE_STRING);
                            cell19.setCellValue(strokaList.get(i).getIndexChdpa());

                            Cell cell20 = row.getCell(20);
                            if (cell20 == null) {
                                cell20 = row.createCell(20);
                            }
                            cell20.setCellType(Cell.CELL_TYPE_STRING);
                            cell20.setCellValue(strokaList.get(i).getAnaliz_street());

                            Cell cell21 = row.getCell(21);
                            if (cell21 == null) {
                                cell21 = row.createCell(21);
                            }
                            cell21.setCellType(Cell.CELL_TYPE_STRING);
                            cell21.setCellValue(strokaList.get(i).getAnaliz_building());

                            Cell cell22 = row.getCell(22);
                            if (cell22 == null) {
                                cell22 = row.createCell(22);
                            }
                            cell22.setCellType(Cell.CELL_TYPE_STRING);
                            cell22.setCellValue(strokaList.get(i).getAnaliz_drob());

                            Cell cell23 = row.getCell(23);
                            if (cell23 == null) {
                                cell23 = row.createCell(23);
                            }
                            cell23.setCellType(Cell.CELL_TYPE_STRING);
                            cell23.setCellValue(strokaList.get(i).getAnaliz_korpus());

                            Cell cell24 = row.getCell(24);
                            if (cell24 == null) {
                                cell24 = row.createCell(24);
                            }
                            cell24.setCellType(Cell.CELL_TYPE_STRING);
                            cell24.setCellValue(strokaList.get(i).getAnaliz_stroenie());

                            Cell cell25 = row.getCell(25);
                            if (cell25 == null) {
                                cell25 = row.createCell(25);
                            }
                            cell25.setCellType(Cell.CELL_TYPE_STRING);
                            cell25.setCellValue(strokaList.get(i).getAnaliz_kvartira());

                            Cell cell26 = row.getCell(26);
                            if (cell26 == null) {
                                cell26 = row.createCell(26);
                            }
                            cell26.setCellType(Cell.CELL_TYPE_STRING);
                            cell26.setCellValue(strokaList.get(i).getFinalAnaliz());
                        }
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                myExcelBook.write(fos);
                myExcelBook.close();
                fos.close();
            }
        }
