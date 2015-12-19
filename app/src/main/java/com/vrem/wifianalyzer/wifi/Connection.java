/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.WifiInfo;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Connection {
    private final WifiInfo wifiInfo;

    public Connection(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public boolean isConnected() {
        return wifiInfo != null;
    }

    public String getIpAddress() {
        byte[] bytes = BigInteger.valueOf(wifiInfo.getIpAddress()).toByteArray();
        ArrayUtils.reverse(bytes);
        try {
            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (UnknownHostException e) {
            Log.e("IPAddress", e.getMessage());
        }
        return "";
    }

    public int getFrequency() {
        return wifiInfo.getFrequency();
    }

    public String getSSID() {
        String result = wifiInfo.getSSID();
        if (result.charAt(0) == '"') {
            result = result.substring(1);
        }
        if (result.charAt(result.length()-1) == '"') {
            result = result.substring(0, result.length()-1);
        }
        return result;
    }

    public String getBSSID() {
        return isConnected() ? wifiInfo.getBSSID() : "";
    }

    public int getRssi() {
        return Math.abs(wifiInfo.getRssi());
    }

    public double getDistance()    {
        return Distance.calculate(getFrequency(), getRssi());
    }

    public int getChannel() {
        return Frequency.findChannel(getFrequency());
    }

    public Strength getStrength() {
        return Strength.calculate(-wifiInfo.getRssi());
    }

}