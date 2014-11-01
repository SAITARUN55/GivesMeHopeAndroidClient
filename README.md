GivesMeHopeAndroidClient
========================

![Feature Graphic](https://cloud.githubusercontent.com/assets/9499097/4872524/1f5e740a-61e9-11e4-9165-8f48289c6a80.jpg)

An unoffical Gives Me Hope Android client for educational purposes.

Download at https://play.google.com/store/apps/details?id=com.jparkie.givesmehope.

This Android application allows the user to browse http://mobile.givesmehope.com/. Thus, it allows them to view the hottest and trending stories, to vote on new stories, and submit their own stories.

## The Source

This source is open for anyone as an educational resource for learning. Some aspects of the source have been commented. Methods which are variable to change or breakage were not commented thoroughly. Finally, please note that I have only but a month of actual Android development experience such that the professionalism of the application and the accuracy of some of my comments may be limited.

## Development Process

The first version of the application (1.0) was developed from October 31st, 2014 and released on November 1st, 2014. The overall development time was approximately eight hours. This application utilized RxJava (https://github.com/ReactiveX/RxJava) and RxAndroid (https://github.com/ReactiveX/RxAndroid) for its concurrency. The networking for the application was achieved through OkHttp 2.0 (http://square.github.io/okhttp/) as Gives Me Hope did not have an official REST API such that I could have used Retrofit. The image downloading and caching was facilitated by the use of Picasso (http://square.github.io/picasso/). Finally, the HTML parsing was achieved through Jsoup (http://jsoup.org/).

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
