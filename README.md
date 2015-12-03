# SimpleInstagramViewer
A sample Instagram viewer project for CodePath

This is an Android demo application for displaying the latest popular posts from Instagram.

## User Stories

The following **required** functionality is completed:

* [x] User can scroll through current popular photos from Instagram
* [x] For each photo displayed, user can see the following details:
      - Graphic, Caption, Username
      - Optional) relative timestamp, like count, user profile image  

The following **optional** features are implemented:

* [x] Add pull-to-refresh for popular stream with SwipeRefreshLayout
* [x] Show latest comment for each photo (bonus: show last 2 comments: 2nd comment not shown due to layout size)
* [x] Display each user profile image using a RoundedImageView
* [x] Display a nice default placeholder graphic for each image during loading (read more about Picasso: used Glide)
* [x] Improve the user interface through styling and coloring
* [x] Allow user to view all comments for an image within a separate activity or dialog fragment
* [x] Allow video posts to be played in full-screen using the VideoView

The following **additional** features are implemented:

* [x] Added a splash screen
* [x] Added activity transition animations
* [x] Added a floating action bar button for refreshing the view
* [x] Added logic to start pre-loading the content in the splash page
* [x] The list of popular pictures refreshes onResume if the previous load is over 20 seconds ago
* [x] The video restarts if the orientation changes in the middle of playback
* [x] Added a “Play” button to watch video directly from the popular pictures activity


Walkthrough of all user stories:

![Video Walkthrough](InstagramTester.gif)
* Please note that video playback doesn’t work in the emulator, but works in an physical device and hence is not part of the video walkthrough.

GIF created with [LiceCap](http://www.cockos.com/licecap/).
