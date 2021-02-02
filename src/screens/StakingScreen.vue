<template>
  <view class="staking-layout">
    <header :navigation="navigation" :back="false" />
    <view class="staking-container">
      <text class="page-title">Stake tokens now!!!</text>
      <text class="page-text"
        >Stake TON to earn TON..</text>
      <staking-component/>
    </view>
  </view>
</template>
<script>
import Header from "@/components/Header";
import StakingComponent from "@/components/StakingComponent";
export default {
  components: {
    header: Header,
    'staking-component': StakingComponent,
  },
  props: {
    navigation: {
      type: Object,
    },
  },
};
</script>
<style scoped>
.staking-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #FAFBFC;

}
.staking-container {
  flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding: 20px 40px;
}
.page-title {
  font-size: 30px;
  text-align: center;
  color: #555555;
  padding: 0px 40px;
  font-weight: 700;
}
.page-text {
  font-size: 20px;
  text-align: center;
  padding: 20px 40px;
}
</style>