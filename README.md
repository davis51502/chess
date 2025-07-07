# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![SequenceDiagram](serverDesign.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEYCjACDAASij2SKoWckgQaIEA7gAWSMGYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlGjAALYofaV9MH0ANNO46snQHBNTs9MoI8BICKvTAL6YwiUwBazsXJTl-YNQw2N7630LqktQK5PTc32b27uffUObE43FgZ2OonKUCiMWCUAAFJForFKJEAI4+NRgACUR2KolOhVk8iUKnU5XsKDAAFUOvDbvcULjiYplGpVISjDpSgAxJCcGC0ygsmA6SwwBmjMSYHTQ4AAa0FHRgyQyqQlHUZMFCco44pQAA9YRoWaT2adwfiVOUhVAWXiRCoLYVjlcYAoELrLPUFegAKIGlTYAiJB0nXJnbLmcoAFicAGZuv0xupgJSJtM-VBvGUNUMpdrPSE9TB5PL0ICzJxTKa2epnUVHShymgfAgEGGCWda2TVKUQHLgrb6R0WcztGb62djKUFBwOErhdpO07uxO632ByFggofGBUvDgHvUmPMD3zdPuXOF7v9-aIauzsDLrmkbDUWo21hn6CG67czcmpSlM5RrN8R77vUEDlmgIHTAcYaUA2UYYOUABMThOEmAxAQ8MCgV80wQakUEwXBayHOgHCmF4vj+AE0DsJSMAADIQNESQBGkGRiChzCWtQbrVHUTStAY6gJGgSaSmMcwvG8HCHP+T4XKC1w4fmDwAt88nLI8+wwJgP5IQJVCQjACDsfy8JsRx6KYrE2KGQ+hhriSG4UlSw4yUyhnnlOhQzjAfICraWjyKK4o+TKcqKvyVChEgC5bhwGgwDI669n+VrNiq3gODA2DJDA0AwK27aYOlLkNsZuY+MMx6GkGIZoJV5wgiZplunGACMhlVcUnWFHxsZOH1PSUDm5T1cR0BIAAXigimGVRK4oAU-l9jAW5yCgt4HsRp6bZyQXAPO2rHuFwDOTlNWqW6tn8pEqhfoZtXZYJAEaXcwH4dMTzEaRFaTICN2fchyDRjAGFODAgGaeMf19ADx5A7BIMGW1VbUZ43h+IEXgoOgrHsb4zBcekmR8Zy-7lBU0h+ixfr1H6zQtOJqiSd0gPQegiFgoUtXlDzMFGfdAuNmZ1oWaTe6HqjvNoE5a2cptnlgPt8uQYryvHZe5QAGa+AKxFXZFF3azBhmyiEcVoAlyALiLxNteleumeZ5UdgNTZ3R1dUNfuTUoMGklte9EYuoNua9f1ktDfAkOoTAscTdm0DTYHqRzYty3Y21LkbZl7LlEYKDcDux5ayROtnsXAVcuU0jl1Shj7fet2R+1L7lLZZPPV+Yv+x9JToZhYPhpGSdgKXMM9JR1a43RgTQguLGwjAADiUochTPFZNPNPR3Tm-M2z9hStzCui-+nJCxbNdW0PL5H025TILE28ptXaO4tVbmsl7OrTWzslZ13cllfWMAjb1SdpdbQ5tQHW1ijAeKiU4GWxdulN29cORdxci2Ns3tJZdhUv7TOxFg6h1DOlCOXUY5jTjrfLuI0U6MLTlNCUWcc5LUxqtH2Ut1r5DVulD+YAv5qHhLrXBJ1uTQP5E7dsMAL5jCuuAwBF4o5vwsrCAAPCopa2hCgqy7vfMRBjVDQheggZgz9fz4OPnDPoFiMw9B6C4Dx+wZjuM8UpaOEMcgz3SjDJxLjrg+JcF4iJ+wF441ovjAI2AfBQGwNweAg5DASJSJTXih96F01qA0c+l8s5oyTAYgAclKPx4MyE9wfmjOY-QDGMlWLoxk+RKwRy0YI8oO1ggGPhP0lAEj7JYmVv-IkuDgFV1AdIiBmjG7QONhg1IZsxQNMVsg22qD7boM2U-bBGUFlTg9tLL2BdO51LUlwyhgYQ4tXDuLEe3VGGXNqVPQJo1xozWPDwvOq1C7COmdtDJgyWlSnHCcvBgU5FnQXAYtResemexQMVCRABJaQE9SGC3Fn0sFUorGDzoT0t0zSpRYozDGeMaEeqY2YZ8qG0NMJNOcZS6Q1LaX0pWoveJ9FLDl0ssVAAUhAfkW8CwBB0AgUA8oD6BNfqPSoVRqSiRaAYq+mCpK9FScAQVUA4AQEslAOYBisU1PDHi8hBz0Bsr1QakAxroDrA8R4745rOWY26SQ6WAArcVDgpTwgDfyAx0IMTjJxauKZ0LSiUg1rM6+6B5kaIbkFGBJt4ERQ2UgmKOy0GO1tUkV2xy014LOblC5AjcXdxub8oO9zqGtVoc8hxn1vlMP8Sw6enaegNuzlABavDeXUSBSI5RVJwUctTZOGFSzM2rPWeKPNNsFTKILKkfQaAADkzAdBEySIukqsAMgcidSa0svpWruxRecohJ6N2qIQckFo21nWwB1MWcUBgsD-2uW6PQHAI12ITrTNhTgWhxnjBPBOrCINQYTHDLMnCgMwAjY+i9pUyzEy-WdSwfDqyTLLXO0ofhwpRGDYi7QULy2yOmtgCjT7DERVVPuMqaLCqyoNSYgDuZQ1BrGMSmxoGwT5JZbDfmATowhPnvnJeCSvD6sTiET92BUmEHiIkbJ+9qbifpozZmrNWjGCk3x64QI213tyiAbgeAFDqakdG1ysby19LswiWdG56NlwroYUIYQzYG28CMQsYR5AyrUH5GR7benkB8BuAAhLx619TbOqYHjYt6bbxMhLM0y5OMNTCrSAA)

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
