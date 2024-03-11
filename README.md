# Introduction
This API allows you to save any kind of collection of data in database.

# Bruno
Bruno is an API Client allowing us to save API requests in the project for others to reuse.
In order to use it, download Bruno and select "Open Collection". Select the bruno repository in the project, and you'll have access to the API requests saved on this project.

# Endpoints
After launching the app, you can see all the available endpoints and how to use them at the URL : http://localhost:8080/swagger-ui/index.html

# Setup
If you want to launch the app locally for the first time : 
1. Make sure your postgre password is set in the application-local.yml file
2. Make sure you have a database named "collectionSaver" created
3. Launch the app
4. Initialize database : execute the sql files in the `database` folder

You're ready to execute your requests !

# Authentication
The current authentication is handled by spring security and user credentials are saved in database (password encrypted).
After creating a user with the `signup` endpoint, the first thing to do is use the `login` endpoint in order to get a jwt token that will need to be passed as a header named `Authorization` in all the other requests.
