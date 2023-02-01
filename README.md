# SpringLoginService
Uses Spring framework to create a user registration/login application. 
Includes REST API calls and token generation.

## Overview
In this application, a user must first register for an account through
a form. After they register, they will be given a unique verification link. Once they
click the verification link, their account will be verified, and they may log in using
their credentials. If their account is not registered or verified they will not
be able to log in.

## How To Use

1. Run the Program
2. Go to the following URL (please see note below): http://localhost:8080/api/v1/registration
3. Submit the form presented
4. After submitting, you will be given a verification link. Click the link to verify your account
5. Once your account is verified you may now log in

**_NOTE:_**  If you changed the default port number you will have to change the
URL port as well. Use the link pattern `http://localhost:[PORT NUMBER]/api/v1/registration`

#### URL PATTERNS 
Registration- `http://localhost:[PORT NUMBER]/api/v1/registration`
\
Log In - `http://localhost:[PORT NUMBER]/login`

## Setup
There is no setup required for this application. If one desires for added functionality or if the 
default port (8080) is not available, please follow the below setups
### Port Change (Optional)
1. Go to **application.properties**
2. Change the port number (**server.port** value) to a free port
3. You may now run the program (**Application.java**)

**_NOTE:_** If you desire to use added functionality you may
follow the below setups.

### PostgreSQL Setup (Optional)

This application was originally designed with PostgreSQL, but now supports in-memory databases as well. 
If one wishes to use PostgreSQL however, do the following:
1. Go to **application.properties** 
2. Enable (un-comment) the paragraph under the commented headline: `==== POSTGRESQL CONFIGURATION ====`
3. Disable (comment) the paragraph under the commented headline: `==== H2 CONFIGURATION ====`


### Email Setup (Optional)
In the original design, after a user registers, they were sent a verification link to
their email. But to send an email, the program's email address password
needed to be stored in the **application.properties** file. This is a security risk. In the future, there will be
another application on a public server running, where it will send an email via a POST call.


If you wish to use your own email address (as the sender) do as follows:
1. Go to **application.properties**
2. Go to the section under the commented headline: `==== EMAIL CONFIGURATION ====`
3. Set up the host, username and password values
4. Change `config.is-email-enabled=false` to `config.is-email-enabled=true`


## Future Plans
The aim for this program is to eventually turn this into a full stack application with more functionality than just 
simple login and registration.

### Next Updates
- Add unit tests
- Add integration tests
- Add error handling 
- Create a public server (for emailing only) so that emails may be safely sent without needing configuration