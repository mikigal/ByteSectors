# ByteSectors [MC 1.8 - 1.15]
Sectors system for Minecraft servers based on Redis

## Project is no longer supported, please don't DM me for support or fix

Find whatever you want
 - [Requirements](#requirements)
 - [Plugins compatibility](#plugins-compatibility)
 - [How to use](#how-to-use)
 - [Configuration](#configuration)
 - [API](#API)
 - [How to build](#how-to-build)
 - [Commercial usage](#commercial-usage)
 - [I found bug!](#i-found-bug!)
 - [Contribution](#contribution)
 - [TODO](#TODO)
 - [Known bugs](#known-bugs)

## Requirements
 - Few Bukkit (or it's fork like Spigot) servers (can be on the same physical server)
 - BungeeCord (or it's fork like WaterFall) proxy
 - Redis (database used to send packets between sectors and proxy)
 - MySQL (API for synchronization MySQL queries wil be added soon)

## Plugins compatibility
Most plugins need synchronization with every sector, so it won't work properly with ByteSectors without rewrite with sectors support. If you want do some plugin compatible with ByteSectors do it yourself (check [API](#API)), contact it's developer or me.  
My Discord: **mikigal#5517** 

## How to use
Simply download latest release from this repository and put **Client to every Bukkit server** and **System to you BungeeCord**  
After install, you must configure plugins. Check [Configuration](#configuration)
You can check every sectors status with **/sectors** command (bytesectors.sectors permission)

## Configuration
The main part of configuration is on **System** (BungeeCord) plugin. Every Client synchronize it's configuration with System

### System configuration
```yaml
redisHost: "localhost"
redisPort: 6379 
redisPassword: "" 

mysqlHost: "localhost"
mysqlPort: 3306
mysqlUsername: "root"
mysqlPassword: ""
mysqlDatabase: "bytesectors"

nearBorderTerrainModifyBlockDistance: 20 # How near border modifying terrain should be disallowed

joinSectorOfflineMessage: "&cYour sector is offline!" # Kick message for players that's trying to join, but are on offline sector
outOfBorderMessage: "&cYou can't go outside map's border!" # Message when player is trying to go outside map's border
sectorOfflineMessage: "&cThat sector is offline! You can't go there" # Message when player is trying to go to offline sector
nearBorderTerrainModifyMessage: "&cYou can modify terrain near border" # Message when player is trying to modify blocked terrain near border

nearBorderActionBar: "&c&lYou are approaching map border ({DISTANCE}m)" # ActionBar message near border
nearSectorActionBar: "&cYou are approaching sector {ID} ({DISTANCE}m) [{PERFORMANCE}] [{ONLINE} online]" # ActionBar message near another sector

sectors:
  first: # Server name from BungeeCord config
    min_x: -1000 # Min => inclusive
    max_x: 0 # Max => exclusive
    min_z: -1000
    max_z: 1000
    world: "world" # Sector's world
    default: true # Connect to default this sector on first join
  second:
    min_x: 0
    max_x: 1000
    min_z: -1000
    max_z: 1000
    world: "world"
    default: false

sectorsGui: # GUI with sectors' status
  guiName: "&c&lSectors status"
  online:
    material: WOOL
    amount: 1
    durability: 5
    name: "&a&l{ID} [ONLINE]"
    lore:
      - "&7TPS: &9{PERFORMANCE}"
      - "&7Online: &9{ONLINE}"
  offline:
    material: WOOL
    amount: 1
    durability: 14
    name: "&c&l{ID} [OFFLINE]"
    lore:
      - "&7Last online: &9{LAST_ONLINE}"
```

### Client configuration
```yaml
redisHost: "localhost"
redisPort: 6379
redisPassword: ""

sectorId: "first" # Server name from BungeeCord config
```

## API
**API currently is in WIP state, MySQL queries synchronization should be added soon**  

### Get information about sectors
```java
SectorManager.getSector(String id) // Return Sector with selected ID
SectorManager.getSector(int x, int z, String world) // Return sector with region on selected location
SectorManager.getCurrentSector() // Return current Sector
SectorManager.getCurrentSectorId() // Return ID of current Sector
SectorManager.getSectors() // Return Collection with all sectors
```

### Let's create your own packet!
```java
// All you packet's must extends Packet class
public class ExamplePacket extends Packet { 
  
    private String test;

    public ExamplePacket(String test) {
        super(); // Remember to call super() constructor! It sets sender (from Packet class) to current sector
        this.test = test;
    }

    public String getTest() {
        return this.test;
    }
}
```
**You must put your packet in both sides (if BungeeCord receive it). I recommend to use modules scheme like ByteSector (Commons module is dependcy of Client and System)**  

**Remember, that every field in your Packet must primitive type or implements Serializable!**  
If you want send some not serializabje object, you must create own Serializable implementation or use implementation from ByteSectors. I will add more Serializable implementations soon
```java
SerializationUtils.serializeLocation(Location) // Return LocationSerializable object
SerializationUtils.deserializeLocation(LocationSerializable) // Return Bukkit Location

SerializationUtils.serializePotionEffect(PotionEffect) // Serialize PotionEffect
SerializationUtils.deserializePotionEffect(PotionEffectSerializable) //Deserialize PotionEffectSerializable

SerializationUtils.serializePotionEffects(PotionEffect) // Serialize List of PotionEffect
SerializationUtils.deserializePotionEffects(PotionEffectSerializable) // Deserialize array of PotionEffectSerializable

SerializationUtils.serializeItemstacks(ItemStack[]) // Serialize ItemStack[] to Base64 encoded String
SerializationUtils.deserializeItemcks(String) //Deserialize ItemStack[] from Base64
```
If you want to create serialization implementation of some object simply create object with fields that you want to send (remember that it must be primitive or serializable! e.g. instead of Bukkit World object, send it's name)  
After receive packet you can create new instance of Bukkit object from fields sent in serializable implementation.  

Example:  
[**LocationSerializable**](https://github.com/mikigal/ByteSectors/blob/master/Commons/src/main/java/pl/mikigal/bytesectors/commons/serializable/LocationSerializable.java)  
[**SerializationUtils**](https://github.com/mikigal/ByteSectors/blob/master/Client/src/main/java/pl/mikigal/bytesectors/client/util/SerializationUtils.java#L24)
<br/>

### Packet is ready, but how to send it?
There are few channel that you can use:
 - `SectorManager.getSystemChannel()` => should be subscribed **ONLY** by BungeeCord plugins
 - `SectorManager.getClientChannel()` => should be subscribed **ONLY** by **EVERY** sector
 - `SectorManager.getPublicChannel()` => should be subscribed by **EVERY** server (sectors + proxy)
 - Sector ID => Should be subscribed by **only** by sector, with this ID
```java
// Packet should be sent to every client, but not system
new ExamplePacket("my example string").send(SectorManager.getClientChannel()) 
```

You can also send packet, as a response. It's useful for e.g. sending response of some requests
```java
// Packet should be send to channel from which it came
receivedPacket.sendResponse(new ExamplePacket("my example string"))
```
<br/>

### How to receive packets?
```java
// It should listen for ExamplePacket sent to every client's channel
public class ExamplePacketListener extends RedisListener<ExamplePacket> {

    public ExamplePacketListener() {
        super(SectorManager.getClientChannel(), ExamplePacket.class)
    }

    @Override
    public void onMessage(ExamplePacket packet) {
        System.out.println("I've received ExamplePacket with message " + packet.getTest());
    }
}
```

**Remember to register your listener!**
```java
// It should listen for ExamplePacket sent to every client's channel
RedisUtils.subscribe(SectorManager.getClientChannel(), new ExamplePacketListener())
```
<br/>

### Using redis database
You can get/set data to redis DB using few methods. Currenly, there's only methods to save Strings
```java
RedisUtils.set(String key, String value)
RedisUtils.setAsync(String key, String value) // Return RedisFuture<String>

RedisUtils.get(String key)
RedisUtils.getAsync(String key) // Return RedisFuture<String>
```

### Remote MySQL
For sending MySQL queries you should use DatabaseAPI. Every query will be sent to global database, and asynchronously executed by System.

**Sending query without response, like INSERT or UPDATE**
```java
// Values 1, 2, 3 will be placed into "?" placeholder, like on standard PreparedStatement
DatabaseAPI.execute(new DatabaseStatement("INSERT INTO `my_table` VALUES (?, ?, ?)", 1, 2, 3));
```

**Sending query with response, like SELECT**
```java
// Of course you can use replacements here too!
DatabasAPI.query(new DatabaseStatement("SELECT * FROM `my_table` WHERE kills > ?", 10, resultSet -> {
  // This code will be executed, when Client got reponse with ResultSet

  for (Row row : resultSet.getRows()) { // Iterate through every row from ResultSet
      System.out.println(row.getString("username")) // Get data like with standard ResultSet
  }

  // You can also get columns with details
  for (Column column : resultSet.getColumns()) {
      column.getIndex() // Index in table, remember than first column has ID = 1
      column.getName(); // Name of column
      column.getType(); // Data type
      column.isAutoIncrement(); // Is this column AUTO_INCREMENT?
      column.isNullable(); // Can value from this column be NULL?
  }
}, exception -> {
  // This code will be executed, if statement throws exception or Client didn't get query response in selected time (default 5s)
  System.out.println("Invalid query or request timed out");
  exception.printStactTrace();
}));
```

### Bukkit listeners
[**SectorChangeEvent**](https://github.com/mikigal/ByteSectors/blob/master/Client/src/main/java/pl/mikigal/bytesectors/client/event/SectorChangeEvent.java)
```java
public class SectorChangeListener implements Listener {

    @EventHandler
    public void onChange(SectorChangeEvent event) {
        event.getPlayer(); // Return player that's trying to change sector
        event.getCurrentSector(); // Return player's sector
        event.getNewSector(); // Return sector, that player is trying to reach
        event.setCancelled(true); // You can cancel this event. Then player can't change sector
    }
}
```

## How to build 
If you won't use release from GitHub you can build ByteSectors by yourself  
To build **system** and **client** change build path and simply run `build` task with Gradle.  
If you build **client** remember to use jar with **shadow** suffix with relocated netty (Lettuce does not like netty from older MC versions)

## Commercial usage
Commercial usage of this ByteSectors version is permitted.  
If you want use this project in commercial usage contact me, I can offer you better support, instant bug fixes and plugins compatible with ByteSectors.  
Discord: **mikigal#5517**

## I found bug!
If you find any bug in ByteSectors pleas report it by Issues on GitHub [**\*click\***](https://github.com/mikigal/ByteSectors/issues/new)  
Otherwise you can contact with me on Discord: **mikigal#5517**

## Contribution
If you want help in ByteSectors development feel free to create Pull Request! I will mention you here  

## TODO
 - Make switch to last sector after join async
 - Support for newer MC versions

## Known bugs
 - Seems currently everything work!
