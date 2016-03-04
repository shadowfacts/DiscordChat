# DiscordChat

Discord chat is a simple server-side mod that connects Minecraft communication with a Discord server.

## Config

```cfg
settings {
    B:enabled=true

    # Required settings
    S:email=user@gmail.com
    S:password=userspassword
    S:serverId=000000000000000000
    S:channels=general 
    
    # Optional settings
    B:sendPlayerDeathMessages=true
    B:sendPlayerAchievementMessages=true
    B:sendPlayerJoinLeaveMessages=true

    # Inbound message formatting
    ## $1 is discord channel, $2 is discord username and $3 is the message
    S:discordToMCFormat=$1 \u00BB <$2> $3

    # Outbound message formatting
    ## $1 is minecraft username and $2 is message
    S:mcToDiscordFormat=MC \u00BB <$1> $2
    S:deathMessageFormat=MC \u00BB $2
    S:achievementMessageFormat=MC \u00BB $1 has just earned the achievement $2
    S:playerJoinMessageFormat=MC \u00BB $1 joined the game
    S:playerLeaveMessageFormat=MC \u00BB $1 left the game
}
```

An example configuration file is seen above. You can copy the example into `config/shadowfacts/DiscordChat.cfg` on your server and then alter the following required values:

  - email: The email of the user you wish to use for connecting to Discord (you can use your own account, normal Discord activity is not affected)
  - password: The password belonging to the account you wish to use for connecting (you can use your own account, normal Discord activity is not affected)
  - channels: A return separated list of channels you want to be relayed to MC and MC chat should be relayed to
  - serverId: The ID number of the Discord server

You can find your Discord server ID in Discord under `Server Settings -> Widget -> Server ID`.
