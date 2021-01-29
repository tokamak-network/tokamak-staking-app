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
        <text class="th-text">Reward <text :style="{color: '#2a72e5'}">TON</text></text></view
      >
      <view class="th-name" :style="{ width: '30%' }">
        <text class="th-text">End date</text></view
      >
    </view>
    <view class="table-content-container">
      <scroll-view>
        <view class="list-row" v-for="winner in winners" :key="winner.round">
          <view class="list-item" :style="{ width: '15%' }">
            <text class="list-item-text">{{
              winner.round
            }}</text>
             
          </view>
          <view  class="list-item" :style="{ width: '30%' }">
            <text class="list-item-text">{{
              winner.winner
            }}</text>
          </view>
          <view class="list-item" :style="{ width: '25%' }">
            <text class="list-item-text" :style="{color: '#2a72e5', fontWeight: 'bold'}">{{
              winner.reward
            }}</text>
          </view>
          <view class="list-item" :style="{ width: '30%' }">
            <text class="list-item-text">{{
              winner.date
            }}</text>
          </view>
         
        </view>
      </scroll-view>
    </view>
  </view>
</template>
<script>
import React from "react";
import { View, Text } from "react-native";
export default {
  data() {
    return {
      winners: [
        {
          round: 29,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 28,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 27,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 26,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 25,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 24,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 23,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 22,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 21,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 20,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 19,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 18,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 17,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 16,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 15,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 14,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 13,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 12,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 11,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 10,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 9,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 8,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 7,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 6,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 5,
          winner: "0xe300...1327",
          reward: "30,868.54",
          date: "18/12/2020",
        },
        {
          round: 4,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 3,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 2,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 1,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
        {
          round: 0,
          winner: "0x36f9...3E44",
          reward: "32,580.62",
          date: "18/12/2020",
        },
      ],
    };
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
  height: 520;
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
