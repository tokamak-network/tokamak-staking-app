<template>
  <view class="nav">
    <ImageBackground :source=Bg class="nav-bg">
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('Home')">
      <image :source="activeTab === 'Home' ? HomeIcon : HomeIconInactive" class="nav-icon" style="margin-left: 5"/>
      <text  :class="{selected: activeTab === 'Home', 'nav-text': activeTab !== 'Home' }" :style="{marginLeft: 5}">Home</text>
      </touchable-opacity>
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('Operators')">
      <image :source="activeTab === 'Operators' ? OperateIcon : OperateIconInactive" class="nav-icon"/>
      <text  :class="{selected: activeTab === 'Operators', 'nav-text': activeTab !== 'Operators'}">Operators</text>
      </touchable-opacity>
      <touchable-opacity class="nav-menu staking" :on-press="()=>handleListItemClick('Staking')">
        <image :source="activeTab === 'Staking' ? StakingIcon : StakingIconInactive" class="nav-staking" />
      <text class="nav-text-staking" :class="{selected: activeTab === 'Staking', 'nav-text': activeTab !== 'Staking'}">Staking</text>
    </touchable-opacity>
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('PowerTON')">
      <image :source="activeTab === 'PowerTON' ? TonIcon : TonIconInactive" class="nav-icon"/>
      <text :class="{selected: activeTab === 'PowerTON', 'nav-text': activeTab !== 'PowerTON'}">PowerTON</text>
      </touchable-opacity>
      <touchable-opacity class="nav-menu" :on-press="()=>handleListItemClick('Account')">
      <image :source="activeTab === 'Account' ? AccountIcon : AccountIconInactive" class="nav-icon"/>
      <text  :class="{selected: activeTab === 'Account', 'nav-text': activeTab !== 'Account'}">Account</text>
      </touchable-opacity>
    </ImageBackground>
    </view>
</template>

<script>
import { ImageBackground } from "react-native";
import { store } from "@/store/index";
import { mapState } from "vuex";

import Bg from "../../assets/nav-bar-bg.png"
import HomeIcon from "../../assets/toolbar-icon-home-active.png"
import OperateIcon from "../../assets/toolbar-icon-operators-active.png"
import StakingIcon from "../../assets/toolbar-icon-staking-active.png"
import TonIcon from "../../assets/toolbar-icon-power-active.png"
import AccountIcon from "../../assets/toolbar-icon-accoung-active.png"
import HomeIconInactive from "../../assets/toolbar-icon-home-inactive.png"
import OperateIconInactive from "../../assets/toolbar-icon-operators-inactive.png"
import StakingIconInactive from "../../assets/toolbar-icon-staking-inactive.png"
import TonIconInactive from "../../assets/toolbar-icon-power-inactive.png"
import AccountIconInactive from "../../assets/toolbar-icon-accoung-inactive.png"

export default {
  data() {
    return {  
      Bg,
      HomeIcon,
      StakingIcon,
      OperateIcon,
      TonIcon,
      AccountIcon,
      HomeIconInactive,
      OperateIconInactive,
      StakingIconInactive,
      TonIconInactive,
      AccountIconInactive,
      activeTab: "Home"
    }
  },
  props: {
    navigation: {
      type: Object
    },
  },
  methods: {
    handleListItemClick(dataObj) {
      this.activeTab = dataObj
      this.navigation.navigate(dataObj)
    }
  },
}
</script>

<style>
.nav{ 
   margin-left: -8px;
  margin-right: -8px;
  background-color: #fafbfc;
}
.nav-bg {
  width: 100%;
  height: 65px;
  display: flex;
  justify-content: space-around;
  flex-direction: row;
  align-self: stretch;
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
  width: 64px;
  height: 64px;
}

.staking {
  justify-content: flex-end;
  padding-bottom: 6px;
}

.nav-text-staking {
  left: 1px;
}

.selected {
  font-size: 10px;
  color: #ffffff;
  opacity: 1;
}
</style>