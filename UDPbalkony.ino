/*
 *  This sketch sends data via HTTP GET requests to data.sparkfun.com service.
 *
 *  You need to get streamId and privateKey at data.sparkfun.com and paste them
 *  below. Or just customize this script to talk to other HTTP servers.
 *
 */

#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

#define PIN D2
#define STRIPLENGTH 240

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
//   NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(240, PIN, NEO_GRB + NEO_KHZ800);

const char* ssid = "PRISM";
const char* password = "pleaseleaveushereCloseoureyestotheoctopusride1";

const char* host = "UDPbalkony";

WiFiUDP Udp;
unsigned int localUdpPort = 4210;
char incomingPacket[721];
char  replyPacekt[] = "ok";

void setup() {
  Serial.begin(115200);
  delay(10);

  // We start by connecting to a WiFi network

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

  Udp.begin(localUdpPort);
  Serial.printf("Now listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), localUdpPort);

  strip.begin();
  strip.show(); // Initialize all pixels to 'off'
}

uint32_t val = 0;

void loop() {
  int packetSize = Udp.parsePacket();
    if (packetSize)
    {
      // receive incoming UDP packets
      Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
      int len = Udp.read(incomingPacket, sizeof(incomingPacket));
      if (len > 0) 
      {
        incomingPacket[len] = 0;
      }
      for(uint32_t i = 0; i<STRIPLENGTH; i++) {
        val = incomingPacket[i*3];
        val = (val << 8) ^ incomingPacket[i*3+1];
        val = (val << 8) ^ incomingPacket[i*3+2];
        //Serial.printf("UDP packet contents: %d\n", incomingPacket[i]);
        strip.setPixelColor(i, val);

        //Serial.printf("pixel %d color: %d\n", i, val);
      }
      Serial.printf("payload start %d\n", incomingPacket[0]);
      memset(incomingPacket, 0, sizeof(incomingPacket));
      strip.show();
      delay(10);   
      // send back a reply, to the IP address and port we got the packet from
      Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
      Udp.write(replyPacekt);
      Udp.endPacket();
    }
}
