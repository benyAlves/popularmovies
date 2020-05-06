# popularmovies
I built this app as a part of course program project. 

This app to allow users to discover the most popular movies playing.

# Specs
* Lists a grid list of movies
* Allow your user to change sort order via a setting:
* The sort order can be by most popular, or by top rated
* Allow the user to tap on a movie poster and transition to a details screen with some additional information.

# How to run
This app uses themoviedb API to get the Movies. So you should request an api key in https://www.themoviedb.org/account/signup.

Once requested your API_KEY, create a resource file for your secrets called res/values/secrets.xml with a string for api key value:
```<?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string name="themoviedb_api_key">YOUR_API_KEY</string>
     </resources>
```

 Now you are ready to enjoy!
