<template>
  <view
    class="balance-container"
    :style="{
      width: componentWidth,
      height: componentHeight,
      marginBottom: windowWidth * 0.031,
      paddingLeft: componentWidth * 0.063,
       paddingRight: componentWidth * 0.063,
       paddingTop: componentHeight *  0.137,
       paddingBottom: componentHeight * 0.103,
    }"
  >
    <view class="row" >
      <text class="title" :style="{height: componentHeight*0.11,marginBottom:componentHeight*0.034}">{{ title }}</text>
      <view class="text-container" :style="{marginBottom:componentHeight*0.137}">
        <view class="text-inside-container">
       
        <text class="text" >{{ balance.substring(0, balance.length - 3) }}</text>
        <text class="text-symbol" >TON</text>
        </view>
      </view>
    </view>
    <view class="divider" :style="{marginBottom:componentHeight*0.103}" />
    <view class="row">
      <view class="row-bottom" :style="{height:componentHeight*0.103}" >
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
    windowWidth() {
      return Dimensions.get("window").width;
    },
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
  border-color: #e7ebf2;
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
   /* border-width: 1;
  border-color: #cc24d3; */
}
.text-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
   /* border-width: 1;
  border-color: #ccd1d3; */
  padding: 0px;
}
.text-inside-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
   /* border-width: 1;
  border-color: #ccd155; */
}
.text {
  font-size: 30px;
  color: #131315;
  font-weight: bold;
}
.text-symbol {
  align-self: flex-end;
  padding-bottom: 5px;
  color: #000000;
  font-size: 11px;
  font-weight: bold;
}
.divider {
  width: 98%;
  height: 1px;
  background-color: #e7ebf2;
}
.row-bottom {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  color: #86929d;
}
.reward {
font-size: 11px;
}
.value {
font-size: 11px;
}
</style>
