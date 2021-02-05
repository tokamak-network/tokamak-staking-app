<template>
  <view class="staking-layout">
    <view class="staking-container">
      <text class="page-title">Stake tokens now</text>
      <text class="page-text"
        >Stake TON to earn TON.</text>
      <staking-component :layer2Address="operators[0].layer2" :selectedOperatorName="operators[0].name"/>
    </view>
  </view>
</template>
<script>
import StakingComponent from "@/components/StakingComponent";
import { mapState, mapGetters } from 'vuex';

export default {
  components: {
    'staking-component': StakingComponent,
  },
  props: {
    navigation: {
      type: Object,
    },
  },
  computed: {
    ...mapState([
      'operators',])}
};
</script>
<style scoped>
.staking-layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #FAFBFC;
}
.staking-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 4.7%;
}
.page-title {
font-size: 24px;
color: #3e495c;
font-weight: bold;
}
.page-text {
  font-size: 12px;
  color: #86929d;
  height: 5%;
  margin-bottom: 3.9%;
}
</style>