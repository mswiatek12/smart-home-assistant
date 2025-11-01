package com.smartaink.smart_home_assistant.tools;

public interface TechnicalTools {

    //thermostat
    String getThermostatTemperature();

    String setThermostatTemperature(String temperature);

    String setOptimalTemperature();


    //doorlock
    String getDoorlockStatus();

    String unlockDoorlock();

    String lockDoorlock();

    String changeDoorlockPin(String pincode);


    //actions for both
    String runDiagnostics(String device);

    String checkWifiConnection(String device);
}
