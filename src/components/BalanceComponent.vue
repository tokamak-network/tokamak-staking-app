<template>
  <view
    class="balance-container"
    :style="{
      width: componentWidth,
      height: componentHeight,
      marginBottom: componentHeight * 0.031,
      paddingLeft: componentWidth * 0.063,
       paddingRight: componentWidth * 0.063,
       paddingTop: componentHeight *  0.137,
       paddingBottom: componentHeight * 0.103,
    }"
  >
    <view class="row">
      <text class="title">{{ title }}</text>
      <view class="text-container">
        <text class="text">{{ balance.substring(0, balance.length - 3) }}</text>
        <text class="text-symbol">TON</text>
      </view>
    </view>
    <view class="divider" />
    <view class="row">
      <view class="row-bottom">
        <text class="reward">{{ rewards }}</text>
        <text class="value">{{ value }}</text>
      </view>
    </view>
  </view>
</template>
<script>
import { Dimensions } from "react-native";

export default {
  props: ["title", "balance", "rewards", "value"],
  computed: {
    // windowWidth() {
    //   return Dimensions.get("window").width;
    // },
    // windowHeight() {
    //   return Dimensions.get("window").height;
    // },
    componentHeight() {
      return Dimensions.get("window").height * 0.228 
    }, 
    componentWidth () {
     return Dimensions.get("window").width * 0.889 
    }
  },
};
</script>
<style scoped>
.balance-container {
  background-color: #ffffff;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 10px;
  display: flex;
}
.row {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.title {
  text-align: center;
  color: #86929d;
  font-size: 13px;
}
.text-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  font-weight: 500;
}
.text {
  font-size: 32px;
  color: #131315;
  font-weight: bold;
}
.text-symbol {
  align-self: flex-end;
  padding-bottom: 5px;
  color: #000000;
}
.divider {
  width: 98%;
  height: 1px;
  background-color: #e7ebf2;
  margin-top: 20px;
  margin-bottom: 15px;
}
.row-bottom {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 11px;
  color: #86929d;
}
</style>
