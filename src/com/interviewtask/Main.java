package com.interviewtask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        List<Employee> employees = readCSV(args[0]);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Employee firstOfTeam = new Employee();
        Employee secondOfTeam = new Employee();
        long maxDays = 0;
        Date start;
        Date end;

        for (int first = 0; first < employees.size(); first++) {
            for (int second = first; second < employees.size(); second++) {
                if ((employees.get(first).getEmployeeId() == employees.get(second).getEmployeeId()) ||
                        (employees.get(first).getProjectId() != employees.get(second).getProjectId())) {
                    continue;
                }

                Date dateFrom1 = format.parse(employees.get(first).getDateFrom());
                Date dateTo1 = format.parse(employees.get(first).getDateTo());
                Date dateFrom2 = format.parse(employees.get(second).getDateFrom());
                Date dateTo2 = format.parse(employees.get(second).getDateTo());

                //Get latest of both Start Dates
                if (dateFrom1.compareTo(dateFrom2) > 0) {
                    start = dateFrom1;
                } else {
                    start = dateFrom2;
                }
                //Get earliest of both End Dates
                if (dateTo1.compareTo(dateTo2) < 0) {
                    end = dateTo1;
                } else {
                    end = dateTo2;
                }

                if (start.compareTo(end) < 0) {
                    LocalDate date1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate date2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                    if (daysBetween > maxDays) {
                        maxDays = daysBetween;
                        firstOfTeam = employees.get(first);
                        secondOfTeam = employees.get(second);
                    }
                }
            }
        }
        System.out.println("Days worked together: " + maxDays +
                " \nEmployee1's ID: " + firstOfTeam.getEmployeeId() +
                " \nEmployee2's ID: " + secondOfTeam.getEmployeeId() + "\n");
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

            if (data[2].equals("NULL")) {
                employee.setDateFrom(LocalDate.now().toString());
            } else {
                employee.setDateFrom(data[2]);
            }

            if (data[3].equals("NULL")) {
                employee.setDateTo(LocalDate.now().toString());
            } else {
                employee.setDateTo(data[3]);
            }
            employees.add(employee);
        }

        bufferedReader.close();
        return employees;
    }
}
