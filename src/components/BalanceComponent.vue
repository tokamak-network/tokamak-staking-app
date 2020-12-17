<template>
<view class="balance-container">
    <view class="row" style="flex-direction: column">
        <text class="title">{{title}}</text>
<text  class="text">{{balance}}</text>
    </view>
     <view class="divider" />
    <view class="row">
<text>{{rewards}}</text>
<text>{{value}}</text>
</view>
</view>
</template>
<script>
export default {
   props: ['title', 'balance', 'rewards', 'value']
}
</script>
<style scoped>
.balance-container {
    width: 290;
    background-color: #e2e8eb;
    border-width: 1;
    border-color: #ccd1d3;
    border-radius: 13;
   height: 180px;
  margin-bottom: 40;
}
.row {
    margin: 15px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.title {
    color: #8c8c8c;
    font-size: 30px;  
}
.text {
   font-size: 36px;
  font-weight: 700;
  color: #555555; 
}
.divider {
  width: 100%;
  height: 2px;
  background-color: #ccd1d3;
}
</style>