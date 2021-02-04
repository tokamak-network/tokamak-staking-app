<template>
<root>
   <StatusBar
            background-color="#d4d7da"
            bar-style="dark-content"
        />
         <view 
        v-if="!loaded"
        :style="{flex: 1, justifyContent: 'center'}">
        <activity-indicator size="large" color="#0000ff" />
    </view>
 
  <app-navigator v-else>
  </app-navigator>
  
</root>
</template>

<script>
import {
  createAppContainer,
  createStackNavigator,
  createBottomTabNavigator
  // createDrawerNavigator
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
import SplashScreen from "@/screens/SplashScreen"
import Header from "./src/components/Header"
import NavBarScreen from "./src/components/NavBar"
import { NativeModules, Alert } from 'react-native';
import store from '@/store';
import { mapState } from 'vuex';
const { BlockchainModule } = NativeModules;
import TestScreen from "@/screens/TestScreen";


Vue.prototype.$store = store;
Vue.use(VueNativeBase);

const BottomTabNavigator = createBottomTabNavigator(
  {
    Home: {screen: HomeScreen},
    Operators: {screen: OperatorsScreen},
    Staking: {screen: StakingScreen},
    PowerTON: {screen: PowertonScreen}, 
    Account: {screen: AccountScreen},
    TestScreen: {screen: TestScreen}
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
      BottomTabNavigator: {screen: BottomTabNavigator},
    },
    {
      headerMode: "none"
    }
  )
)
export default {
  components: { Root, AppNavigator },
    computed: {
    ...mapState(['loaded']),
  },
  created () {
    this.init();
  },
  methods: {
    init () {
      this.$store.dispatch('signIn')
    }
  }
}
</script>
<style scoped>
.statusBar {
  background-color: #d4d7da;
  height: 35px;
}
</style>