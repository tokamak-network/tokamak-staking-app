<template>
  <view class="winner-table-container">
    <view class="table-header-container">
      <view class="th-name" :style="{ width: '15%' }">
        <text class="th-text">Round</text></view
      >
      <view class="th-name" :style="{ width: '30%' }">
        <text class="th-text">Winner</text></view
      >
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text">Reward <text :style="{color: '#2a72e5'}">TON</text></text>
       
        </view
      >
      <view class="th-name" :style="{ width: '30%' }">
        <text class="th-text">End date</text></view
      >
    </view>
    <view class="table-content-container">
      <scroll-view :showsVerticalScrollIndicator="false">
        <view class="list-row" v-for="round in rounds" :key="round.index">
          <view class="list-item" :style="{ width: '15%' }">
            <text class="list-item-text">{{
             round.index
            }}</text>
             
          </view>
          <view  class="list-item" :style="{ width: '30%' }">
            <text class="list-item-text">{{
              round.winner | hexSlicer
            }}</text>
          </view>
          <view class="list-item" :style="{ width: '25%' }">
            <text class="list-item-text" :style="{color: '#2a72e5'}">{{ round.reward | currencyAmount }}</text>
          </view>
          <view class="list-item" :style="{ width: '30%' }">
            <text class="list-item-text" >{{ round.timestamp|formattedTimestamp}}</text>
          </view>
         
        </view>
      </scroll-view>
    </view>
  </view>
</template>
<script>
import React from "react";
import { mapState } from 'vuex';
import { View, Text } from "react-native";
export default {
 computed: {
    ...mapState([
      'rounds',
    ]),
    toExplorer () {
      return (type, param) => this.$options.filters.toExplorer(type, param);
    },
    formattedTimestamp () {
      return timestamp => this.$options.filters.formattedTimestamp(timestamp);
    },
  },
  methods: {
    getFlatListRenderItem(item) {
      return <Text>{item}</Text>;
    },
  },
};
</script>
<style scoped>
.winner-table-container {
  width: 90%;
  height: 85%;
}
.table-header-container {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
  padding-left: 8px;
  margin-bottom: 15px;
  
}
.th-name {
  display: flex;
  /* justify-content: center; */
  align-self: stretch;
  align-items: center;
}
.th-text {
  font-size: 12px;
  font-weight: bold;
  color: #555555;
}
.list-row {
  display: flex;
  flex-direction: row;
}
.table-content-container {
  display: flex;
  flex-direction: row;
  background-color: #FFFFFF;
  border-width: 1px;
  border-radius: 10px;
  border-color: #e7ebf2;
  padding: 5px;
  padding-top: 20px;
}
.list-row {
  display: flex;
 
  flex-direction: row;
  
}
.list-item {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 15px;
}
.list-item-text {
  font-size: 11px;
}
</style>
