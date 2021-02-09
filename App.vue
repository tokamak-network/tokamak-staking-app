<template>
  <root>
    <StatusBar
      :background-color="signIn ? '#FAFBFC' : '#2a72e5'"
      bar-style="dark-content"
    />
    <view
      class="main-view"
      v-if="!loaded"
      :style="{ flex: 1, justifyContent: 'center', alignItems: 'center' }"
    >
      <image
        class="logo"
        :style="{ width: 235, height: 82, margin: 53 }"
        :source="require('./assets/logo.png')"
      />
        <touchable-opacity
          v-if="!loggedIn"
          class="button-login"
          :on-press="() => init()"
        >
        <text class="button-name">Connect Wallet</text>
        </touchable-opacity>
      <activity-indicator v-if="loggedIn" size="large" color="#FFFFFF" />
    </view>

    <app-navigator v-else></app-navigator>

    <!-- <footer
      :color1="signIn ? '#FFFFFF' : '#2a72e5'"
      :color2="signIn ? '#2a72e5' : '#FFFFFF'"
      :signedIn="signIn"
    /> -->
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
import SidebarScreen from "./src/components/Sidebar";
import OperatorsScreen from "@/screens/OperatorsScreen";
import HomeScreen from "@/screens/HomeScreen";
import StakingScreen from "@/screens/StakingScreen";
import SelectedOperatorScreen from "@/screens/SelectedOperatorScreen";
import PowertonScreen from "@/screens/PowertonScreen";
import AccountScreen from "@/screens/AccountScreen";
import LoginScreen from "@/screens/LoginScreen";
import Header from "./src/components/Header";
import Footer from "@/components/Footer";
import NavBarScreen from "./src/components/NavBar";
import { NativeModules, Alert } from "react-native";
import store from "@/store";
import { mapState } from "vuex";

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
      loggedIn: false
    }
  },
  components: { Root, AppNavigator },
  computed: {
    ...mapState(["loaded", "signIn"]),
  },
  methods: {
    init() {
      this.$store.dispatch("signIn");
      this.loggedIn = true;
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
  border-radius: 13;
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
  flex-direction: row;
  align-items: center;
  justify-content: center;
}
.main-view {
  background-color: #2a72e5;
}
</style>
