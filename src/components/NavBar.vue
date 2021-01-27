<template>
<view>
    <ImageBackground :source=Bg class="nav-bg">
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('Home')">
      <image :source=HomeIcon class="nav-icon" style="margin-left: 5"/>
      <text class="nav-text" :style="{marginLeft: 5}">Home</text>
      </touchable-opacity>
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('Operators')">
      <image :source=HomeIcon class="nav-icon"/>
      <text class="nav-text">Operators</text>
      </touchable-opacity>
      <touchable-opacity>
      <text class="nav-text">Operators</text>
    </touchable-opacity>
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('PowerTON')">
      <image :source=HomeIcon class="nav-icon"/>
      <text class="nav-text">Power TON</text>
      </touchable-opacity>
      <touchable-opacity class="nav-menu">
      <image :source=HomeIcon class="nav-icon"/>
      <text class="nav-text">Account</text>
      </touchable-opacity>
    </ImageBackground>
    <Image :source=StakingIcon class="nav-staking" />
    
    </view>
</template>

<script>
import { ImageBackground } from "react-native";
import { store } from "@/store/index";
import { mapState } from "vuex";

import Bg from "../../assets/nav-bar-bg.png"
import HomeIcon from "../../assets/toolbar-icon-home-active.png"
import StakingIcon from "../../assets/toolbar-icon-staking-inactive.png"

export default {
  data() {
    return {  
      Bg,
      HomeIcon,
      StakingIcon,
      isActive: false
    }
  },
  props: {
    navigation: {
      type: Object
    }
  },
  methods: {
    handleListItemClick(dataObj) {
      this.navigation.navigate(dataObj);
    }
  }
}
</script>

<style>
.nav-bg {
  width: 375px;
  height: 65px;
  display: flex;
  flex-direction: row;
  margin-left: -7px
}

.nav-menu {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 74px;
  height: 65px;
}

.nav-icon {
  width: 24px;
  height: 24px;
  margin-bottom: 5px;
}

.nav-text {
  font-size: 10px;
  color: #ffffff;
  opacity: 0.5;
}

.nav-staking {
  position: absolute;
  bottom: 30px;
  display: flex;
  align-self: center;
  width: 64px;
  height: 64px;
}
</style>