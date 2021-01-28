<template>
  <view class="home-layout">
    <header :navigation="navigation" :back="false" />
    <view class="home-container">
      <text class="page-title">Stake tokens now!!!</text>
      <text class="page-text"
        >Stake your TON to win Power TON and other rewards..</text
      >
      <balance-component
        title="Your TON Balance"
        :balance="currencyAmount(tonBalance)"
        rewards="Power TON Balance"
        :value="currencyAmount(power)"
      />
      <balance-component
        title="Total Staked Amount"
        :balance="currencyAmount(tonBalance)"
        rewards="Expected Rewards"
        :value="currencyAmount(userTotalSeigs)"
      />
    </view>
  </view>
</template>
<script>
import { mapState,mapGetters } from "vuex";
import { store } from "@/store/index";
import Header from "@/components/Header";
import BalanceComponent from "@/components/BalanceComponent";
import Vue from "vue-native-core";

export default {
  computed: {
    ...mapState(['tonBalance', 'power', 'stakedAmount', 'rewards']),
    ...mapGetters([
      'userTotalStaked',
      'userTotalSeigs',
    ]),
     currencyAmount () {
      return amount => this.$options.filters.currencyAmount(amount);
    },
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