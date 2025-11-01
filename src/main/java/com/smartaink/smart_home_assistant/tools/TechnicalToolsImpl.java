package com.smartaink.smart_home_assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class TechnicalToolsImpl implements TechnicalTools {

    @Override
    @Tool("Return the current temperature of the thermostat device in Celsius or Fahrenheit when asked by the user.")
    public String getThermostatTemperature() {
        return "21.2°C";
    }

    @Override
    @Tool("Set thermostat temperature to specified value in Celsius")
    public String setThermostatTemperature(String temperature) {
        //
        // this.thermostat.setTemp(temperature);
        return "Thermostat temperature was set to: " + temperature;
    }

    @Override
    @Tool("Set thermostat temperature to optimal energy-saving temperature")
    public String setOptimalTemperature() {
        //some logic of actually doing it
        return "Optimal temperature set: " + getThermostatTemperature();
    }

    @Override
    @Tool("Check current doorlock status locker/unlocked")
    public String getDoorlockStatus() {
        return "Locked";
    }

    @Override
    @Tool("Unlock the doorlock")
    public String unlockDoorlock() {
        return "Unlocked";
    }

    @Override
    @Tool("Lock the doorlock")
    public String lockDoorlock() {
        return "Locked";
    }

    @Override
    @Tool("Change the PIN code for the doorlock")
    public String changeDoorlockPin(String pincode) {
        return "Your PIN Code was changed";
    }

    @Override
    @Tool("run diagnostics for a specified device")
    public String runDiagnostics(String device) {
        if (device == null || device.isBlank()) {
            return "Device name not provided. Please specify 'thermostat' or 'doorlock'.";
        }

        switch (device.toLowerCase()) {
            case "thermostat":
                return """
                Diagnostics report for Smart Thermostat:
                - Temperature sensor: OK
                - Wi-Fi connection: Stable
                - Power supply: Normal
                - Firmware version: 2.1.3 (up to date)
                No issues detected.
                """;

            case "doorlock":
                return """
                Diagnostics report for Smart Doorlock:
                - Lock mechanism: Functional
                - Battery level: 78%
                - Wi-Fi connection: Stable
                - Firmware version: 1.9.0 (update recommended)
                Minor notice: Firmware update available.
                """;

            default:
                return "Unknown device: " + device + ". Supported devices: thermostat, doorlock.";
        }
    }


    @Override
    @Tool("Check the Wi-Fi connection for a specified device")
    public String checkWifiConnection(String device) {
        if (device == null || device.isBlank()) {
            return "Device name not provided. Please specify 'thermostat' or 'doorlock'.";
        }

        switch (device.toLowerCase()) {
            case "thermostat":
                return """
                Wi-Fi connection: OK
                """;

            case "doorlock":
                return """
                - Wi-Fi connection: NOT CONNECTED
                """;

            default:
                return "Unknown device: " + device + ". Supported devices: thermostat, doorlock.";
        }
    }

}
