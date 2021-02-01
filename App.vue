<template>
  <root>
    <StatusBar
      :background-color="signIn ? '#d4d7da' : '#2a72e5'"
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
      <activity-indicator size="large" color="#FFFFFF" />
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
import LoginScreen from "@/screens/LoginScreen";
import Header from "./src/components/Header";
import Footer from "@/components/Footer";
import NavBarScreen from "./src/components/NavBar"
import { NativeModules, Alert } from "react-native";
import store from "@/store";
import { mapState } from "vuex";


const { BlockchainModule } = NativeModules;

Vue.prototype.$store = store;
Vue.use(VueNativeBase);

const BottomTabNavigator = createBottomTabNavigator(
  {
   Home: {screen: HomeScreen},
    Operators: {screen: OperatorsScreen},
    Staking: {screen: StakingScreen},
    PowerTON: {screen: PowertonScreen}, 
   
  },
  {
    initialRouteName: "Home",
    // tabBar: NavBar,
     tabBarComponent: NavBarScreen,
  }
);
const AppNavigator = createAppContainer(
  createStackNavigator(
    {
      Login: { screen: LoginScreen },
       BottomTabNavigator: {screen: BottomTabNavigator},
      SelectedOperator: { screen: SelectedOperatorScreen },
      Header: { screen: Header },
    },
    {
      initialRouteName: "Login",
      headerMode: "none",
    }
  )
);
export default {
  components: { Root, AppNavigator },
  computed: {
    ...mapState(["loaded", "signIn"]),
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.$store.dispatch("signIn");
    },
  },
};
</script>
<style scoped>
.test {
  margin-left: -8px;
  margin-right: -8px;
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
