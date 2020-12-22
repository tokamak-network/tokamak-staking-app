<template>
<root>
   <StatusBar
            background-color="#d4d7da"
            bar-style="dark-content"
        />
  <app-navigator></app-navigator>
      <footer/>
</root>
</template>

<script>
import {
  createAppContainer,
  createStackNavigator,
  createDrawerNavigator
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
import Header from "./src/components/Header"
import Footer from '@/components/Footer'

import store from '@/store';
Vue.prototype.$store = store;
Vue.use(VueNativeBase);

const Drawer = createDrawerNavigator(
  {
    Home: {screen: HomeScreen},
    Operators: {screen: OperatorsScreen},
    Staking: {screen: StakingScreen},
    PowerTON: {screen: PowertonScreen}, 
    // Header: {screen: Header},
    // SelectedOperator : {screen: SelectedOperatorScreen},
  },
  {
    initialRouteName: "Home",
    contentOptions: {
      activeTintColor: "#e91e63"
    },
    contentComponent: SidebarScreen
  }
);
const AppNavigator = createAppContainer(
  createStackNavigator(
    {
      Login: {screen: LoginScreen},
      Drawer: {screen: Drawer},
      SelectedOperator : {screen: SelectedOperatorScreen},
      Header: {screen: Header}
    },
    {
      initialRouteName: "Login",
      headerMode: "none"
    }
  )
)
export default {
  components: { Root, AppNavigator, Footer },
}
</script>
<style scoped>
.statusBar {
  background-color: #d4d7da;
  height: 35px;
}
.logo-container {
  flex-direction: row;
  align-items: center;
  justify-content: center;

}
</style>