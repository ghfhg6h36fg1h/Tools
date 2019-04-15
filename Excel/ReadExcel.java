package com.liangxin.platform.common.tools.excel;

import com.liangxin.platform.common.entity.KPI.EmployeeBonus;
import com.liangxin.platform.common.entity.KPI.EmployeePerformanceImport;
import com.liangxin.platform.common.entity.KPI.TeamOverfulfillBonus;
import com.liangxin.platform.common.entity.KPI.TeamPerformance;
import com.liangxin.platform.common.tools.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcel() {
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }


    public Workbook getWorkbook(MultipartFile mFile) {
        Workbook mWorkbook = null;
        String fileName = mFile.getOriginalFilename();//获取文件名
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }

            if (isExcel2003) {// 当excel是2003时,创建excel2003
                mWorkbook = new HSSFWorkbook(mFile.getInputStream());
            } else {// 当excel是2007时,创建excel2007
                mWorkbook = new XSSFWorkbook(mFile.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mWorkbook;
    }

    /**
     * 读EXCEL文件，获取班组超产奖金集合
     *
     * @param fileName
     * @return
     */
    public List<TeamOverfulfillBonus> getExcelInfo0(Workbook wb, int sheetnum) {
        List<TeamOverfulfillBonus> teamOverfulfillBonusList = new ArrayList<TeamOverfulfillBonus>();
        try {
            teamOverfulfillBonusList = createExcel0(wb, sheetnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamOverfulfillBonusList;
    }

    /**
     * 读EXCEL文件，获取员工个人绩效集合
     *
     * @param fileName
     * @return
     */
    public List<EmployeePerformanceImport> getExcelInfo1(Workbook wb, int sheetnum) {
        List<EmployeePerformanceImport> employeePerformanceImportList = new ArrayList<EmployeePerformanceImport>();

        try {
            employeePerformanceImportList = createExcel1(wb, sheetnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeePerformanceImportList;
    }

    /**
     * 读EXCEL文件，获取员工个人奖金集合
     *
     * @param fileName
     * @return
     */
    public List<EmployeeBonus> getExcelInfo2(Workbook wb, int sheetnum) {
        List<EmployeeBonus> employeeBonusList = new ArrayList<EmployeeBonus>();
        try {

            employeeBonusList = createExcel2(wb, sheetnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeBonusList;
    }

    /**
     * 读EXCEL文件，获取班组绩效集合
     *
     * @param fileName
     * @return
     */
    public List<TeamPerformance> getExcelInfo3(Workbook wb, int sheetnum) {
        List<TeamPerformance> teamPerformanceList = new ArrayList<TeamPerformance>();
        try {
            teamPerformanceList = createExcel3(wb, sheetnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamPerformanceList;
    }

    /**
     * 根据excel里面的内容读取班组超产奖金信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<TeamOverfulfillBonus> createExcel0(Workbook wb, int sheetnum) {
        List<TeamOverfulfillBonus> teamOverfulfillBonusList = new ArrayList<TeamOverfulfillBonus>();
        try {
            teamOverfulfillBonusList = readExcelValue0(wb, sheetnum);// 读取Excel里面班组超产奖金的信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamOverfulfillBonusList;
    }

    /**
     * 根据excel里面的内容读取员工个人绩效信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<EmployeePerformanceImport> createExcel1(Workbook wb, int sheetnum) {
        List<EmployeePerformanceImport> employeePerformanceImportList = new ArrayList<EmployeePerformanceImport>();
        try {

            employeePerformanceImportList = readExcelValue1(wb, sheetnum);// 读取Excel里面员工个人绩效的信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeePerformanceImportList;
    }

    /**
     * 根据excel里面的内容读取员工个人奖金信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<EmployeeBonus> createExcel2(Workbook wb, int sheetnum) {
        List<EmployeeBonus> employeeBonusList = new ArrayList<EmployeeBonus>();
        try {
            employeeBonusList = readExcelValue2(wb, sheetnum);// 读取Excel里面员工个人奖金的信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeBonusList;
    }

    /**
     * 根据excel里面的内容读取班组绩效信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<TeamPerformance> createExcel3(Workbook wb, int sheetnum) {
        List<TeamPerformance> teamPerformanceList = new ArrayList<TeamPerformance>();
        try {
            teamPerformanceList = readExcelValue3(wb, sheetnum);// 读取Excel里面班组绩效的信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamPerformanceList;
    }

    /**
     * 读取Excel里面班组超产奖金的信息
     *
     * @param wb
     * @return
     */
    public List<TeamOverfulfillBonus> readExcelValue0(Workbook wb, int sheetnum) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(sheetnum);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<TeamOverfulfillBonus> teamOverfulfillBonusList = new ArrayList<TeamOverfulfillBonus>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            TeamOverfulfillBonus teamOverfulfillBonus = new TeamOverfulfillBonus();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            teamOverfulfillBonus.setPrdDate(sdf.format(cell.getDateCellValue()));
                        } else {
                            teamOverfulfillBonus.setPrdDate(cell.getStringCellValue());
                        }
                    } else if (c == 1) {
                        teamOverfulfillBonus.setLineType(cellValue(cell));
                    } else if (c == 2) {
                        teamOverfulfillBonus.setLineName(cellValue(cell));
                    } else if (c == 3) {
                        teamOverfulfillBonus.setTeamLeader(cellValue(cell));
                    } else if (c == 4) {
                        teamOverfulfillBonus.setPrdType(cellValue(cell));
                    } else if (c == 5) {
                        teamOverfulfillBonus.setDayProductionNumber(cellValue(cell));
                    } else if (c == 6) {
                        teamOverfulfillBonus.setTimeOfDay(cellValue(cell));
                    } else if (c == 7) {
                        teamOverfulfillBonus.setCapacityPerHour(cellValue(cell));
                    } else if (c == 8) {
                        teamOverfulfillBonus.setDayOverfulfillBonus(cellValue(cell));
                    } else if (c == 9) {
                        teamOverfulfillBonus.setMonthlyCumulativeOutput(cellValue(cell));
                    } else if (c == 10) {
                        teamOverfulfillBonus.setMonthlyCumulativeTime(cellValue(cell));
                    } else if (c == 11) {
                        teamOverfulfillBonus.setCumHourlyCapacity(cellValue(cell));
                    } else if (c == 12) {
                        teamOverfulfillBonus.setCumOverfulfillBonus(cellValue(cell));
                    } else if (c == 13) {
                        teamOverfulfillBonus.setCapacityPerHourStandard(cellValue(cell));
                    } else if (c == 14) {
                        teamOverfulfillBonus.setCapacityPerHourGoal(cellValue(cell));
                    } else if (c == 15) {
                        teamOverfulfillBonus.setChallengeValue(cellValue(cell));
                    } else if (c == 16) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            DecimalFormat df = new DecimalFormat("0.00%");
                            teamOverfulfillBonus.setMonthEfficiency(df.format(cell.getNumericCellValue()));
                        } else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
                            DecimalFormat df = new DecimalFormat("0.00%");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String temp = cell.getStringCellValue();
                            if (StringUtils.isNotEmpty(temp)) {
                                teamOverfulfillBonus.setMonthEfficiency(String.valueOf(df.format(Double.parseDouble(temp))));
                            }
                        } else {
                            teamOverfulfillBonus.setMonthEfficiency(cell.getStringCellValue());
                        }
                    }
                }
            }
            // 添加到list
            teamOverfulfillBonusList.add(teamOverfulfillBonus);
        }
        return teamOverfulfillBonusList;
    }

    private String cellValue(Cell cell) {
        String cellValue = "";
        String temp;
        DecimalFormat df = new DecimalFormat("###################.##");
        if (null != cell) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    cellValue = String.valueOf(df.format(cell.getNumericCellValue()));
                    break;

                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    cellValue = cell.getStringCellValue();
                    break;

                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;

                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    temp = cell.getStringCellValue();
                    if (StringUtils.isNotEmpty(temp)) {
                        cellValue = String.valueOf(df.format(Double.parseDouble(temp)));
                    }
                    break;

                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    cellValue = "";
                    break;

                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    cellValue = "非法字符";
                    break;

                default:
                    cellValue = "未知类型";
                    break;
            }

        }
        return cellValue;
    }

    /**
     * 读取Excel里面员工个人绩效的信息
     *
     * @param wb
     * @return
     */
    public List<EmployeePerformanceImport> readExcelValue1(Workbook wb, int sheetnum) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(sheetnum);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<EmployeePerformanceImport> employeePerformanceImportList = new ArrayList<EmployeePerformanceImport>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            EmployeePerformanceImport employeePerformanceImport = new EmployeePerformanceImport();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            employeePerformanceImport.setPrdDate(sdf.format(cell.getDateCellValue()));
                        } else {
                            employeePerformanceImport.setPrdDate(cell.getStringCellValue());
                        }
                    } else if (c == 1) {
                        employeePerformanceImport.setWorkShop(cellValue(cell));
                    } else if (c == 3) {
                        employeePerformanceImport.setEmployeeNo(cellValue(cell));
                    } else if (c == 4) {
                        employeePerformanceImport.setName(cellValue(cell));
                    } else if (c == 5) {
                        employeePerformanceImport.setQualityViolation(cellValue(cell));
                    } else if (c == 6) {
                        employeePerformanceImport.setRanking1(cellValue(cell));
                    } else if (c == 7) {
                        employeePerformanceImport.setCrossCheckViolations(cellValue(cell));
                    } else if (c == 8) {
                        employeePerformanceImport.setRanking2(cellValue(cell));
                    } else if (c == 9) {
                        employeePerformanceImport.setAttendanceViolation(cellValue(cell));
                    } else if (c == 10) {
                        employeePerformanceImport.setRanking3(cellValue(cell));
                    } else if (c == 11) {
                        employeePerformanceImport.setCasualLeave(cellValue(cell));
                    } else if (c == 12) {
                        employeePerformanceImport.setRanking4(cellValue(cell));
                    } else if (c == 13) {
                        employeePerformanceImport.setWorkshopDiscipline(cellValue(cell));
                    } else if (c == 14) {
                        employeePerformanceImport.setRanking5(cellValue(cell));
                    } else if (c == 15) {
                        employeePerformanceImport.setPpeWearStandard(cellValue(cell));
                    } else if (c == 16) {
                        employeePerformanceImport.setRanking6(cellValue(cell));
                    } else if (c == 17) {
                        employeePerformanceImport.setTrafficSafety(cellValue(cell));
                    } else if (c == 18) {
                        employeePerformanceImport.setRanking7(cellValue(cell));
                    } else if (c == 19) {
                        employeePerformanceImport.setRedCardPosting(cellValue(cell));
                    } else if (c == 20) {
                        employeePerformanceImport.setRanking8(cellValue(cell));
                    } else if (c == 21) {
                        employeePerformanceImport.setTypicalEvents(cellValue(cell));
                    } else if (c == 22) {
                        employeePerformanceImport.setRanking9(cellValue(cell));
                    } else if (c == 23) {
                        employeePerformanceImport.setRank(cellValue(cell));
                    }
                }
            }
            // 添加到list
            employeePerformanceImportList.add(employeePerformanceImport);
        }
        return employeePerformanceImportList;
    }

    /**
     * 读取Excel里面员工个人奖金的信息
     *
     * @param wb
     * @return
     */
    public List<EmployeeBonus> readExcelValue2(Workbook wb, int sheetnum) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(sheetnum);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<EmployeeBonus> employeeBonusList = new ArrayList<EmployeeBonus>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            EmployeeBonus employeeBonus = new EmployeeBonus();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            employeeBonus.setPrdDate(sdf.format(cell.getDateCellValue()));
                        } else {
                            employeeBonus.setPrdDate(cell.getStringCellValue());
                        }
                    } else if (c == 1) {
                        employeeBonus.setWorkShop(cellValue(cell));
                    } else if (c == 2) {
                        employeeBonus.setLineName(cellValue(cell));
                    } else if (c == 3) {
                        employeeBonus.setEmployeeNo(cellValue(cell));
                    } else if (c == 4) {
                        employeeBonus.setName(cellValue(cell));
                    } else if (c == 5) {
                        employeeBonus.setRank(cellValue(cell));
                    } else if (c == 6) {
                        employeeBonus.setTeamOverfulfillBonus(cellValue(cell));
                    } else if (c == 7) {
                        employeeBonus.setWastage(cellValue(cell));
                    } else if (c == 8) {
                        employeeBonus.setDeductBonus(cellValue(cell));
                    } else if (c == 9) {
                        employeeBonus.setTeamPerformanceBonus(cellValue(cell));
                    } else if (c == 10) {
                        employeeBonus.setBonusFactor(cellValue(cell));
                    } else if (c == 11) {
                        employeeBonus.setPerBonus(cellValue(cell));
                    }
                }
            }
            // 添加到list
            employeeBonusList.add(employeeBonus);
        }
        return employeeBonusList;
    }

    /**
     * 读取Excel里面班组绩效的信息
     *
     * @param wb
     * @return
     */
    public List<TeamPerformance> readExcelValue3(Workbook wb, int sheetnum) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(sheetnum);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<TeamPerformance> teamPerformanceList = new ArrayList<TeamPerformance>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            TeamPerformance teamPerformance = new TeamPerformance();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 2) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            teamPerformance.setPrdDate(sdf.format(cell.getDateCellValue()));
                        } else {
                            teamPerformance.setPrdDate(cell.getStringCellValue());
                        }
                    } else if (c == 3) {
                        teamPerformance.setWorkShop(cellValue(cell));
                    } else if (c == 4) {
                        teamPerformance.setLineName(cellValue(cell));
                    } else if (c == 5) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            DecimalFormat df = new DecimalFormat("0.00%");
                            teamPerformance.setEfficiencyPercent(df.format(cell.getNumericCellValue()));
                        } else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
                            DecimalFormat df = new DecimalFormat("0.00%");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String temp = cell.getStringCellValue();
                            if (StringUtils.isNotEmpty(temp)) {
                                teamPerformance.setEfficiencyPercent(String.valueOf(df.format(Double.parseDouble(temp))));
                            }
                        } else {
                            teamPerformance.setEfficiencyPercent(cell.getStringCellValue());
                        }
                    } else if (c == 6) {
                        teamPerformance.setRanking1(cellValue(cell));
                    } else if (c == 7) {
                        teamPerformance.setQualityAssessment(cellValue(cell));
                    } else if (c == 8) {
                        teamPerformance.setRanking2(cellValue(cell));
                    } else if (c == 9) {
                        teamPerformance.setRedLight360(cellValue(cell));
                    } else if (c == 10) {
                        teamPerformance.setRanking3(cellValue(cell));
                    } else if (c == 11) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            DecimalFormat df = new DecimalFormat("0.00%");
                            teamPerformance.setOrderDeliveryInTime(df.format(cell.getNumericCellValue()));
                        } else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
                            DecimalFormat df = new DecimalFormat("0.00%");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String temp = cell.getStringCellValue();
                            if (StringUtils.isNotEmpty(temp)) {
                                teamPerformance.setOrderDeliveryInTime(String.valueOf(df.format(Double.parseDouble(temp))));
                            }
                        } else {
                            teamPerformance.setOrderDeliveryInTime(cell.getStringCellValue());
                        }
                    } else if (c == 12) {
                        teamPerformance.setRanking4(cellValue(cell));
                    } else if (c == 13) {
                        teamPerformance.setEmpTurnoverRate(cellValue(cell));
                    } else if (c == 14) {
                        teamPerformance.setRanking5(cellValue(cell));
                    } else if (c == 15) {
                        teamPerformance.setProcessInspection(cellValue(cell));
                    } else if (c == 16) {
                        teamPerformance.setRanking6(cellValue(cell));
                    } else if (c == 17) {
                        teamPerformance.setTypicalEvents(cellValue(cell));
                    } else if (c == 18) {
                        teamPerformance.setRanking7(cellValue(cell));
                    } else if (c == 19) {
                        teamPerformance.setRank(cellValue(cell));
                    }
                }
            }
            // 添加到list
            teamPerformanceList.add(teamPerformance);
        }
        return teamPerformanceList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

}
