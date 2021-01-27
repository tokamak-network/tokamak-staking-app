<template>
  <view class="home-layout">
    <view class="home-container">
      <image class="home-container-logo" :source="require('../../assets/sub-logo.png')" />
      <text class="page-title">Tokamak Network</text>
      <text class="page-text"
        >Stake your TON to earn Power TON</text
      >
      <text class="page-text" :style="{marginBottom: 30}"
        >and other rewards</text
      >
      <balance-component
        title="Your TON Balance"
        :balance=TONbalance
        rewards="Power TON Balance"
        :value=powerTONbalance
      />
      <balance-component
        title="Total Staked Amount"
        :balance=stakedAmount
        rewards="Expected Rewards"
        :value=rewards
      />
    </view>
  </view>
</template>
<script>
import { mapState } from "vuex";
import { store } from "@/store/index";
import Header from "@/components/Header";
import BalanceComponent from "@/components/BalanceComponent";
import Vue from "vue-native-core";

export default {
  computed: {
    ...mapState(['TONbalance', 'powerTONbalance', 'stakedAmount', 'rewards']),
  },
  components: {
    header: Header,
    "balance-component": BalanceComponent,
  },
  props: {
    navigation: {
      type: Object,
    },
  },
  methods: {},
};
</script>
<style scoped>
.home-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.home-container {
  flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding: 20px 20px;
}

.home-container-logo {
  width: 170px;
  height: 60px;
  margin-top: 40px;
  margin-bottom: 25px;
}

.page-title {
  font-size: 24px;
  text-align: center;
  color: #3e495c;
  padding: 0px 40px;
  font-weight: 700;
  margin-bottom: 5px;
}
.page-text {
  font-size: 12px;
  text-align: center;
  color: #86929d;
}
.home-balance-component {
  margin-bottom: 20px;
}
</style>