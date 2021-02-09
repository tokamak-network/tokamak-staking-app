# tokamak-staking-app

## Install
```
npm install 
```

## create file local.properties in the android folder and include the following line 
sdk.dir = /Users/YOUR_NAME/Library/Android/sdk 
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

```
npm start -- --reset-cache
```

## Run the project 
``` 
npx react-native run-android
```

## Crypto 
```
npm i --save react-native-crypto
# install peer deps 
npm i --save react-native-randombytes
react-native link react-native-randombytes
# install latest rn-nodeify 
npm i --save-dev tradle/rn-nodeify
# install node core shims and recursively hack package.json files 
# in ./node_modules to add/update the "browser"/"react-native" field with relevant mappings 
./node_modules/.bin/rn-nodeify --hack --install
rn-nodeify will create a shim.js in the project root directory
// index.ios.js or index.android.js
// make sure you use `import` and not require!  
import './shim.js'
// ...the rest of your code
react-native link
```