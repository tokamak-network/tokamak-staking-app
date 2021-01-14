# tokamak-staking-app

## Install
```
npm install 
```
## Setup local environment 
```
export ANDROID_HOME=~/Library/Android/sdk
```
## Bundle 
```
react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
```

## Start React Native 
```
npx react-native start
```

## Run the project 
``` 
npx react-native run-android
```
