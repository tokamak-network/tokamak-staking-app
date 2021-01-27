<template>
<view class="balance-container">
    <view class="row">
        <text class="title">{{title}}</text>
        <view class="text-container">
            <text class="text">{{balance.value.toFixed(3)}}</text>
            <text class="text-symbol">{{balance.symbol}}</text>
        </view>
    </view>
     <view class="divider" />
    <view class="row">
    <view class="row-bottom">
        <text class="reward">{{rewards}}</text>
        <text class="value">{{value.value.toFixed(3)}}{{value.symbol}}</text>
    </view>
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
    width: 320px;
    height: 146px;
    background-color: #FFFFFF;
    border-width: 1;
    border-color: #ccd1d3;
    border-radius: 10px;
    display: flex;
    padding: 20px 20px 15px 20px;
    margin-bottom: 20px;
}
.row {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}
.title {
    text-align: center;
    color: #86929d;
    font-size: 11px;
}

.text-container {
    display: flex;
    flex-direction: row;
    justify-content: center;
    font-weight: 500;

}
.text {
    font-size: 30px;
    color: #131315; 
}
.text-symbol {
    align-self: flex-end;
    padding-bottom: 5px;
}
.divider {
  width: 280px;
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