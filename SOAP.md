Send text to SOAP api listening on port 12346
(sniffed from iOS app using "nc -l 12346")
=============================================

POST /ipc/control/X_IPcontrol HTTP/1.0
Content-Type: text/xml; charset="utf-8"
HOST: 172.16.10.220
Content-Length: 338
SOAPACTION: "urn:schemas-sharp-co-jp:service:X_IPcontrol:1#X_SetInputpadText"
Connection: close

<?xml version="1.0" encoding="utf-8"?>
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
<s:Body>
<u:X_SetInputpadText xmlns:u="urn:schemas-sharp-co-jp:service:X_IPcontrol:1">
<Text>INSERT_TEXT_HERE</Text>
<ID></ID>
<Pass></Pass>
</u:X_SetInputpadText>
</s:Body>
</s:Envelope>

*********************************************
Response
*********************************************
<?xml version="1.0" standalone="no"?>
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/ s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
    <s:Body>
        <u:X_SetInputpadTextResponse xmlns:u="urn:schemas-sharp-co-jp:service:X_IPcontrol:1>
            <Result>OK</Result>
        </u:X_SetInputpadTextResponse>
    </s:Body>
</s:Envelope>

=============================================
Query TV Status
(Reverse engineered from http://aquos:12346/ipc/ipcontrol/X_IPcontrol.xml)
=============================================
POST /ipc/control/X_IPcontrol HTTP/1.1
Host: localhost:12346
Content-Type: text/xml; charset="utf-8"
Connection: keep-alive
SOAPACTION: "urn:schemas-sharp-co-jp:service:X_IPcontrol:1#X_GetTvStatus"
Accept: */*
User-Agent: CocoaRestClient/15 CFNetwork/760.2.6 Darwin/15.2.0 (x86_64)
Accept-Language: en-us
Accept-Encoding: gzip, deflate
Content-Length: 335

<?xml version="1.0" encoding="utf-8"?>
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
<s:Body>
<u:X_GetTvStatus xmlns:u="urn:schemas-sharp-co-jp:service:X_IPcontrol:1">
<InfoName></InfoName>
<ID></ID>
<Pass></Pass>
</u:X_GetTvStatus>
</s:Body>
</s:Envelope>

*********************************************
Response
*********************************************
<?xml version="1.0" standalone="no"?>
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/ s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
    <s:Body>
        <u:X_GetTvStatusResponse xmlns:u="urn:schemas-sharp-co-jp:service:X_IPcontrol:1>
            <Result>ERR</Result>
            <PowerStatus>0:ON</PowerStatus>
            <AppStatus>0:</AppStatus>
            <InputpadStatus>0:OFF</InputpadStatus>
            <UpdateInfo>0:</UpdateInfo>
        </u:X_GetTvStatusResponse>
    </s:Body>
</s:Envelope>