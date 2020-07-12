package com.interviewtask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * This class reads file from the path added to Program arguments in Main's configurations
 * It returns the pair of Employees that have worked the most days together
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        List<Employee> employees = readCSV(args[0]);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //Map<Set<Employee.Id>, xDays>>
        Map<Set<Integer>, Long> mapPairAndDays = new HashMap<>();
        Date start;
        Date end;

        for (int first = 0; first < employees.size(); first++) {
            for (int second = first; second < employees.size(); second++) {
                if ((employees.get(first).getEmployeeId() == employees.get(second).getEmployeeId()) ||
                        (employees.get(first).getProjectId() != employees.get(second).getProjectId())) {
                    continue;
                }
                // Set <Employee.Id>
                Set<Integer> pair = new HashSet<>();
                pair.add(employees.get(first).getEmployeeId());
                pair.add(employees.get(second).getEmployeeId());

                Date dateFromFirst = format.parse(employees.get(first).getDateFrom());
                Date dateToFirst = format.parse(employees.get(first).getDateTo());
                Date dateFromSecond = format.parse(employees.get(second).getDateFrom());
                Date dateToSecond = format.parse(employees.get(second).getDateTo());

                //Gets the latest of dateFrom of the first employee and dateFrom of the second employee
                start = getDate(dateFromFirst, dateFromSecond, dateFromFirst.compareTo(dateFromSecond) > 0);
                //Gets the earliest of dateTo of the first and dateTo ot the second
                end = getDate(dateToFirst, dateToSecond, dateToFirst.compareTo(dateToSecond) < 0);

                //Check if there is a period between the start and the end date
                fillMapIfPeriodBetween(mapPairAndDays, start, end, pair);
            }
        }

        Set<Integer> teamToPrint = new HashSet<>();
        long maxDaysToPrint = 0;
        for (Map.Entry<Set<Integer>, Long> keyValue : mapPairAndDays.entrySet()) {
            if (keyValue.getValue() > maxDaysToPrint) {
                maxDaysToPrint = keyValue.getValue();
                teamToPrint = keyValue.getKey();
            }
        }

        List<Integer> list = new ArrayList<>(teamToPrint);
        System.out.println("Days worked together: " + maxDaysToPrint +
                " \nEmployee1's ID: " + list.get(0) + " \nEmployee2's ID: " +
                list.get(1) + "\n");
    }

    private static List<Employee> readCSV(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        bufferedReader.readLine();
        String line;
        List<Employee> employees = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(", ");
            Employee employee = new Employee();
            employee.setEmployeeId(Integer.parseInt(data[0]));
            employee.setProjectId(Integer.parseInt(data[1]));
            setDateFrom(data[2], employee);
            setDateTo(data[3], employee);

            employees.add(employee);
        }

        bufferedReader.close();
        return employees;
    }

    private static void setDateFrom(String datum, Employee employee) {
        if (datum.equals("NULL")) {
            employee.setDateFrom(LocalDate.now().toString());
        } else {
            employee.setDateFrom(datum);
        }
    }

    private static void setDateTo(String datum, Employee employee) {
        if (datum.equals("NULL")) {
            employee.setDateTo(LocalDate.now().toString());
        } else {
            employee.setDateTo(datum);
        }
    }

    private static Date getDate(Date dateFirst, Date dateSecond, boolean condition) {
        return condition ? dateFirst : dateSecond;
    }

    private static void fillMapIfPeriodBetween(Map<Set<Integer>, Long> mapPairAndDays, Date start, Date end, Set<Integer> pair) {
        if (start.compareTo(end) < 0) {
            LocalDate date1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate date2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(date1, date2);

            if (mapPairAndDays.containsKey(pair)) {
                mapPairAndDays.put(pair, mapPairAndDays.get(pair) + daysBetween);
            } else {
                mapPairAndDays.put(pair, daysBetween);
            }
        }
    }
}