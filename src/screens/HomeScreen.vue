<template>
  <view class="home-layout" :style="{paddingTop: windowHeight*0.063}">
   
       <image :source="require('../../assets/sub-logo.png')"  :style="{width: windowWidth*0.472, height:windowHeight*0.094, resizeMode: 'contain'}"/>
      <text class="page-title" :style="{marginTop: windowHeight*0.039, marginBottom: windowHeight*0.008}">Tokamak Network</text>
      <text class="page-text"
        >Stake your TON to earn Power TON</text
      >
      <text class="page-text" :style="{marginBottom: windowHeight*0.047}" 
        >and other rewards</text
      >
      <balance-component
        title="Your TON Balance"
        :balance="currencyAmount(tonBalance)"
        rewards="Power TON Balance"
        :value="currencyAmount(power)"
      />
      <balance-component
        title="Total Staked Amount"
        :balance="currencyAmount(userTotalStaked)"
        rewards="Expected Rewards"
        :value="expectedRewards()"
      />
    
  </view>
</template>
<script>
import { mapState,mapGetters } from "vuex";
import { store } from "@/store/index";
import Header from "@/components/Header";
import BalanceComponent from "@/components/BalanceComponent";
import Vue from "vue-native-core";
import { Dimensions } from 'react-native';

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
     windowWidth () {
      return Dimensions.get('window').width
    },
    windowHeight () {
      return Dimensions.get('window').height
    }
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
  created () {
    setInterval(() => this.expectedRewards(), 1000);
  },
  methods: {
    expectedRewards () {
      return this.currencyAmount(this.userTotalSeigs);
    },
  },
};
</script>
<style scoped>
.home-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #FAFBFC;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
}
.home-container {
  flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
}

.page-title {
   font-size: 24px;
  color: #3e495c;
  font-weight: 700;
  text-align: center;
}
.page-text {
  font-size: 12px;
  text-align: center;
  color: #86929d;
}

</style>