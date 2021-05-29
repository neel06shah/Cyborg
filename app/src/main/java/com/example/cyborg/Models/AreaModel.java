package com.example.cyborg.Models;

public class AreaModel {
    private  String AreaName;
    private int AreaBills;
    private double AreaAmount;

    public AreaModel(String areaName, int areaBills, double areaAmount) {
        AreaName = areaName;
        AreaBills = areaBills;
        AreaAmount = areaAmount;
    }

    public String getAreaName() {
        return AreaName;
    }

    public int getAreaBills() {
        return AreaBills;
    }

    public double getAreaAmount() {
        return AreaAmount;
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            AreaModel area = (AreaModel) object;
            if (this.AreaName.equals(area.getAreaName())) {
                result = true;
            }
        }
        return result;
    }
}
