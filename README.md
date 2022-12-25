# Exams Race Game Android App
## About
A student-racer runaway from exams-obstacles and  collect grade-coins.
Racer can move Left / Right by buttons or by sensor (tilt - R/L).
Game include 5 roads.
Game start with a 3 life.
on crash exam-obstacle app vibrate, toast , play sound and reduce life.
on crash grade-coin app, play sound and increas score life.

All records saved to SharePrefrences.
Records screen dispay list on record and map.
Click on record item make zoom on map (Location pf game played). 

The Game written as an exercise in the mobile application course at the college.
 
## Gameplay Version 1
https://user-images.githubusercontent.com/93843185/205163941-fc16e8c0-ce1d-4c2d-9022-5ce41930fdc1.mp4

## Gameplay Version 2
https://user-images.githubusercontent.com/93843185/209480624-a5007de7-f7fb-4d96-aac8-6ba879970224.mp4

## Permissions
This app requires the following permissions:
- Vibrate
- Internet
- location

## Dependencies

implementation 'com.github.bumptech.glide:glide:4.14.2'

annotationProcessor 'com.github.bumptech.glide:compiler:4.14.2'
    
implementation 'com.google.code.gson:gson:2.10'

implementation 'com.google.gms:google-services:4.3.14'

implementation 'com.google.android.gms:play-services-maps:18.1.0'

implementation 'com.google.android.gms:play-services-location:21.0.1'
