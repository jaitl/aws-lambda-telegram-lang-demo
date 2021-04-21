# aws-lambda-telegram-lang-demo
[![build](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml/badge.svg)](https://github.com/Jaitl/aws-lambda-telegram-lang-demo/actions/workflows/build.yml)

Demo project for my article: name

## The bot uses several AWS services
* Polly - to synthesize an audio
* Transcribe - to recognize a voice message to text
* Translate - to translate a foreign text to your language
* Lambda - to run a serverless application

## How to run the bot locally
1. Create an AWS user then adjust policies for the user to AWS services written above.
2. Create `~/.aws/credentials` file according to the AWS docs.
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
    aws lambda create-function --region us-west-2 --function-name lambda-telegram-lang-bot \                                                 13:03:38 
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT-all.jar \
    --role arn:aws:iam::<your role iam number>:role/lambda-telegram-lang-role \
    --handler com.github.jaitl.aws.telegram.english.AwsLambdaMain::handler --runtime java11 \
    --environment "Variables={TELEGRAM_TOKEN=<your telegram token>}" \
    --timeout 30 --memory-size 500
    ```
4. Create a REST API gateway for `lambda-telegram-lang-bot` Lambda with POST method then deploy it.
5. Register a webhook url to telegram:
    ```bash
    curl https://api.telegram.org/bot<your telegram token>/setWebhook?url=https://<your api gateway url>.amazonaws.com/<your prodaction stage>
    ```
6. You've done. Now you can use your telegram bot.

## Useful commands:
* Updates the lambda:
    ```bash
    aws lambda update-function-code --function-name lambda-telegram-lang-bot \                                                   SIGINT(2) ↵  13:11:47 
    --zip-file fileb://build/libs/aws-lambda-telegram-lang-demo-1.0-SNAPSHOT-all.jar
    ```
* Shows the telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/getWebhookInfo 
    ```
* Drops the telegram webhook:
    ```bash 
    curl https://api.telegram.org/bot<your telegram token>/deleteWebhook 
    ```
