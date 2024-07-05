# comments-collect-server

<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
    <h3 align="center">Youtube Comments Collector</h3>
</div>



<!-- TABLE OF CONTENTS -->



<!-- ABOUT THE PROJECT -->
## About The Project

This is a simple CLI program that collects comments from a Youtube video. It uses the Youtube API to get the comments.

[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)


### Built With

This section should list any major frameworks/libraries used to bootstrap your project. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.

* [![picocli][picocli-img]][picocli-url]
* [![Gradle][gradle-img]][gradle-url]

[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)



<!-- GETTING STARTED -->
## Getting Started

This project is built with Gradle. To build the project, you need to have Gradle installed on your machine or use the Gradle wrapper. 

This is an example of build and run the project.

### Build with Gradle Wrapper

```shell
./gradlew build -x test
```

### Build with Gradle installed on your machine

```shell
gradle wrapper --gradle-version 8.8
gradle build -x test
```

### Run the command

After building the project, you can run the command with the following command.

```shell
# java -cp { builded jar file } { command you want to run } { ... arguments }
java -cp build/libs/comments-collect-server-1.0-SNAPSHOT.jar command.TutorialCommand
```

<!-- USAGE EXAMPLES -->


<!-- ROADMAP -->


<!-- CONTRIBUTING -->


<!-- LICENSE -->
## License

Distributed under the Mozilla Public License. See `LICENSE.txt` for more information.

[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)


<!-- CONTACT -->


<!-- ACKNOWLEDGMENTS -->


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/comment-clean-bot/youtube-comments-collector.svg?style=for-the-badge
[contributors-url]: https://github.com/comment-clean-bot/youtube-comments-collector/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/comment-clean-bot/youtube-comments-collector.svg?style=for-the-badge
[forks-url]: https://github.com/comment-clean-bot/youtube-comments-collector/network/members
[stars-shield]: https://img.shields.io/github/stars/comment-clean-bot/youtube-comments-collector.svg?style=for-the-badge
[stars-url]: https://github.com/comment-clean-bot/youtube-comments-collector/stargazers
[issues-shield]: https://img.shields.io/github/issues/comment-clean-bot/youtube-comments-collector.svg?style=for-the-badge
[issues-url]: https://github.com/comment-clean-bot/youtube-comments-collector/issues
[license-shield]: https://img.shields.io/github/license/comment-clean-bot/youtube-comments-collector.svg?style=for-the-badge
[license-url]: https://github.com/comment-clean-bot/youtube-comments-collector/blob/master/LICENSE.txt

[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 
[picocli-img]: https://img.shields.io/badge/picocli-ed9226?style=for-the-badge&logo=picocli&logoColor=white
[picocli-url]: https://picocli.info
[gradle-img]: https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[gradle-url]: https://gradle.org