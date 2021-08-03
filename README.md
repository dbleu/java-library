### ⚠️ Pull requests will all get rejected at the moment!

[discord-invite]: https://discord.gg/2Pang7t9vr
[license]: https://github.com/MauricePascal/DblEu-java/blob/main/LICENSE
[faq]: https://github.com/DV8FromTheWorld/JDA/wiki/10\)-FAQ
[troubleshooting]: https://github.com/DV8FromTheWorld/JDA/wiki/19\)-Troubleshooting
[discord-shield]: https://discord.com/api/guilds/645357850893221918/widget.png
[faq-shield]: https://img.shields.io/badge/Wiki-FAQ-blue.svg
[troubleshooting-shield]: https://img.shields.io/badge/Wiki-Troubleshooting-red.svg
[license-shield]: https://img.shields.io/badge/License-GPL3.0-green.svg
[ ![license-shield][] ][license]
[ ![discord-shield][] ][discord-invite]
<!-- [ ![faq-shield] ][faq] -->
<!-- [ ![troubleshooting-shield] ][troubleshooting] -->

<img align="right" src="https://cdn.discord-botlist.eu/pictures/logo.png" height="200" width="200">

# DblEu-java
DblEu-java is the official Java library of discord-botlist.eu

## Table of contents
1. [Creating the DblEu-Object](#creating-the-dbleu-object)
2. [Listening to events](#listening-to-events)
3. [Post data](#post-data)
3. [Fetching votes](#fetching-votes)
4. [Working with RatelimitManagers](#working-with-ratelimitmanagers)
5. [Simulate Events](#simulate-events)
6. [Catch votes with webhooks](#catch-votes-with-webhooks)
5. [API Key and ID](#api-key-and-id)
6. [License](#license)

## Creating the DblEu Object
To use the library, you must create the `DblEu.Builder()` object first.
The API requires an API Key and an id
> See [API Key and ID](#api-key-and-id)

**Example**
````java
DblEu api = new DblEu.Builder()
        .setAPIKey("key") //Set the API Keys
        .setId("id") //Set the client id
        .build(); //Build the object
````

## Listening to events
You can catch events to work even better with the API

**Example**
````java
public class DblEuEvents extends DblEuListerns { //Extend all event listeners from the DblEuListerns class
    
    //Ready event
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Version of Library: " + event.getDblEu().version()); //Printing the Library-Version
    }
    
}
````

You can catch multiple events in one class

**Example**
````java
public class DblEuEvents extends DblEuListerns { //Extend all event listeners from the DblEuListerns class

    //Ready event
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Version of Library: " + event.getDblEu().version()); //Printing the Library-Version
    }

    //Vote event
    @Override
    public void onVote(VoteEvent event) {
        System.out.println("Received a vote from " + event.getVote().getVoter().getName() + " (" + event.getVote().getVoter().getId() + ")"); //Printing information
    }

}
````

**Note**<br>
You need to register all of these listeners before you build the `DblEu` object.

**Example**
````java
DblEu api = new DblEu.Builder()
        .setAPIKey("key") //Set the API Keys
        .setId("id") //Set the client id
        .addEventListener(new DblEuEvents()) //Add the readyevent listener
        .addEventListener(new DblEuEvents()) //Add the voteevent listener
        .build(); //Build the object
````
> See [Creating the DblEu-Object](#creating-the-dbleu-object)

## Post data
You can post data which will be shown on your bots page.
```java
DblEu api = ...;
PostData postData = new DataBuilder()
   .setServers(servers)
   .build();
api.postData(postData).queue((consumer) -> {
   System.out.println("Sent data");
});
```

## Fetching votes
You can also fetch all current votes

**Example**
```java
public static void printVotes(DblEu api) {
   api.fetchVotes().queue((votes) -> {
      System.out.println("All votes:");
      for(Vote v : votes) System.out.println("   - by "+v.getVoter().getName()+" at "+v.getTime());
      if(votes.size() == 0) System.out.println("There are no votes");
   },
   (e) -> e.printStackTrace());
}
```

## Working with RatelimitManagers
You can work with a RatelimtManager to prevent temporary bans

### Catching Exceptions
```java
@Override
public void onReady(ReadyEvent event) {
   int servers = 0;
   DataBuilder dataBuilder = new DataBuilder()
      .setServers(servers);
   try {
      event.getDblEu().postData(dataBuilder.build()).queue((consumer) -> {
         System.out.println("Sent data");
      });
   } catch(RatelimitReachedException ex) {
      System.out.println("Could not send data");
   }
}
```
> See [Listening to events](#listening-to-events), [Post data](#post-data)

### Using the Throwable of the `.queue()`
```java
@Override
public void onReady(ReadyEvent event) {
   int servers = 0;
   PostData postData = new DataBuilder()
      .setServers(servers)
      .build();
   event.getDblEu().postData(postData).queue((consumer) -> {
      System.out.println("Sent data");
   },
   (throwable) -> {
      System.out.println("Could not send data");
   });
}
```
> See [Listening to events](#listening-to-events), [Post data](#post-data)

### Using the manager
```java
@Override
public void onReady(ReadyEvent event) {
   RatelimitManager manager = event.getDblEu().getRatelimitManager();
   if(manager.postData().getAvailableRequests() != 0) event.getDblEu().postData(
      new DataBuilder()
         .setServers(0)
         .build()
      ).queue((consumer) -> {
         System.out.println("Sent data");
      }); else System.out.println("Could not send data");
}
```
> See [Listening to events](#listening-to-events), [Post data](#post-data)

## Simulate Events
You can simulate events to test features etc.

**Example**
```java
api.simulateEvent(ReadyEvent.class, api.getSimulationBuilder().build()).queue((event) -> {
   System.out.println("Version of simulated ready event: "+event.getDblEu().version());
})
```
Check the [Javadoc](https://DiscordBotlistEU.github.io/java-library), examples, wiki or ask on the Discord Server if you don't know which methods you need to use at the builder.
> See [Listening to Events](#listening-to-events)

## Catch votes with webhooks
You need to catch the votes of your bot with webhooks. We show you how you can receive these
### Using integrated webhook webserver
If you want to catch votes with our webhook implementation, you need to follow these steps:
1. Go to [dev.discord-botlist.eu](https://dev.discord-botlist.eu) and login with you discord account.
2. Click on your bot
3. Scroll down to "Webhook-URL" and paste the following: `<ip>:2526/webhook`
   - **Note:** You need to replace `<ip>` with your IP-Address
4. Add `.enableWebhooks(true)` to the builder
   - **Example**
      ```java
         DblEu api = new DblEu.Builder()
           .setAPIKey("key") //Setting the API Keys
           .setId("id") //Setting the client id
           .enableWebhooks(true) //enable webhooks
           .build(); //Build the object
      ```
      > See [Creating the DblEu-Object](#creating-the-dbleu-object)

### Using your own webserver
If you want to use your own webserver, you can simulate a VoteEvent

**Example**
```java
DblEu api = ...;
String body = "...";
api.simulateEvent(VoteEvent.class, api.getSimulationBuilder().setBody(body).build()).queue((event) -> {
   System.out.println("Received a vote from " + event.getVote().getVoter().getName() + " (" + event.getVote().getVoter().getId() + ")"); //Printing information
})
```
> See [Simulate Events](#simulate-events), [Listening to events](#listening-to-events)

## API Key and ID
The API Key and the ID are required parameter of the library to tell the API who you are.

### API Key
Follow these steps to get your bots API Key:
1. Go to [dev.discord-botlist.eu](https://dev.discord-botlist.eu) and login with you discord account.
2. Click on your bot
3. Scroll down to "API-Key" and click the "Copy"-Button

### ID
1. Go to the [discord developer portal](https://discord.com/developers) and go to the setting of your bot. 
2. Copy the text under the "APPLICATION ID" headline or click the "Copy" button

## License
This Library is licensed under the [GNU General Public License v3.0](https://github.com/MauricePascal/DblEu-java/blob/main/LICENSE)
