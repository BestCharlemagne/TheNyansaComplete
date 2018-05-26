package com.candeapps.thenyansacomplete.devicesearch;


/**
 * Created by mchan on 12/03/17.
 */
public class DeviceSearchAsyncTask extends com.candeapps.thenyansacomplete.utils.JsonRequestAsyncTask {

    @Override
    protected String buildApJson(String item) {

        String builder = ("{\"query\": \"{ accessPointList("+
                "search" + ":\\\"" + item + "\\\"" +
                ") { accessPoints {uuid, apGroup, apLocation, apModel, apName, apRadios { adminStatus, numDevices, radioChannel, radioHtMode, radioMode, radioNoiseFloor, radioPhyType, rfBand, rfChannelWidth, rfProtocol, txPowerLevel, associatedClientList{isActive, uuid} }, ipAddress, controllerModel, controllerVersion, controllerSerialNum, numDevices } } } \",\"variables\":null,\"operationName\":null}");
        return (builder);
    }

    @Override
    protected String buildClientJson(String item) {
        String builder2 = ("{\"query\": \"{ deviceList("
                + "search" + ":\\\"" + item + "\\\"" +
                ") { clients{apGroup, apMacAddr, browser, bssid, chWidth, class, controllerIp, createdAt, dnsHostname, essid, ipAddress, is5ghzCapable, isActive, isDfsCapable, isOnDualBandAp, isWireless, lastUpdated, macAddress, model, network, os, osAndVersion, osVersion, protocol, radioChannel, radioTechType, rfBand, source, userAgent, userName, uuid, voyanceUrl} } }\",\"variables\":null,\"operationName\":null}");
        return builder2;
    }
}