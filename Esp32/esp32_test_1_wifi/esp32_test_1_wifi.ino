#include <WiFi.h>
 //http://www.windworkshop.cn/?p=1277
const char* ssid     = "hdy";
const char* password = "hdy954310";
void setup()
{
    Serial.begin(115200);
    delay(10);
    Serial.println();
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}
void loop() {
 
}
