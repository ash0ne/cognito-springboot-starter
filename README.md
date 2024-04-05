# Cognito Spring Boot Starter

- [Overview](#overview)
- [Getting Started](#getting-started)
- [Settings](#settings)
- [Security Config](#security-config)

## Overview

This project provides the boilerplate code for integrating Amazon Cognito OAuth into a Spring Boot application.
It's designed to work seamlessly with the [cognito-react-starter](https://github.com/ash0ne/cognito-react-starter),
but can be used with any frontend of your choice.

## Getting Started

- Before using this application, it's necessary to set up
  an [AWS Cognito Identity Pool](https://docs.aws.amazon.com/cognito/latest/developerguide/getting-started-with-identity-pools.html)
  and have a User and a User Group created . This process is pretty straightforward
  
  > Note: The default configuration on this app assumes that you have created a Cognito Identity Pool without an App
  Client Secret since this is the most common pattern used for oAuth apps with user-facing frontends.
  >
  > If you instead use an identity pool that has a client secret, remove the
  line `spring.security.oauth2.client.registration.cognito.client-authentication-method=none`
  from `application.properties` and add your Cognito Client Secret in a new config field as new
  key `spring.security.oauth2.client.registration.cognito.client-id=<cognito_client_secret>`
  >
  > Refer to [this Stack Overflow thread](https://stackoverflow.com/questions/47916034/what-is-a-cognito-app-client-secret) for
  more information.
  
- Make sure you have the Java 21 jdk and docker installed.
- Checkout the project and build it by `./gradlew clean build` from the project directory.
- Ensure Docker is installed and then run `docker-compose up -d` in the `local-run` directory to get a local Postgres
  database running.
- update the Cognito config placeholders in `application.properties` to the correct values.
- Start the application by running the main class.
- Post the successful startup, you can go to `http://localhost:8080/swagger-ui/index.html` to see the APIs.
  > Note: Attempting to try the APIs from Swagger will result in a `401` error as the app will not have the auth
  context. If you want to quickly try the GET APIs through swagger-ui and want to check that the application is working
  with your AWS Cognito config, set `cognito.client-behaviour-enabled` in `application.properties` to `true`

## Settings

All the settings required are in:

- `src/resources/application.properties`:
    - Update the placeholders with your Cognito config.
    - Update the database properties to connect to your database. The default values are configured to connect to the
      local database.

## Security Config

- By default, the `/api/**` route is protected, while all other routes are open.
- Accessing `/swagger-ui/index.html` will only allow viewing the API docs;
  attempting to try the APIs will result in a 401 error unless you have the app in OAuth Client Mode explained below.

  ### OAuth Client Mode

  If you're using another OAuth client (typically a frontend), you generally wouldn't need to set this. However, if it's
  convenient for testing APIs without a frontend, you can enable the OAuth client behaviour directly on the app by
  following these steps:

    - Set `cognito.client-behaviour-enabled` in `application.properties` to `true`. This will make all routes
      authenticated and will redirect to Cognito when accessing `/swagger-ui/index.html`.
    - By default, only GET requests are allowed from Swagger, and the SpringBoot CSRF config prevents other methods.
      Adjust the security config accordingly if you need to enable other methods like POST, DELETE, etc.
