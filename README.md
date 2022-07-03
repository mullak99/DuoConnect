# DuoConnect
## Info
A simple mod to easily recreate [MC-74984](https://bugs.mojang.com/browse/MC-74984).

## Install
1) Install Fabric Loader (>=0.14.8) and the latest version of Fabric API
2) Install as you would any other Fabric mod

## How to use
1) After launching the game, open the Multiplayer menu
2) Click the "DC" button. Enter the two server addresses into the fields
3) Click "Join Servers"

**Note: The first address is connected to first, followed by the second.**

## Configuration
The mod does have a config file that lets you add a delay between the connections, but it's not working correctly.  

If you can't get the mod working with the delay disabled, navigate to `config/DuoConnect.cfg` and set `waitBetweenConnections` to `true`, 
and optionally configure `waitTimeMs` to any number between `1 - 3000`.
