SharpControlWeb by Nicholas Robinson

This project houses a fully functional and re-usable Java library for the Sharp AQUOS "IP Control" Remote Control API. A web server and HTML/javascript user interface are provided as an example of how to leverage this library. This project was created for fun to allow me to control the television from any device with a web browser, but could easily be leveraged to integrate compatible Sharp televisions into a hoe automation system.

To see it in action go here (sockets/cURL are disabled for safety):
http://www.nicholassavilerobinson.com/SharpControlWeb/index.html

Requirements:
- Java

Installation:
- Download/Clone project folder
- ./gradlew run

I also added an example of controlling the driver from the command line:
- ./gradlew run -DmainClassName=com.nicholassavilerobinson.SharpControl.SharpControlCommandLine -Dexec.args="<SERVER> <COMMAND>"
- e.g. ./gradlew run -DmainClassName=com.nicholassavilerobinson.SharpControl.SharpControlCommandLine -Dexec.args="aquos POWER_ON"
- e.g. ./gradlew run -DmainClassName=com.nicholassavilerobinson.SharpControl.SharpControlCommandLine -Dexec.args="aquos VOLUME 10"

The full list of commands is available here:
https://github.com/nicholasrobinson/SharpControlWeb/blob/master/src/main/resources/public_html/commands.json

Please let me know if you find this useful or come up with any novel implementations.

Enjoy!

Nicholas Robinson
me@nicholassavilerobinson.com

P.S. You 'could' host your SharpControlWeb Remote Control Interface application on the public internet and control your TV from anywhere. If you choose to go this route, please consider the security implications of potentially allowing anyone on the internet to control your TV.

References:
http://files.sharpusa.com/Downloads/ForHome/HomeEntertainment/LCDTVs/Manuals/mon_man_LC70LE847U_LC60LE847U_LC70LE745U_LC60LE745U_LC80LE844U.pdf