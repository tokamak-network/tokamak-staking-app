<template>
<view class="sidebar-content-wrapper" :bounces="false">
   <view class="logo-container" >
        <image class="logo" :style="{width: 90, height: 58}" :source="require('../../assets/TokamakLogo.png')" />
      </view>
  <view class="sidebar-item">
    <touchable-opacity :on-press="()=>handleListItemClick('Home')" :style="{alignItems: 'center', justifyContent: 'center',height: 50, width: 280, backgroundColor: '#DDDDDD'}">
      <text :style="{fontSize:20}">Home</text>
    </touchable-opacity>
  </view>
  <view class="sidebar-item">
    <touchable-opacity :on-press="()=>handleListItemClick('Operators')" :style="{alignItems: 'center', justifyContent: 'center',height: 50, width: 280, backgroundColor: '#DDDDDD'}">
      <text :style="{fontSize:20}">Operators</text>
    </touchable-opacity>
  </view>
  <view class="sidebar-item">
    <touchable-opacity :on-press="()=>handleListItemClick('Staking')" :style="{alignItems: 'center', justifyContent: 'center',height: 50, width: 280, backgroundColor: '#DDDDDD'}">
      <text :style="{fontSize:20}">Staking</text>
    </touchable-opacity>
  </view>
  <view class="sidebar-item">
    <touchable-opacity :on-press="()=>handleListItemClick('PowerTON')" :style="{alignItems: 'center', justifyContent: 'center',height: 50, width: 280, backgroundColor: '#DDDDDD'}">
      <text :style="{fontSize:20}">Power TON</text>
    </touchable-opacity>
  </view>
   <view class="sidebar-item">
    <touchable-opacity :on-press="()=>handleListItemClick('Login')" :style="{alignItems: 'center', justifyContent: 'center',height: 50, width: 280, backgroundColor: '#DDDDDD'}">
      <text :style="{fontSize:20}">Logout</text>
    </touchable-opacity>
  </view>
</view>
</template>
<script>
import React from 'react';
import {Text} from 'react-native';

export default {
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
<style scoped>
.sidebar-content-wrapper {
    background-color: #fff;
    flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 50;
}
.statusBar {
  background-color: #d4d7da;
  height: 40px;
}
.logo-container {
  padding-bottom: 40;
}
.sidebar-item {
  height: 50;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>