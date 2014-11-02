GivesMeHopeAndroidClient
========================

![Feature Graphic](https://cloud.githubusercontent.com/assets/9499097/4872524/1f5e740a-61e9-11e4-9165-8f48289c6a80.jpg)

An unofficial Gives Me Hope Android client for educational purposes.

Download at https://play.google.com/store/apps/details?id=com.jparkie.givesmehope.

This Android application allows the user to browse http://mobile.givesmehope.com/. Thus, it allows them to view the hottest and trending stories, to vote on new stories, and submit their own stories.

Disclaimer: This application was developed by an individual who does not have any affiliation with the content providers Gives Me Hope and affiliates. This application is for educational purposes only. Please email park.jacob.96@gmail.com with any concerns or issues.

## The Source

This source is open for anyone as an educational resource for learning. Some aspects of the source have been commented. Methods which are variable to change or breakage were not commented thoroughly. Finally, please note that I have only but a month of actual Android development experience such that the professionalism of the application and the accuracy of some of my comments may be limited.

## Development Process

The first version of the application (1.0) was developed from October 31st, 2014 and released on November 1st, 2014. The overall development time was approximately eight hours (A few hours were spent experimenting possible features and learning the newly updated AppCompat library). This application utilized RxJava (https://github.com/ReactiveX/RxJava) and RxAndroid (https://github.com/ReactiveX/RxAndroid) for the application's concurrency. The networking for the application was achieved through OkHttp 2.0 (http://square.github.io/okhttp/) as Gives Me Hope did not have an official REST API such that I could have used Retrofit. The image downloading and caching was facilitated by the use of Picasso (http://square.github.io/picasso/). Finally, the HTML parsing was achieved through Jsoup (http://jsoup.org/).

## Experimenting

If you are just learning Android development, I recommend importing the source to Android Studio (https://developer.android.com/sdk/installing/studio.html) and just play with the source. If you are so inclined, you can adapt the source to make a client aside from Gives Me Hope. The following lists mobile websites which I believe follow a similar structure to the mobile Gives Me Hope website:
- http://mobile.love.givesmehope.com/
- http://mobile.thatssotrue.com/
- http://mobile.tasteofawesome.com/
- http://mobile.memestache.com/
- http://mobile.ragestache.com/
- http://mobile.sixbillionsecrets.com/
- http://mobile.smartphowned.hollywood.com/
- http://mobile.unfriendable.hollywood.com/
- http://mobile.pokestache.com/

## Screen Shots
![1](https://cloud.githubusercontent.com/assets/9499097/4872520/132ad250-61e9-11e4-8137-940962de82e3.png)
![2](https://cloud.githubusercontent.com/assets/9499097/4872519/132629b2-61e9-11e4-871f-ec6849b97189.png)
![3](https://cloud.githubusercontent.com/assets/9499097/4872521/132cf5da-61e9-11e4-968d-8a8d118a0443.png)
![4](https://cloud.githubusercontent.com/assets/9499097/4872522/1335a3d8-61e9-11e4-9b30-d607c9797e7b.png)

## License

    Copyright 2014 Jacob Park
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
