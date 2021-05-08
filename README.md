# aws-lambda-telegram-lang-demo
[![build](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml/badge.svg)](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml)

Demo project for my article: [How AWS AI Services Can Help You Improve Your Foreign Language](https://jaitl.pro/post/2021/04/26/aws-foreign-language/).

## The bot uses several AWS services
* Polly - to synthesize an audio
* Transcribe - to recognize a voice message to a text
* Translate - to translate a foreign text to your language
* Lambda - to run a serverless application

## How to use
Set up your first and foreign languages [in this file](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/blob/main/src/main/kotlin/com/github/jaitl/aws/telegram/english/aws/Aws.kt).

Then you can:
* Send a voice message to recognize your speech and receive a text of the message.
* Send a text message to receive an audio message from the text and translation to another language.

## How to run the bot locally
1. Create an AWS user then adjust the policies for the user:
    * `AmazonTranscribeFullAccess`
    * `AmazonPollyReadOnlyAccess`
    * `TranslateReadOnly`
2. Create `~/.aws/credentials` file according to [AWS docs](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html).
3. Run the bot application
   ```bash
   ./gradlew run -Dconfig.override.telegram-token=<your telegram token>
    ```

## How to deploy the bot on AWS Lambda
1. Create a role `lambda-telegram-lang-role` for lambda function
   ```bash
   aws iam create-role --role-name lambda-telegram-lang-role --assume-role-policy-document file://aws-trust-policy.json
   ```
2. Attach policies to the role
   ```bash
   aws iam attach-role-policy --role-name lambda-telegram-lang-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
   aws iam attach-role-policy --role-name lambda-telegram-lang-role --policy-arn arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess
   aws iam attach-role-policy --role-name lambda-telegram-lang-role --policy-arn arn:aws:iam::aws:policy/AmazonTranscribeFullAccess
   aws iam attach-role-policy --role-name lambda-telegram-lang-role --policy-arn arn:aws:iam::aws:policy/AmazonPollyReadOnlyAccess
   aws iam attach-role-policy --role-name lambda-telegram-lang-role --policy-arn arn:aws:iam::aws:policy/TranslateReadOnly
   ```
2. Build a jar file
    ```bash
   ./gradlew shadowJar
    ```
3. Create a lambda using aws cli:
    ```bash
    aws lambda create-function --region us-west-2 --function-name lambda-telegram-lang-bot \
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT-all.jar \
    --role arn:aws:iam::<your role iam number>:role/lambda-telegram-lang-role \
    --handler com.github.jaitl.aws.telegram.english.AwsLambdaMain::handler --runtime java11 \
    --environment "Variables={TELEGRAM_TOKEN=<your telegram token>}" \
    --timeout 30 --memory-size 500
    ```
4. Create a REST API gateway for `lambda-telegram-lang-bot` Lambda with POST method then deploy it. [AWS docs](https://docs.aws.amazon.com/apigateway/latest/developerguide/getting-started.html) will help you.
5. Register a webhook url to Telegram:
    ```bash
    curl https://api.telegram.org/bot<your telegram token>/setWebhook?url=https://<your api gateway url>.amazonaws.com/<your prodaction stage>
    ```
6. You've done. Now you can use your Telegram bot.

## Useful commands:
* Updates the lambda:
    ```bash
    aws lambda update-function-code --function-name lambda-telegram-lang-bot \
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT-all.jar
    ```
* Shows the Telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/getWebhookInfo 
    ```
* Drops the Telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/deleteWebhook 
    ```
