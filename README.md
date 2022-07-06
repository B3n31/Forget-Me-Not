This is a social app for dementia people to get together and sing
## Run the Sample Project
Since we are using API provided to us by VideoSDK, you will need the TOKEN inorder to run the app.

Please follow the steps below careful to be able to compile the code.

### Step 1: Create local.properties
If you your root directory have this file, please move to step 3. Otherwise, create a file with exactly the name and extentions called local.properties.

### Step 2: Add this your SDK directory to your project

**For windows users:** <br>
sdk.dir = C:\\\Users\\\your_user_name\\\AppData\\\Local\\\Android\\\Sdk

**For Mac users:** <br>
sdk.dir=/Users/your_user_name/Library/Android/sdk

**For Linux (Ubuntu) users:**<br>
sdk.dir = /home/your_user_name/Android/Sdk

Where your_user_name is your user name of your operating syste. Also, make sure the folder is sdk or Sdk since some operating system is very case sensitive for the directory name.

The Andriod SDK folder should be in this directory. If you do not see this, there might be error in your installation.

In case adding the sdk.dir to local.properties does not work for Window, add ANDROID_HOME variable in "Environment Variables" as C:\Users\your_user_name\AppData\Local\Android\Sdk

### Step 3: Add auth_token
Please add the line below to your local.properties

auth_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiI0MGJjZGYyZC1kYzNkLTQ3YzktYjQ4ZS1jOWE1MWZhMzMyMWQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY1Njk5NTUwMiwiZXhwIjoxNjU3NjAwMzAyfQ.SJILODMnECna2edeOjCCjO7q_6k3btQ4m_WfBvqWrCU

The above token is a token we will be using for app in this project. Please do not use this token for your own interest.

If the token above is not working, please contact us and we will provide you guys a new token.

### Step 4: Run the sample app
Run the android app with **Shift+F10** or the ** ▶ Run ** from toolbar. 