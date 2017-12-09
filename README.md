# ao3-java [![GitHub release](https://img.shields.io/github/release/glorantq/ao3-java.svg?style=flat-square)](https://github.com/glorantq/ao3-java/releases) [![Github All Releases](https://img.shields.io/github/downloads/glorantq/ao3-java/total.svg?style=flat-square)](https://github.com/glorantq/ao3-java)
This library provides access to AO3 data like works, chapters and users.

This is **not** an official API.

### Motivation
I made this library becasue I want to make projects that use AO3 data. It is far from perfect, but it gets the job done and is easy to use.

### Features
- Work, user and chapter data
- All objects can be converted to JSON and back
- Allows searching for works

### Watch out
Again, this is **not** an official API. At the moment there is no official one. This works by parsing the HTML of the pages and getting information from that. Any major layout changes made to the Archive will most definitely break this. I'll try to keep this as updated as possible, but keep in mind there is **no** guarantee that this will work.

### Examples
The API is extremely easy to use, most things are just a line of code. The examples use Kotlin, but it's the same in Java. Data is shown as JSON here just for simplicity, of course it can be accessed from Java code as well.

All methods querying the Archive can be found in the AO3 class:
```kotlin
AO3.getWork(workID)
AO3.getUser(username)
AO3.getPseud(username, pseud)
AO3.searchWork(query, warnings, rating) // Has other overloads!
```
Exceptions include the `getChapter` method in AO3Work and the `fromJson` method in AO3Data.

Any data returned by this API can be converted to JSON by calling its `json` method:
```kotlin
val work: AO3Work = AO3.getWork(workId)
val user: AO3User = AO3.getUser(username)
val chapter: AO3Chapter = work.getChapter(chapterID)

work.json()
user.json()
chapter.json()
```

#### Getting a Work
```kotlin
val work: AO3Work = AO3.getWork(workID) // workID is the numeric part of the URL: http://archiveofourown.org/works/xxxxxxxx/
```
Serialised as JSON:
```json
{
  "title": "ayy",
  "authors": [
    {
      "imageUrl": "https://s3.amazonaws.com/otw-ao3-icons/icons/1953616/standard.png?1511579332",
      "fandoms": [
        "Miraculous Ladybug",
        "Voltron: Legendary Defender"
      ],
      "recentWorks": [
        12824172,
        11304987,
        12231312,
        11738514,
        11326692
      ],
      "username": "agrestenoir",
      "pseud": "agrestenoir"
    }
  ],
  "archiveWarning": "NONE_APPLY",
  "rating": "GENERAL",
  "category": "F_M",
  "fandom": "the internet",
  "relationships": [
    "1x2"
  ],
  "characters": [
    "me",
    "you",
    "them",
    "us"
  ],
  "additionalTags": [
    "Flirting",
    "Romance",
    "Drama",
    "someone help",
    "don\u0027t"
  ],
  "language": "English",
  "stats": {
    "hits": "2943",
    "bookmarks": "53",
    "comments": "108",
    "kudos": "321",
    "chapters": "4/?",
    "words": "8273"
  },
  "published": "Apr 25, 2017 12:00:00 AM",
  "updated": "Jun 16, 2017 12:00:00 AM",
  "chapters": {
    "23782818": "1. i must be in seine",
    "23867334": "2. RIP: boiled water, you will be mist.",
    "24063258": "3. the commentator, just your everyday potato",
    "25066965": "4. up beet, down beet, sick beet, dead beet (veggie..."
  },
  "summary": "aaaaaaaaAAAAAAAAAAAA",
  "id": 10731414
}
```
#### Searching for Works
You can search for works with a simple query, or optionally you can specify a rating and warnings
```kotlin
val works: List<AO3Work> = AO3.searchWorks("Simple Query")
val works: List<AO3Work> = AO3.searchWorks("Mature Works", AO3Work.Ratings.MATURE)
val reallyComplex: List<AO3Work> = AO3.searchWorks("Specific", AO3Work.Warnings.NONE_APPLY, AO3Work.Ratings.MATURE)
```
#### Getting a Chapter
You can get chapters by passing in a chapter ID to a Work object. The first element of the `notes` array is the note at the beginning, the last on is the end note.
```kotlin
val chapter: AO3Chapter = AO3.getWork(workID).getChapter(chapterID) // Chapter ID can be obtained from the chapters map in AO3Work
```
Serialised as JSON:
```json
{
  "title": "my awesome chapter",
  "content": "long stuff here",
  "notes": [
    "ayy lmao nicc"
  ],
  "workID": 84869963,
  "id": 16994373
}
```

#### Getting a User
You can get a user based on their username. Optionally, you can specify a pseud to use
```kotlin
val user: AO3User = AO3.getUser(username)
val pseud: AO3User = AO3.getPseud(username, pseud)
```
Serialised as JSON:
```json
{
  "imageUrl": "https://s3.amazonaws.com/otw-ao3-icons/icons/3027026/standard.gif?1504399592",
  "fandoms": [],
  "recentWorks": [],
  "username": "glorantq",
  "pseud": "glorantq"
}
```

#### Converting from JSON
Converting from JSON can be done with the `fromJson` method in AO3Data
```kotlin
val work: AO3Work = AO3Data.fromJson(data, AO3Work::class.java)
val chapter: AO3Chapter = AO3Data.fromJson(data, AO3Chapter::class.java)
val user: AO3User = AO3Data.fromJson(data, AO3User::class.java)
```

### Usage
You can grab the latest JARs from the releases page. The fat JAR includes dependencies as well, the regular one needs to have these dependencies imported as well:
* JSoup (1.11.1)
* OkHttp 3 (3.9.0)
* GSON (2.8.2)
* Guava (23.4)

The library is compiled with Java 8. If you need support for earlier versions you'll need to compile it again.
