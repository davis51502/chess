# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5T9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kwECQuQoflwm6MhnaKXqNnGcoKDgcPkdRmqqh406akmqcq6lD6hQ+MCpWHAP2pE0Ss1a1mnK0wG1233+x1g9W5U6Ai5lCJQpFQSKqJVYNPAmWFI6XGDXDp3SblVZPIP++oQADW6GrU32TsoxfgyHM5QATE4nN0K0MxWMYDXHlN66lGy20G3Vgd0BxPN4-IFoOxyTAADIQaJJAJpDJZXt5EvFMvVOpNVoGdQJNAjgaV8e7L4vN4fZedkGFIWZajrcH6fE835LJ+ALnEWspqig5QIIePKwgeR6ouisTYkmhiuuG7pkhShq0u+oymkSEaWhyMDcryhqCsKMCiuKbrSimV4IeUDHaE6LoEgRLLlFQwDIFomRVPorxLLCkHvLMPHyC4ckcDhcp4QJlGETqeqZPGAazqGbEWlGNGxjGwaJup3ZARm6E8rm+aYLZ3alhm-SzvOraTu2fQHG53bZH2MCDsOvR9J5zbeVOflmJw66+P4AReCg6D7oevjMCe6SZJgQWXkU1A3tIACie4lfUJXNC0j6qM+3SRQu-5srZ5SNegzmwV28HOvKMDIfYmWBsGXloGpCEaoJ2ocCg3B6cGw0NlFY1hlp7GFNGkTDBANAWQm2hCmE7VoHxyapl1dkZX6jkIGAsJ5hGAC8FjotiLkcYVJRXH0D3uuMMAvSg-nXiCpz5WAA5Dq+v0sv9gMrvFmBeIlgSQnae7QjAADi46stlZ55RezA9TeWMVdV9jjg1I3Lc151AmWx2dQzAGfeC-XQjjoyqItc7LeNvUaTIU0ejAIliT6C3HRRTLutRXI8nGlkHUxTPGZGPXs-pgqwuLSCqadQvq0RYBc2osIy+akYbTRnIUiAqQwKJCDY+OgrmFAqAaOrrnqeUGOxAAPGbjL5IbNkXeUZsB2AN13ZT3OPTHwdu9o+RvRdvtFe5fQJ2o-0VC4ReNMD2eBUTkNhf0eeqAXRcuCXcVrkjG5JdgPhQNg3DwLphhmykOXnjkxOcV9lS1A0FNU8ENMLq+ecAHJgX+AX0+m33HZ+TyL8vHbvaP7NevqZuwnAvdm5hGIC-xwtrdqeviSg+m86NltUaZCu8trKtHbPHU+x9XC1plbyBgLrUS+tr7Jk0rLISOlvSZBPjvUYswbgAElpBvzlh-GMtoYBoBQMkV2ox3YYCsOHD6rUe4IJQBfNQ+ZYR5wwY9PoABGfsABmPoGcWZZzHtXccGD-psM4bFVeYMK4hShuFJh0hhHsK4QjZuyNNwBEsLNZCRCABSEAeTEMMAEHQCBQBNkJsPNkblyjVEpPeFoedqZLTnuFLuwB1FQDgBAZCUAt5TFkdBOmgFI7lgin-Rc4E5jGLcR4rxPjc6CLkf8Zm6YLF+xgAAK10WgE+OieR0LRFfChMCrbCQgY-Z+0tVqwJMjbT+St9qgNVqEypVs+FaxAcAMBD8oFGxFqUckptxyMPiVg9a7JShYwpPoxiYRZHNPfpreUEzmAhwOuAsSMA84wBQAAD2hKobp+FQHFL8I-KokTKCyXOe4zx0BphMI4NMdBqkXBNwSqorwriezelgMAbAXdCDxESAPAm4MUnZysaVcqlVqrGFeZgIAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
