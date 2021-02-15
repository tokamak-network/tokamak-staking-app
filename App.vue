<template>
  <root>
    <StatusBar
      :background-color="signIn ? '#FAFBFC' : '#2a72e5'"
      bar-style="dark-content"
    />
    <view
      class="main-view"
      v-if="!loaded"
      :style="{ flex: 1}"
    >
    <view class="logo-container" 
     :style="{ height:windowHeight*0.636}">
      <image
        class="logo"
        :style="{ width: 235, height: 82, marginBottom: windowHeight*0.08 }"
        :source="require('./assets/logo.png')"
      />
        <touchable-opacity
          v-if="!isLogin"
          class="button-login"
          :on-press="() => init()"
        >
        <text class="button-name">Connect Wallet</text>
        </touchable-opacity>
      <activity-indicator v-if="isLogin" size="large" color="#FFFFFF" />

 
      <image
       :style="{ marginTop: windowHeight*0.33, height:40, width: 113}"
        :source="require('./assets/TN_logo.png')"
      />
    </view>
    </view>
     
    <app-navigator v-else></app-navigator>

  </root>
</template>

<script>
import {
  createAppContainer,
  createStackNavigator,
  createBottomTabNavigator,
} from "vue-native-router";
import Vue from "vue-native-core";
import { Root, VueNativeBase } from "native-base";
import OperatorsScreen from "@/screens/OperatorsScreen";
import HomeScreen from "@/screens/HomeScreen";
import StakingScreen from "@/screens/StakingScreen";
import SelectedOperatorScreen from "@/screens/SelectedOperatorScreen";
import PowertonScreen from "@/screens/PowertonScreen";
import AccountScreen from "@/screens/AccountScreen";
import LoginScreen from "@/screens/LoginScreen";
import Header from "./src/components/Header";
import NavBarScreen from "./src/components/NavBar";
import { NativeModules, Alert } from "react-native";
import store from "@/store";
import { mapState } from "vuex";
import { Dimensions } from 'react-native';

const { BlockchainModule } = NativeModules;

Vue.prototype.$store = store;
Vue.use(VueNativeBase);

const BottomTabNavigator = createBottomTabNavigator(
  {
    Home: { screen: HomeScreen },
    Operators: { screen: OperatorsScreen },
    Staking: { screen: StakingScreen },
    PowerTON: { screen: PowertonScreen },
      Account: {screen: AccountScreen}
  },
  {
    initialRouteName: "Home",
    // tabBar: NavBar,
    tabBarComponent: NavBarScreen,
    
      keyboardHidesTabBar: true,
 
  }
);
const AppNavigator = createAppContainer(
  createStackNavigator(
    {
      LoginScreen: { screen: LoginScreen },
      BottomTabNavigator: { screen: BottomTabNavigator },
      SelectedOperator: { screen: SelectedOperatorScreen },
      Header: { screen: Header },
    },
    {
      initialRouteName: "LoginScreen",
      headerMode: "none",
    }
  )
);
export default {
  data() {
    return {
      
    }
  },
  components: { Root, AppNavigator },
  computed: {
    ...mapState(["loaded", "signIn", "isLogin"]),
     windowWidth () {
      return Dimensions.get('window').width
    },
    windowHeight () {
      return Dimensions.get('window').height
    }
  },
  created() {
  },
  methods: {
    init() {
      this.$store.dispatch("isLogin");
      this.$store.dispatch("signIn");
    },
  },
};
</script>
<style scoped>
.button-login {
  width: 130;
  height: 35;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 19;
  display: flex;
  justify-content: center;
  align-items: center;
}
.button-name {
  color: #ffffff;
}
.statusBar {
  background-color: #d4d7da;
  height: 35px;
}
.logo-container {
  flex-direction: column;
  align-items: center;
 
}
.main-view {
  background-color: #2a72e5;
  display: flex;
  justify-content: flex-end;
}
</style>
