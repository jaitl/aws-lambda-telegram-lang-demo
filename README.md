# aws-lambda-telegram-lang-demo
[![build](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml/badge.svg)](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml)

Demo project for my article: name

The bot uses several AWS services:
* Polly - to an audio synthesize
* Transcribe - to a voice message recognition
* Lambda - to serverless running

## How to build jar
`./gradlew shadowJar`

## How to run the bot on server
1. Create an AWS user then adjust policies for the user to AWS services written above.
2. Create `~/.aws/credentials` file according to the AWS docs.
3. Export the env variable `TELEGRAM_TOKEN` with your telegram token.
4. Build the jar file then run with the main class `com.github.jaitl.aws.telegram.english.MainLocal`

## How to deploy the bot on aws lambda
1. Create the role `lambda-telegram-lang-role` with policies for AWS services written above.
2. Build the jar file
    `./gradlew shadowJar`
3. Create the lambda using aws cli:
    ```bash
    aws lambda create-function --region us-west-2 --function-name lambda-telegram-lang-bot \                                                 13:03:38 
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT.jar \
    --role arn:aws:iam::<your role iam number>:role/lambda-telegram-lang-role \
    --handler com.github.jaitl.aws.telegram.english.AwsLambdaMain::handler --runtime java11 \
    --environment TELEGRAM_TOKEN=<your telegram token> \
    --timeout 30 --memory-size 400
    ```
4. Create a REST API gateway for `lambda-telegram-lang-bot` with POST method then deploy it.
5. Register a webhook url to telegram:
    ```bash
    curl https://api.telegram.org/bot<your telegram token>/setWebhook?url=https://<your api gateway url>.amazonaws.com/<your prodaction stage>
    ```
6. You've done. Now you can use your telegram bot.

## Useful commands:
* Updates the lambda:
    ```bash
    aws lambda update-function-code --function-name lambda-english-bot \                                                   SIGINT(2) ↵  13:11:47 
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT.jar
    ```
* Shows the telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/getWebhookInfo 
    ```
* Drops the telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/deleteWebhook 
    ```
