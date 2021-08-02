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
1. [Creating the DBLEU-Object](#creating-the-dbleu-object)
2. [Listening to events](#listening-to-events)
3. [API Key and ID](#api-key-and-id)
4. [License](#license)

## Creating the DBLEU Object
To use the library, you must first create the `DblEu.Builder()` object.
The API requires an API Key and an id
> See [API Key and ID](#api-key-and-id)

**Example**
````java
DblEu api = new DblEu.Builder()
        .setAPIKey("key") //Setting the API Keys
        .setId("id") //Setting the client id
        .build(); //Build the object
````

## Listening to events
You can catch events with their interfaces. Implement the interface for the event you want to catch and override the `onEvent()` method.

**Example**
````java
public class DblEuEvents implements ReadyEvent { //Implement the ReadyEvent interface
    
    //Override the onEvent method of readyevent
    @Override
    public void onEvent(ReadyEventInstance event) {
        event.printInfo(); //Printing ready information
        System.out.println("Version of Library: " + event.getDblEu().version()); //Printing the Library-Version
    }
    
}
````

You can also use two event listeners. Just set a `,` behind the first interface and write the interface name of another event interface

**Example**
````java
public class DblEuEvents implements ReadyEvent, VoteEvent { //Implement the ReadyEvent interface and VoteEvent Interface

    //Override the onEvent method of readyevent
    @Override
    public void onEvent(ReadyEventInstance event) {
        event.printInfo(); //Printing ready information
        System.out.println("Version of Library: " + event.getDblEu().version()); //Printing the Library-Version
    }

    //Override the onEvent method of voteevent
    @Override
    public void onEvent(VoteEventInstance event) {
        System.out.println("Received a vote from " + event.getVote().getVoter().getName() + " (" + event.getVote().getVoter().getId() + ")"); //Printing information
    }

}
````

**Note**<br>
You need to register all of these events when before you create the `DblEu.Builder()` object.

**Example**
````java
DblEu api = new DblEu.Builder()
        .setAPIKey("key") //Setting the API Keys
        .setId("id") //Setting the client id
        .addReadyEvent(new DblEuEvents()) //Add the readyevent listener
        .addVoteEvent(new DblEuEvents()) //Add the voteevent listener
        .build(); //Build the object
````
> See [Creating the DBLEU-Object](#creating-the-dbleu-object)

## API Key and ID
The API Key and the ID are required parameter of the library to tell the API who you are.

### API Key
Follow these steps to get your bots API Key:
1. Go to [discord-botlist.eu](https://discord-botlist.eu) and login with you discord account.
2. Refresh the page. When it's your discord name and avatar in the top right, press it
3. Choose your bot and click the "Show" buttom at the bottom of its card
4. Click on the "Edit" button under the "Developer"-Tag
5. Scroll down until you can see the "API-Token" headline. Copy the text of the textline below

### ID
1. Go to the [discord developer portal](https://discord.com/developers) and go to the setting of your bot. 
2. Copy the text under the "APPLICATION ID" headline or click the "Copy" button

## License
This Library is licensed under the [GNU General Public License v3.0](https://github.com/MauricePascal/DblEu-java/blob/main/LICENSE)
