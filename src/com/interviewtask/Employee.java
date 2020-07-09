package com.interviewtask;

class Employee {
    private int employeeId;
    private int projectId;
    private String dateFrom;
    private String dateTo;

    Employee() {
    }

    int getEmployeeId() {
        return employeeId;
    }

    void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    int getProjectId() {
        return projectId;
    }

    void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    String getDateFrom() {
        return dateFrom;
    }

    void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    String getDateTo() {
        return dateTo;
    }

    void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return String.format("Employee's ID: %d\nProject's ID: %d\nDate From: %s\nDate To: %s\n",
                this.getEmployeeId(),
                this.getProjectId(),
                this.getDateFrom(),
                this.getDateTo());
    }
}
