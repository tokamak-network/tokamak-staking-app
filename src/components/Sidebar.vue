<template>
<view class="sidebar-content-wrapper" :bounces="false">
   <view class="logo-container" >
        <image class="logo" :style="{width: 90, height: 58}" :source="require('../../assets/TokamakLogo.png')" />
      </view>
       <view class="logo-container" :style="{paddingBottom: 40}" >
        <image class="logo" :style="{width: 246, height: 30}" :source="require('../../assets/simple-staking-text.png')" />
      </view>

      <view class="account-info">
        <text class="info-title">ETH Balance</text>
        <text class="ton-value">{{ethBalance | currencyAmount}}</text>
        <text class="info-title">TON Balance</text>
        <text class="ton-value">{{TONbalance.value.toFixed(3)}}{{TONbalance.symbol}}</text>
         <text class="info-title">Power Balance</text>
          <text class="powerton-value">{{powerTONbalance.value}}{{powerTONbalance.symbol}}</text>
           <text class="info-title">Account Address</text>
          <text class="powerton-value">{{user.substring(0, 6) + '...' + user.substring(37, 42)}}</text>
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
import { store } from "@/store/index";
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState(['ethBalance','TONbalance', 'powerTONbalance', 'user']),
    currencyAmount () {
      return amount => this.$options.filters.currencyAmount(amount);
    },
  },
     props: {
    navigation: {
      type: Object
    }
  },
    methods: {
            handleListItemClick(dataObj) {
      this.navigation.navigate(dataObj);
      if ( dataObj === 'Login') {
         this.$store.dispatch('logout')
      }
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
  padding-bottom: 20;
  display: flex;
  flex-direction: column;
}
.sidebar-item {
  height: 50;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.account-info {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left:20px;
  margin-right: 20px
}
.info-title {
  font-size: 20px;
  margin: 5px;
}
.ton-value {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 23px;
}
.powerton-value {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
}
</style>