#include <PubSubClient.h>
#include <Ethernet.h>

//-=-=-=--=-=-=-=-=Storage and Comunication -=-=-=-=-=
EthernetClient ethClient;
PubSubClient client(ethClient);

byte mac[6] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress ip(10, 1, 1, 34);  // IP LOCAL
IPAddress server(23, 95, 106, 176); // IP Server MQTT

const int RECONNECTION_TIMEOUT = 5000; // Timeout to reconnect in milliseconds.
unsigned long lastReconnection = 0;

//-=-=-=-=- Weather -=-=-=-=-=-
const byte WDIR = A0;
const byte WSPEED = 3;
const byte RAIN = 2;

const int WEATHER_TIMEOUT = 2000; // Timeout to process new data.
unsigned long lastWeatherTime = 0;

long lastWindCheck = 0;
volatile long lastWindIRQ = 0;
volatile byte windClicks = 0;

volatile unsigned long rainTime, rainLast, rainDump;

long rainDumpLast = -10;

const byte RESULT_SIZE = 16;

void setup() {
  Serial.begin(9600);
  
  //-=-=-=- Comunication -=-=-=-=-=-
  client.setServer(server, 1883);
  client.setCallback(callback);

  Ethernet.begin(mac, ip);
  delay(1500);

  //-=-=-=- Weather Sensors -=-=-=-=-
  pinMode(WSPEED, INPUT_PULLUP); // input from wind meters windspeed sensor
  pinMode(RAIN, INPUT_PULLUP); // input from wind meters rain gauge sensor
  
  // Turn on interrupts
  attachInterrupt(0, rainIRQ, FALLING);
  attachInterrupt(1, wspeedIRQ, FALLING);
  interrupts();

  Serial.println("Starting...");
  
  delay(3500);
}

void loop() {
  verifyConnection();
  
  client.loop();
  
  processWeather();
  
  delay(100);
}

void processWeather() {
  unsigned long currentTime = millis();
  
  if ((currentTime - lastWeatherTime) > WEATHER_TIMEOUT) {
    String currentTimeStr = String(currentTime) + ":";
    lastWeatherTime = currentTime;

    // Wind direction
    sendWindDir(currentTimeStr);

    // Wind speed
    sendWindSpeed(currentTimeStr);

    // Rain
    sendRain(currentTimeStr);
  }
}

void sendWindDir(String currentTimeStr) {
  int windDirCurrent = getWindDirection(); // [0-360 instantaneous wind direction]
  char result[RESULT_SIZE] = "";
  String str = currentTimeStr + String(windDirCurrent);
  str.toCharArray(result, RESULT_SIZE);
  client.publish("estacao1/winddir", result);
  
  Serial.print("Wind direction = ");
  Serial.println(str);
}

void sendWindSpeed(String currentTimeStr) {
  float windSpeedCurrent = getWindSpeed();

  char result[RESULT_SIZE] = "";

  dtostrf(windSpeedCurrent, 3, 2, result);
  String str = currentTimeStr + result;
  str.toCharArray(result, RESULT_SIZE);
  client.publish("estacao1/windspeed", result);

  Serial.print("Wind speed = ");
  Serial.println(str);
}

void sendRain(String currentTimeStr) {
  unsigned long rainDumpCurrent = rainDump;

  if (rainDumpLast != rainDumpCurrent) {
    rainDumpLast = rainDumpCurrent;
    
    char result[RESULT_SIZE] = "";
    String str = currentTimeStr + String(rainDumpCurrent);
    str.toCharArray(result, RESULT_SIZE);
    client.publish("estacao1/rain", result);
  
    Serial.print("Rain = ");
    Serial.println(str);
  }
}

// Count rain gauge bucket tips as they occur
// Activated by the magnet and reed switch in the rain gauge, attached to input D2
void rainIRQ() {
  rainTime = millis();

  // Ignore switch-bounce glitches less than 10mS after initial edge
  if ((rainTime - rainLast) > 10) {
    rainDump += 1; // Count dumps of water.
    rainLast = rainTime; // Set up for next event
  }
}

// Activated by the magnet in the anemometer (2 ticks per rotation), attached to input D3
void wspeedIRQ() {
  // Ignore switch-bounce glitches less than 10ms (142MPH max reading) after the reed switch closes
  if (millis() - lastWindIRQ > 10) {
    lastWindIRQ = millis(); //Grab the current time
    windClicks++; //There is 1.492MPH for each click per second.
  }
}

int getWindDirection()  {
  unsigned int adc;

  adc = analogRead(WDIR); // get the current reading from the sensor

  // The following table is ADC readings for the wind direction sensor output, sorted from low to high.
  // Each threshold is the midpoint between adjacent headings. The output is degrees for that ADC reading.
  // Note that these are not in compass degree order! See Weather Meters datasheet for more information.

  if (adc < 380) return 113;
  if (adc < 393) return  68;
  if (adc < 414) return  90;
  if (adc < 456) return 158;
  if (adc < 508) return 135;
  if (adc < 551) return 203;
  if (adc < 615) return 180;
  if (adc < 680) return  23;
  if (adc < 746) return  45;
  if (adc < 801) return 248;
  if (adc < 833) return 225;
  if (adc < 878) return 338;
  if (adc < 913) return   0;
  if (adc < 940) return 293;
  if (adc < 967) return 315;
  if (adc < 990) return 270;
  
  return -1; // error, disconnected?
}

// Returns the instataneous wind speed
float getWindSpeed() {
  float deltaTime = millis() - lastWindCheck; //750ms
  deltaTime /= 1000.0; //Covert to seconds
  float windSpeed = (float)windClicks / deltaTime; //3 / 0.750s = 4

  windClicks = 0; //Reset and start watching for new wind
  lastWindCheck = millis();

  windSpeed *= 1.492; //4 * 1.492 = 5.968MPH

  return windSpeed;
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] -> ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  
  if (strstr((char*) payload, "fazalgo") > 0 ) { 
    Serial.println("recebi um comando");
  }
}

void verifyConnection() {
  if (!client.connected()) {
    if ((millis() - lastReconnection) > RECONNECTION_TIMEOUT) {
      reconnect();
      lastReconnection = millis();
    }
  }
}

void reconnect() {
  Serial.print("Attempting MQTT connection...");
  
  // Attempt to connect
  if (client.connect("arduinoClient")) {
    Serial.println("connected");
    // Once connected, publish an announcement...
    //publishDados();
    // ... and resubscribe
    client.subscribe("estacao2/in");
  } else {
    Serial.print("failed, rc=");
    Serial.println(client.state());
  }
}

