# Popular Movies Stage 1
This Android app:
* Presents the user with a grid arrangement of movie posters upon launch.
* Allows the user to change sort order via a setting
  * The sort order can be by most popular or by highest-rated
* Allows the user to tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date

## Getting Started
This project uses the Gradle build system. 
To build this project,

1. Use "Import Project" in Android Studio
2. Go to app/build.gradle
3. Replace PASTE_YOUR_THEMOVIEDB_API_KEY_HERE with your api_key registered under api.themoviedb.org
4. Sync your project with gradle

## How to Contribute
1. Submit an issue describing your proposed change to the repo in question.
2. The repo owner will respond to your issue promptly.
3. Fork the desired repo, develop and test your code changes.
4. Ensure that your code has an appropriate set of unit tests which all pass.
5. Submit a pull request.

## License

MIT License

Copyright (c) 2017 Rohit Gupta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
